package us.polarismc.polarisuhc.managers.player;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import us.polarismc.polarisuhc.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("FieldCanBeLocal")
public class PlayerManager implements Listener {
    private final Main plugin;
    @Getter private final List<UHCPlayer> playerList = new ArrayList<>();

    public PlayerManager(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (getUHCPlayer(player) == null) {
            playerList.add(new UHCPlayer(event.getPlayer()));
        }
        if (plugin.info != null && plugin.info.board != null) {
            plugin.info.board.onPlayerJoin(player);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (player.isInvulnerable()) {
            player.setInvulnerable(false);
        }
        if (plugin.info != null && plugin.info.board != null) {
            plugin.info.board.onPlayerQuit(player);
        }
    }

    public void removeAllPassengers(Player player) {
        TextDisplay display = getUHCPlayer(player).getNametag();
        player.getPassengers().stream().filter(passenger -> passenger != display).toList().forEach(player::removePassenger);
    }

    public UHCPlayer getUHCPlayer(@NotNull Player player) {
        return getUHCPlayer(player.getUniqueId());
    }

    public @Nullable UHCPlayer getUHCPlayer(String name) {
        return playerList.stream().filter(player -> player.getName().equals(name)).findFirst().orElse(null);
    }

    public @Nullable UHCPlayer getUHCPlayer(UUID uniqueId) {
        return playerList.stream().filter(player -> player.getUniqueId().equals(uniqueId)).findFirst().orElse(null);
    }

    public List<UHCPlayer> getOnlinePlayers() {
        return playerList.stream().filter(UHCPlayer::isOnline).toList();
    }

    public List<UHCPlayer> getPlayingPlayers() {
        return playerList.stream().filter(UHCPlayer::isPlaying).toList();
    }

    public List<UHCPlayer> getPlayingOnlinePlayers() {
        return playerList.stream().filter(UHCPlayer::isOnline).filter(UHCPlayer::isPlaying).toList();
    }

    public List<Player> getPlayingOnlinePlayersAsPlayers() {
        return playerList.stream().filter(UHCPlayer::isOnline).filter(UHCPlayer::isPlaying).map(UHCPlayer::getPlayer).filter(Objects::nonNull).toList();
    }

    public void removeAllDisplays() {
        for (UHCPlayer uhcPlayer : getOnlinePlayers()) {
            TextDisplay display = uhcPlayer.getNametag();
            if (display != null && display.isValid()) {
                display.remove();
                uhcPlayer.setNametag(null);
            }
        }
    }

    // These methods are necessary since Paper doesn't support cross-world teleportation with passengers, if you use
    // the default teleport() or teleportAsync() then the teleportation will not work.

    public void teleport(Player player, Location location) {
        UHCPlayer uhcPlayer = getUHCPlayer(player);
        TextDisplay nametag = uhcPlayer.getNametag();

        boolean crossWorld = nametag != null && !player.getWorld().equals(location.getWorld());

        if (crossWorld) {
            player.eject();
            player.teleport(location);
        } else {
            player.teleport(location);
        }
    }

    public CompletableFuture<Boolean> teleportAsync(Player player, Location location) {
        UHCPlayer uhcPlayer = getUHCPlayer(player);
        TextDisplay nametag = uhcPlayer.getNametag();

        boolean crossWorld = nametag != null && !player.getWorld().equals(location.getWorld());

        if (crossWorld) {
            player.eject();
            return player.teleportAsync(location).thenApply(ok -> {
                if (ok) Bukkit.getScheduler().runTask(plugin, () -> plugin.info.nametag.ensureDisplay(player));
                return ok;
            });
        } else {
            return player.teleportAsync(location);
        }
    }
}
