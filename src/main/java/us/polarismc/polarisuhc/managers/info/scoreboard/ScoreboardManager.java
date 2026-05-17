package us.polarismc.polarisuhc.managers.info.scoreboard;

import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.managers.player.UHCPlayer;
import us.polarismc.polarisuhc.managers.uhc.UHCState;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ScoreboardManager {
    private final Main plugin;
    private final Map<UUID, FastBoard> boards = new HashMap<>();
    private Map<UHCState, StateConfig> stateConfigs = new EnumMap<>(UHCState.class);
    private int updateTaskId = -1;

    public ScoreboardManager(Main plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public record StateConfig(String title, List<String> lines) {}

    public void loadConfig() {
        stateConfigs.clear();

        File folder = plugin.getDataFolder();
        if (folder == null || !folder.exists()) {
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

    public void onPlayerJoin(Player player) {
        UHCPlayer uhcPlayer = plugin.player.getUHCPlayer(player);
        if (uhcPlayer == null) return;

        FastBoard board = new FastBoard(player);
        boards.put(player.getUniqueId(), board);

        updatePlayer(uhcPlayer);
    }

    public void onPlayerQuit(Player player) {
        FastBoard board = boards.remove(player.getUniqueId());
        if (board != null) {
            board.delete();
        }
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
            if (plugin.timer != null && bukkitPlayer != null) {
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

    private StateConfig getConfigForState(UHCState state) {
        return stateConfigs.getOrDefault(state, new StateConfig(state.name(), List.of()));
    }

    public void updatePlayer(UHCPlayer uhcPlayer) {
        Player player = uhcPlayer.getPlayer();
        if (player == null || !player.isOnline()) return;

        FastBoard board = boards.get(player.getUniqueId());
        if (board == null) return;

        UHCState currentState = plugin.uhc.getState();
        StateConfig config = getConfigForState(currentState);

        String title = expand(config.title(), uhcPlayer);
        board.updateTitle(title);

        List<String> expandedLines = config.lines().stream()
                .map(line -> expand(line, uhcPlayer))
                .map(line -> line)
                .toList();
        board.updateLines(expandedLines);
    }

    public void updateAll() {
        for (Map.Entry<UUID, FastBoard> entry : boards.entrySet()) {
            Player player = Bukkit.getPlayer(entry.getKey());
            if (player == null || !player.isOnline()) continue;

            UHCPlayer uhcPlayer = plugin.player.getUHCPlayer(player);
            if (uhcPlayer != null) {
                updatePlayer(uhcPlayer);
            }
        }
    }

    public void restartUpdateTask() {
        if (updateTaskId != -1) {
            Bukkit.getScheduler().cancelTask(updateTaskId);
            updateTaskId = -1;
        }

        updateTaskId = Bukkit.getScheduler().runTaskTimer(plugin, () -> updateAll(), 20L, 20L).getTaskId();
    }

    public void cleanup() {
        if (updateTaskId != -1) {
            Bukkit.getScheduler().cancelTask(updateTaskId);
            updateTaskId = -1;
        }

        for (FastBoard board : boards.values()) {
            board.delete();
        }
        boards.clear();
    }
}