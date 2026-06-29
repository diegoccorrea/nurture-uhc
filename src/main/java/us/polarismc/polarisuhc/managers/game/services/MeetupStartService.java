package us.polarismc.polarisuhc.managers.game.services;

import io.papermc.paper.registry.keys.SoundEventKeys;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.events.MeetupStartEvent;
import us.polarismc.polarisuhc.managers.game.timer.EventAnchor;
import us.polarismc.polarisuhc.managers.player.UHCPlayer;
import us.polarismc.polarisuhc.managers.scenario.ScenarioProperty;
import us.polarismc.polarisuhc.managers.uhc.UHCState;

import java.util.ArrayList;
import java.util.List;

public class MeetupStartService {
    private final Main plugin;
    public MeetupStartService(Main plugin) {
        this.plugin = plugin;
    }

    private final List<UHCPlayer> teleportedPlayers = new ArrayList<>();

    public void startMeetup() {
        plugin.uhc.setState(UHCState.MEETUP);
        boolean disabledOverworld = plugin.scen.hasProperty(ScenarioProperty.DISABLES_OVERWORLD);
        boolean enablesNetherInMeetup = plugin.scen.hasProperty(ScenarioProperty.ENABLES_NETHER_IN_MEETUP);
        boolean nether = plugin.uhc.toggle.isNether();

        broadcastMeetupStart(disabledOverworld, enablesNetherInMeetup, nether);
        applyWorldBorders(disabledOverworld, enablesNetherInMeetup, nether);
        scatterNetherPlayers(disabledOverworld, enablesNetherInMeetup, nether);
        Bukkit.getPluginManager().callEvent(new MeetupStartEvent(plugin.uhc.getAlivePlayers(), teleportedPlayers));
    }

    private void broadcastMeetupStart(boolean disabledOverworld, boolean enablesNetherInMeetup, boolean nether) {
        boolean tpBorder = plugin.uhc.border.isTpBorder();

        if (!disabledOverworld) {
            int finalOW = plugin.uhc.border.getMeetupBorder() / 2;
            if (tpBorder) {
                int startOW = plugin.uhc.border.getBorderList().getFirst() / 2;
                plugin.utils.broadcast(SoundEventKeys.BLOCK_BEACON_ACTIVATE,
                        "<aqua>Meetup <gray>has started! The border shrunk to <white>" + startOW + "x" + startOW +
                                "<gray> and will start shrinking every <white>" + plugin.uhc.border.getBorderTimer() +
                                "m<gray> until it becomes <white>" + finalOW + "x" + finalOW);
            } else {
                plugin.utils.broadcast(SoundEventKeys.BLOCK_BEACON_ACTIVATE,
                        "<aqua>Meetup <gray>has started! The border is shrinking to <white>" + finalOW + "x" + finalOW +
                                "<gray> at <white>" + plugin.uhc.border.getBorderSpeed() + "<gray> blocks per second.");
            }
        }

        if (!nether) return;

        String prefix = disabledOverworld ? "Meetup has started! The" : "Also, the";
        if (enablesNetherInMeetup || disabledOverworld) {
            int finalNether = plugin.uhc.border.getNetherMeetupBorder() / 2;
            boolean netherTpBorder = plugin.uhc.border.isNetherTPBorder();
            if (netherTpBorder) {
                int startNether = plugin.uhc.border.getNetherBorderList().getFirst() / 2;
                plugin.utils.broadcast("<aqua>" + prefix + " <red>nether<aqua> border shrunk to <white>" + startNether + "x" + startNether +
                        "<aqua> and will start shrinking every <white>" + plugin.uhc.border.getNetherBorderTimer() +
                        "m<aqua> until it becomes <white>" + finalNether + "x" + finalNether);
            } else {
                plugin.utils.broadcast("<aqua>" + prefix + " <red>nether<aqua> border is shrinking to <white>" + finalNether + "x" + finalNether +
                        "<aqua> at <white>" + plugin.uhc.border.getNetherBorderSpeed() + "<aqua> blocks per second.");
            }
        }
    }

    private void scatterNetherPlayers(boolean disabledOverworld, boolean enablesNetherInMeetup, boolean nether) {
        if (!nether || disabledOverworld || enablesNetherInMeetup) return;

        World netherWorld = plugin.uhc.world.getNetherWorld();
        plugin.uhc.getPlayingPlayers().stream().filter(player -> player.getWorld().equals(netherWorld)).forEach(player -> {
            Location loc = plugin.game.locationService.findSafeScatterLocation();
            plugin.player.teleport(player, loc);
            teleportedPlayers.add(plugin.player.getUHCPlayer(player));
            plugin.utils.message(player, "<red>You have been teleported to a random location!");
        });
    }

    private void applyWorldBorders(boolean disablesOverworld, boolean enablesNetherInMeetup, boolean nether) {
        boolean tpBorder = plugin.uhc.border.isTpBorder();
        boolean netherTpBorder = plugin.uhc.border.isNetherTPBorder();

        if (!disablesOverworld) {
            if (tpBorder) {
                executeTPBorder(false);
            } else {
                WorldBorder overworldBorder = plugin.uhc.world.getUhcWorld().getWorldBorder();
                int meetupBorder = plugin.uhc.border.getMeetupBorder();
                int currentBorder = plugin.uhc.border.getBorder();
                long time = (long) ((currentBorder - meetupBorder) / plugin.uhc.border.getBorderSpeed());
                overworldBorder.changeSize(meetupBorder, time);
            }
        }

        if (nether && (enablesNetherInMeetup || disablesOverworld)) {
            if (netherTpBorder) {
                executeTPBorder(true);
            } else {
                WorldBorder netherBorder = plugin.uhc.world.getNetherWorld().getWorldBorder();
                int netherMeetup = plugin.uhc.border.getNetherMeetupBorder();
                int netherCurrent = plugin.uhc.border.getNetherBorder();
                long netherTime = (long) ((netherCurrent - netherMeetup) / plugin.uhc.border.getNetherBorderSpeed());
                netherBorder.changeSize(netherMeetup, netherTime);
            }
        }
    }

