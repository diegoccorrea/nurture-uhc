package us.polarismc.polarisuhc.managers.info.scoreboard;

import me.catcoder.sidebar.ProtocolSidebar;
import me.catcoder.sidebar.Sidebar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.managers.player.UHCPlayer;
import us.polarismc.polarisuhc.managers.uhc.UHCState;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ScoreboardManager {
    private final Main plugin;
    private final Map<UHCState, StateConfig> stateConfigs = new EnumMap<>(UHCState.class);
    private final Map<UHCState, Sidebar<Component>> activeScoreboards = new EnumMap<>(UHCState.class);

    public ScoreboardManager(Main plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public record StateConfig(String title, List<String> lines) {}

    public void loadConfig() {
        stateConfigs.clear();

        File folder = plugin.getDataFolder();
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File configFile = new File(folder, "scoreboard.yml");
        if (!configFile.exists()) {
            try (InputStream in = plugin.getResource("scoreboard.yml")) {
                if (in != null) {
                    Files.copy(in, configFile.toPath());
                }
            } catch (IOException e) {
                plugin.getLogger().warning("Failed to copy default scoreboard.yml: " + e.getMessage());
            }
        }

        org.bukkit.configuration.file.YamlConfiguration config = org.bukkit.configuration.file.YamlConfiguration.loadConfiguration(configFile);

        for (UHCState state : UHCState.values()) {
            String path = state.name();
            if (config.contains(path)) {
                String title = config.getString(path + ".title", state.name());
                List<String> lines = config.getStringList(path + ".lines");
                stateConfigs.put(state, new StateConfig(title, lines));
            }
        }
    }

    public void reloadConfig() {
        loadConfig();
    }

    private String expand(String line, UHCPlayer uhcPlayer) {
        try {
            Player bukkitPlayer = uhcPlayer.getPlayer();

            int players = plugin.uhc.getAlivePlayers().size() + plugin.uhc.getDeadPlayers().size();
            int alive = plugin.uhc.getAlivePlayers().size();
            int dead = plugin.uhc.getDeadPlayers().size();

            long spectators = plugin.player.getOnlinePlayers().stream()
                    .filter(p -> !p.isPlaying())
                    .count();

            String state = plugin.uhc.getState().toString();
            String time = plugin.timer.getFormatted() != null ? plugin.timer.getFormatted() : "00:00:00";

            String next = "";
            if (bukkitPlayer != null) {
                next = plugin.timer.actionBarNext(bukkitPlayer);
            }

            int border = 0;
            try {
                border = plugin.uhc.border.getCurrentBorder();
            } catch (Exception ignored) {}

            int kills = 0;
            String teamName = "None";
            if (uhcPlayer.getTeam() != null) {
                kills = uhcPlayer.getTeam().getKills();
                teamName = uhcPlayer.getTeam().getTeamName();
            }

            return line
                    .replace("%players%", String.valueOf(players))
                    .replace("%alive%", String.valueOf(alive))
                    .replace("%dead%", String.valueOf(dead))
                    .replace("%spectators%", String.valueOf(spectators))
                    .replace("%state%", state)
                    .replace("%time%", time)
                    .replace("%next%", next)
                    .replace("%border%", String.valueOf(border))
                    .replace("%kills%", String.valueOf(kills))
                    .replace("%team%", teamName);
        } catch (Exception e) {
            return line;
        }
    }

    public void createScoreboardsForAllPlayers() {
        UHCState currentState = plugin.uhc.getState();
        Sidebar<Component> sidebar = createScoreboard(currentState);
        activeScoreboards.put(currentState, sidebar);
        for (Player player : Bukkit.getOnlinePlayers()) {
            sidebar.addViewer(player);
        }
    }

    public void onPlayerJoin(Player player) {
        UHCState currentState = plugin.uhc.getState();
        Sidebar<Component> sidebar = activeScoreboards.get(currentState);
        if (sidebar == null) {
            sidebar = createScoreboard(currentState);
            activeScoreboards.put(currentState, sidebar);
        }
        sidebar.addViewer(player);
    }

    public void onPlayerQuit(Player player) {
        UHCState currentState = plugin.uhc.getState();
        Sidebar<Component> sidebar = activeScoreboards.get(currentState);

        if (sidebar != null) {
            sidebar.removeViewer(player);
        }
    }

    private StateConfig getConfigForState(UHCState state) {
        return stateConfigs.getOrDefault(state, new StateConfig(state.name(), List.of()));
    }

    public Sidebar<Component> createScoreboard(@NotNull UHCState state) {
        final Sidebar<Component> sidebar = ProtocolSidebar.newAdventureSidebar(
                plugin.utils.chat(getConfigForState(state).title()),
                plugin
        );

        for (String line : getConfigForState(state).lines()) {
            if (line.trim().isEmpty()) {
                sidebar.addBlankLine();
                continue;
            }

            if (line.contains("%")) {
                sidebar.addUpdatableLine(player -> {
                    UHCPlayer uhcPlayer = plugin.player.getUHCPlayer(player);
                    if (uhcPlayer == null) {
                        return Component.empty();
                    }
                    return plugin.utils.chat(expand(line, uhcPlayer));
                });
                continue;
            }

            sidebar.addLine(plugin.utils.chat(line));
        }

        sidebar.updateLinesPeriodically(0, 20); // 1 second update interval
        return sidebar;
    }
    public void cleanup() {

    }
}