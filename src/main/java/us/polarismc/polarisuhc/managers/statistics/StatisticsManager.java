package us.polarismc.polarisuhc.managers.statistics;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.database.StatisticsRepository;
import us.polarismc.polarisuhc.managers.player.MinedResource;
import us.polarismc.polarisuhc.managers.player.UHCPlayer;

import java.util.UUID;

public class StatisticsManager implements Listener {
    private final Main plugin;
    private StatisticsRepository repo;
    private int currentGameId = -1;

    public StatisticsManager(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private StatisticsRepository getRepo() {
        if (repo == null) {
            repo = new StatisticsRepository(plugin);
        }
        return repo;
    }

    public void startGame(UUID hostUuid) {
        if (!isInGame()) return;
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            currentGameId = getRepo().createGame(hostUuid);
            plugin.getLogger().info("Statistics: Started game " + currentGameId + " with host " + hostUuid);
        });
    }

    public void trackKill(UHCPlayer killer, UHCPlayer victim) {
        if (!isInGame()) return;
        killer.addKill();
        victim.addDeath();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            getRepo().savePlayerStats(
                killer.getUniqueId(),
                killer.getName(),
                currentGameId,
                killer.getKills(),
                killer.getDeaths(),
                killer.getMinedResource(MinedResource.DIAMOND),
                false
            );
            getRepo().savePlayerStats(
                victim.getUniqueId(),
                victim.getName(),
                currentGameId,
                victim.getKills(),
                victim.getDeaths(),
                victim.getMinedResource(MinedResource.DIAMOND),
                false
            );
        });
    }

    public void trackDiamond(UHCPlayer player) {
        if (!isInGame()) return;
        player.addDiamond();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            getRepo().savePlayerStats(
                player.getUniqueId(),
                player.getName(),
                currentGameId,
                player.getKills(),
                player.getDeaths(),
                player.getMinedResource(MinedResource.DIAMOND),
                false
            );
        });
    }

    public void flushPlayerStats(UHCPlayer player) {
        if (!isInGame()) return;
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            getRepo().savePlayerStats(
                player.getUniqueId(),
                player.getName(),
                currentGameId,
                player.getKills(),
                player.getDeaths(),
                player.getMinedResource(MinedResource.DIAMOND),
                false
            );
        });
    }

    public void endGame(UUID winner) {
        if (!isInGame()) return;
        plugin.getLogger().info("Statistics: Game ended with winner " + winner + " - victory tracking not yet wired");
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            if (winner != null) {
                getRepo().savePlayerStats(
                    winner,
                    Bukkit.getPlayer(winner) != null ? Bukkit.getPlayer(winner).getName() : "unknown",
                    currentGameId,
                    0, 0, 0, true
                );
            }
            getRepo().endGame(currentGameId, winner);
            currentGameId = -1;
        });
    }

    private boolean isInGame() {
        return plugin.uhc != null && plugin.uhc.hasStarted() && !plugin.uhc.isFinalized();
    }
}