    private void executeTPBorder(boolean isNether) {
        List<Integer> borderList = isNether ? plugin.uhc.border.getNetherBorderList() : plugin.uhc.border.getBorderList();
        int borderTimer = isNether ? plugin.uhc.border.getNetherBorderTimer() : plugin.uhc.border.getBorderTimer();

        World world = isNether ? plugin.uhc.world.getNetherWorld() : plugin.uhc.world.getUhcWorld();
        world.getWorldBorder().setSize(borderList.getFirst());
        teleportAllPlayersToNearestLocation(borderList.getFirst() / 2, isNether, true);

        for (int i = 1; i < borderList.size(); i++) {
            int border = borderList.get(i);
            int offsetSeconds = borderTimer * 60 * i;
            int radius = border / 2;

            String warnBase = (isNether ? "<aqua>The <red>nether<aqua> border" : "<aqua>The border");

            plugin.timer.addEvent(EventAnchor.POST_MEETUP, offsetSeconds - 3, () ->
                    plugin.utils.broadcast(warnBase + " is shrinking to <white>" + radius + "x" + radius + "</white> in <white>3</white> seconds."));
            plugin.timer.addEvent(EventAnchor.POST_MEETUP, offsetSeconds - 2, () ->
                    plugin.utils.broadcast(warnBase + " is shrinking to <white>" + radius + "x" + radius + "</white> in <white>2</white> seconds."));
            plugin.timer.addEvent(EventAnchor.POST_MEETUP, offsetSeconds - 1, () ->
                    plugin.utils.broadcast(warnBase + " is shrinking to <white>" + radius + "x" + radius + "</white> in <white>1</white> second."));
            plugin.timer.addEvent(EventAnchor.POST_MEETUP, offsetSeconds, () -> {
                world.getWorldBorder().setSize(border);
                plugin.uhc.border.advanceBorderIndex();


                String message = isNether ? "<aqua>The <red>nether<aqua> border has shrunk to <white>" + radius + "x" + radius + "."
                        : "<aqua>The border has shrunk to <white>" + radius + "x" + radius + ".";
                plugin.utils.broadcast(message);

                teleportAllPlayersToNearestLocation(radius, isNether, false);
            });
        }
    }

    private int getInvulnerabilityDurationTicks() {
        return plugin.getConfig().getInt("settings.meetup.invulnerability-duration-ticks", 40);
    }

    private int getFireResistanceDurationTicks() {
        return plugin.getConfig().getInt("settings.meetup.fire-resistance-duration-ticks", 200);
    }

    private void teleportAllPlayersToNearestLocation(int radius, boolean isNether, boolean first) {
        plugin.uhc.getPlayingPlayers().forEach(player -> {
            if (isNether != (player.getWorld().getEnvironment() == World.Environment.NETHER)) return;
            Location loc = player.getLocation();
            if (loc.getBlockX() >= radius || loc.getBlockX() < -radius || loc.getBlockZ() >= radius || loc.getBlockZ() < -radius) {
                teleportToNearestLocation(radius, player);
            }
            if (first) {
                teleportedPlayers.add(plugin.player.getUHCPlayer(player));
            }
        });
    }

    private void teleportToNearestLocation(int radius, Player player) {
        World world = player.getWorld();
        Location loc = player.getLocation();

        int x = loc.getBlockX();
        int z = loc.getBlockZ();
        int y;

        double halfWb = (double) radius - 0.5;
        double negHalfWb = -halfWb;

        if (x >= halfWb) loc.setX(halfWb);
        if (x <= negHalfWb) loc.setX(negHalfWb);
        if (z >= halfWb) loc.setZ(halfWb);
        if (z <= negHalfWb) loc.setZ(negHalfWb);

        int newX = loc.getBlockX();
        int newZ = loc.getBlockZ();

        if (world.getEnvironment() == World.Environment.NETHER) {
            y = -1;
            for (int i = 20; i < 110; i++) {
                Material below = world.getBlockAt(newX, i - 1, newZ).getType();
                Material current = world.getBlockAt(newX, i, newZ).getType();
                Material above = world.getBlockAt(newX, i + 1, newZ).getType();
                if (below != Material.AIR && below != Material.LAVA && current == Material.AIR && above == Material.AIR) {
                    y = i;
                    break;
                }
            }
            if (y == -1) {
                teleportToNearestLocation(radius - 10, player);
                return;
            }
        } else {
            y = world.getHighestBlockYAt(newX, newZ) + 2;
        }

        if (y <= 1) {
            teleportToNearestLocation(radius - 10, player);
            return;
        }

        loc.setY(y);

        plugin.utils.message(player, "<aqua>You have been teleported because you were out of the border! You're invulnerable for 2 seconds.");
        player.setInvulnerable(true);
        plugin.utils.delay(getInvulnerabilityDurationTicks(), () -> {
            if (player.isOnline()) {
                player.setInvulnerable(false);
            }
        });

        if (world.getEnvironment() == World.Environment.NETHER && !player.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)) {
            plugin.utils.message(player, "<aqua>You also received Fire Resistance for 10 seconds.");
            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, getFireResistanceDurationTicks(), 0));
        }

        plugin.player.teleport(player, loc);
    }
}
