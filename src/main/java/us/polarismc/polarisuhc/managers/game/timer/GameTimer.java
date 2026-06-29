package us.polarismc.polarisuhc.managers.game.timer;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.managers.scenario.ScenarioProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameTimer extends BukkitRunnable {
    private final Main plugin;

    @Getter private boolean running = false;
    private int elapsedSeconds;

    private int finalheal;
    private int pvptime;
    private int meetuptime;

    @Getter private String formatted;

    private int[] pvpReminders;
    private int[] meetupReminders;

    private final List<GameEvent> events = new ArrayList<>();

    public GameTimer(Main plugin) {
        this.plugin = plugin;
    }

    private int[] getPvpReminders() {
        if (pvpReminders == null) {
            pvpReminders = parseCsvIntArray(plugin.getConfig().getString("settings.timer.pvp-reminders", "3600,1800,900,600,300,60"));
        }
        return pvpReminders;
    }

    private int[] getMeetupReminders() {
        if (meetupReminders == null) {
            meetupReminders = parseCsvIntArray(plugin.getConfig().getString("settings.timer.meetup-reminders", "3600,1800,900,600,300,60"));
        }
        return meetupReminders;
    }

    private int[] parseCsvIntArray(String csv) {
        return Arrays.stream(csv.split(","))
                .map(String::trim)
                .mapToInt(Integer::parseInt)
                .toArray();
    }

    public void start() {
        if (running) return;
        elapsedSeconds = 0;

        finalheal = plugin.uhc.duration.getFinalHealTime() * 60;
        if (finalheal <= 0) finalheal = -1;

        pvptime = plugin.uhc.duration.getPvpTime() * 60;
        if (pvptime <= 0) pvptime = 1;

        meetuptime = plugin.uhc.duration.getMeetupTime() * 60;
        if (meetuptime <= 0) meetuptime = 1;

        running = true;
        this.runTaskTimer(plugin, 20L, 20L);
    }

    public void stop() {
        running = false;
        this.cancel();
    }

    @Override
    public void run() {
        if (!running || plugin.uhc.isFinalized()) {
            stop();
            return;
        }

        int hor = elapsedSeconds / 3600;
        int min = (elapsedSeconds % 3600) / 60;
        int sec = elapsedSeconds % 60;
        formatted = String.format("%02d:%02d:%02d", hor, min, sec);

        checkPvPReminders();
        checkMeetupReminders();

        if (finalheal > 0 && elapsedSeconds == finalheal) {
            plugin.game.giveFinalHeal();
        }
        if (elapsedSeconds == pvptime) {
            plugin.game.startPvP();
        }
        if (elapsedSeconds == pvptime + meetuptime) {
            plugin.game.startMeetup();
        }

        for (GameEvent event : events) {
            if (event.fired) continue;

            int target = event.absoluteSecond(finalheal, pvptime, meetuptime);
            if (target < 0) continue;

            if (elapsedSeconds == target) {
                event.fired = true;
                event.action.run();
            }
        }

        elapsedSeconds++;
    }

    public void addEvent(EventAnchor anchor, int seconds, Runnable action) {
        events.add(new GameEvent(anchor, seconds, action));
    }

    public boolean setFinalHealTime(int minutes) {
        if (plugin.uhc.hasPvPStarted()) return false;
        int newValue = minutes * 60;
        if (newValue <= 0) {
            finalheal = -1;
        } else {
            finalheal = newValue;
        }
        resetUnfiredEventsForAnchors(EventAnchor.PRE_FINAL_HEAL, EventAnchor.POST_FINAL_HEAL);
        return true;
    }

    public boolean setPvpTime(int minutes) {
        if (plugin.uhc.hasPvPStarted()) return false;
        pvptime = Math.max(1, minutes * 60);
        resetUnfiredEventsForAnchors(EventAnchor.PRE_PVP, EventAnchor.POST_PVP, EventAnchor.PRE_MEETUP, EventAnchor.POST_MEETUP);
        return true;
    }

    public boolean setMeetupTime(int minutes) {
        if (plugin.uhc.hasMeetupStarted()) return false;
        meetuptime = Math.max(1, minutes * 60);
        resetUnfiredEventsForAnchors(EventAnchor.PRE_MEETUP, EventAnchor.POST_MEETUP);
        return true;
    }

    private void resetUnfiredEventsForAnchors(EventAnchor... anchors) {
        for (GameEvent event : events) {
            for (EventAnchor anchor : anchors) {
                if (event.anchor == anchor) {
                    int newTarget = event.absoluteSecond(finalheal, pvptime, meetuptime);
                    if (newTarget > elapsedSeconds) {
                        event.fired = false;
                    }
                }
            }
        }
    }

    private void checkPvPReminders() {
        if (plugin.uhc.hasPvPStarted()) return;
        int remaining = pvptime - elapsedSeconds;
        if (remaining < 0) return;

        for (int milestone : getPvpReminders()) {
            if (remaining == milestone) {
                plugin.utils.broadcast("<blue>PvP On<gray> is in <aqua>" + formatDuration(milestone) + "</aqua>");
            }
        }
    }

    private void checkMeetupReminders() {
        if (!plugin.uhc.hasPvPStarted()) return;
        int remaining = (pvptime + meetuptime) - elapsedSeconds;
        if (remaining < 0) return;

        for (int milestone : getMeetupReminders()) {
            if (remaining == milestone) {
                sendMeetupReminder(milestone);
            }
        }
    }

    private void sendMeetupReminder(int milestone) {
        boolean enabledNetherInMeetup = plugin.scen.hasProperty(ScenarioProperty.ENABLES_NETHER_IN_MEETUP);
        boolean disabledOverworld = plugin.scen.hasProperty(ScenarioProperty.DISABLES_OVERWORLD);
        boolean nether = plugin.uhc.toggle.isNether();
        boolean tpBorder = plugin.uhc.border.isTpBorder();

        plugin.utils.broadcast("<gold>Meetup<gray> is in <yellow>" + formatDuration(milestone) + "</yellow>");

        if (!disabledOverworld) {
            int finalOver = plugin.uhc.border.getMeetupBorder() / 2;
            if (tpBorder) {
                int startOver = plugin.uhc.border.getBorderList().getFirst() / 2;
                plugin.utils.broadcast("<aqua>At Meetup, the border will shrink to <white>" + finalOver + "x" + finalOver +
                        "<aqua> and will start shrinking every <white>" + plugin.uhc.border.getBorderTimer() +
                        "m<aqua> until it becomes <white>" + startOver + "x" + startOver);
            } else {
                plugin.utils.broadcast("<aqua>At Meetup, the border will shrink to <white>" + finalOver + "x" + finalOver +
                        "<aqua> at <white>" + plugin.uhc.border.getBorderSpeed() + "<aqua> blocks per second.");
            }
        }

        if (!nether) return;

        String prefix = disabledOverworld ? "At Meetup" : "Also";
        if (enabledNetherInMeetup || disabledOverworld) {
            int finalNether = plugin.uhc.border.getNetherMeetupBorder() / 2;
            boolean netherTpBorder = plugin.uhc.border.isNetherTPBorder();
            if (netherTpBorder) {
                int startNether = plugin.uhc.border.getNetherBorderList().getFirst() / 2;
                plugin.utils.broadcast("<aqua>" + prefix + ", the <red>nether<aqua> border will shrink to <white>" + startNether + "x" + startNether +
                        "<aqua> and will start shrinking every <white>" + plugin.uhc.border.getNetherBorderTimer() +
                        "m<aqua> until it becomes <white>" + finalNether + "x" + finalNether);
            } else {
                plugin.utils.broadcast("<aqua>" + prefix + ", the <red>nether<aqua> border will shrink to <white>" + finalNether + "x" + finalNether +
                        "<aqua> at <white>" + plugin.uhc.border.getNetherBorderSpeed() + "<aqua> blocks per second.");
            }
        } else {
            plugin.utils.broadcast("<gray>" + prefix + ", all the people in the <red>Nether</red> will be teleported to a random location in the Overworld.");
        }
    }

    private String formatDuration(int totalSeconds) {
        int hours   = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        if (hours > 0 && minutes == 0 && seconds == 0) return hours + "h";
        if (hours == 0 && minutes > 0 && seconds == 0) return minutes + "m";

        StringBuilder builder = new StringBuilder();
        if (hours   > 0) builder.append(hours).append("h ");
        if (minutes > 0) builder.append(minutes).append("m ");
        if (seconds > 0 || builder.isEmpty()) builder.append(seconds).append("s");

        return builder.toString().trim();
    }

    public String actionBarNext(Player player) {
        if (!plugin.uhc.hasPvPStarted()) return remainingPvP();
        if (!plugin.uhc.hasMeetupStarted()) return remainingMeetup();

        return "<aqua>WorldBorder <dark_gray>»</dark_gray> " + plugin.utils.getBorderSizeString(player);
    }

    public String remainingFinalHeal() {
        return remainingUntil("Final Heal", finalheal);
    }

    public String remainingPvP() {
        return remainingUntil("PvP", pvptime);
    }

    public String remainingMeetup() {
        return remainingUntil("Meetup", pvptime + meetuptime);
    }

    private String remainingUntil(String label, int targetSeconds) {
        int remaining = Math.max(0, targetSeconds - elapsedSeconds);

        int hours = remaining / 3600;
        int minutes = (remaining % 3600) / 60;
        int seconds = remaining % 60;

        StringBuilder time = new StringBuilder();
        if (hours > 0) time.append(hours).append("h ");
        time.append(minutes).append("m ").append(seconds).append("s");

        return "<aqua>" + label + " <dark_gray>» <gray>" + time;
    }
}