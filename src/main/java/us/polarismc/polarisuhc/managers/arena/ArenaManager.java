
package us.polarismc.polarisuhc.managers.arena;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import us.polarismc.polarisuhc.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("FieldCanBeLocal")
@Getter
@Setter
public class ArenaManager {
    private final Main plugin;
    private boolean enabled = false;
    private final List<Player> playersInArena = new ArrayList<>();
    private Location arenaSpawnLocation;
    private Location worldSpawnLocation;

    public ArenaManager(Main plugin) {
        this.plugin = plugin;
        // Initialize spawn locations - you might want to load these from a config file
        // ArenaUtils.getRandomLocation will be used to determine the exact spawn point within the radius
        this.arenaSpawnLocation = plugin.uhc.world.getArenaWorld() != null ? Objects.requireNonNull(Bukkit.getWorld("arena")).getSpawnLocation()
                : new Location(Bukkit.getWorlds().getFirst(), 0, 100, 0); // Default arena spawn
        this.worldSpawnLocation = plugin.uhc.world.getLobbyWorld() != null ? Objects.requireNonNull(Bukkit.getWorld("world")).getSpawnLocation()
                : new Location(Bukkit.getWorlds().getFirst(), 0, 100, 0); // Default world spawn
    }

    @SuppressWarnings("UnChecked")
    public void joinArena(Player player) {
        if (!enabled) {
            plugin.utils.message(player, "<red>La arena no está habilitada actualmente.</red>");
            return;
        }

        if (playersInArena.contains(player)) {
            plugin.utils.message(player, "<yellow>Ya estás en la arena.</yellow>");
            return;
        }

        playersInArena.add(player);
        ArenaUtils.giveBasicPvpKit(player);
        // Use ArenaUtils for teleportation
        Location targetLocation = ArenaUtils.getRandomLocation(arenaSpawnLocation, 20); // 20 blocks radius for 40x40 area
        ArenaUtils.teleportPlayerSafely(player, targetLocation);
    }

    public void leaveArena(Player player) {
        if (!playersInArena.contains(player)) {
            return; // Player is not in the arena
        }

        playersInArena.remove(player);
        ArenaUtils.removePvpKit(player);
        if (worldSpawnLocation != null) {
            player.teleport(worldSpawnLocation);
        } else {
            plugin.utils.message(player, "<red>El spawn del mundo principal no está configurado.</red>");
        }
    }

    private static final String ARENA_TOGGLEMESSAGE = "<white>Arena: {state}";

    // Method to handle player quitting while in arena
    public void handlePlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (playersInArena.contains(player)) {
            leaveArena(player);
        }
    }

    public void toggleArena() {
        enabled = !enabled;
        String state = enabled ? "<green>Open" : "<red>Closed";
        String message = ARENA_TOGGLEMESSAGE.replace("{state}", state);

        World arenaWorld = plugin.uhc.world.getArenaWorld();
        // Teleport all players currently in the arena world to the main world spawn
        if (arenaWorld != null) {
            for (Player player : arenaWorld.getPlayers()) {
                if (playersInArena.contains(player)) { // Ensure we only teleport players registered in our list
                    teleportPlayerToWorldSpawn(player);
                }
            }
        }

        // Send message and play sound to all players online
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(message);
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
        }
    }

    private void teleportPlayerToArena(Player player) {
        World arenaWorld = plugin.uhc.world.getArenaWorld();

        // Teleport to a random location within a 40x40 radius of the spawn
        if (arenaWorld != null) {
            ThreadLocalRandom random = ThreadLocalRandom.current();
            double xOffset = (random.nextDouble() - 0.5) * 40;
            double zOffset = (random.nextDouble() - 0.5) * 40;
            final Location targetLocation = arenaSpawnLocation.clone().add(xOffset, 0, zOffset);

            // Ensure the location is safe (on ground and not in water/lava, etc.)
            targetLocation.setY(getSafeY(targetLocation));
            
            player.teleport(targetLocation);
        } else {
            plugin.utils.message(player, "<red>El mundo de la arena no existe.</red>");
        }
    }

    private void teleportPlayerToWorldSpawn(Player player) {
        if (worldSpawnLocation != null) {
            player.teleport(worldSpawnLocation);
        } else {
            plugin.utils.message(player, "<red>El spawn del mundo principal no está configurado.</red>");
        }
    }

    private int getSafeY(Location location) {
        int y = location.getBlockY();
        while (y > 0 && (location.getWorld().getBlockAt(location.getBlockX(), y, location.getBlockZ()).getType().isAir() || location.getWorld().getBlockAt(location.getBlockX(), y, location.getBlockZ()).isLiquid())) {
            y--;
        }
        // If we found a solid block below, place the player on top of it
        if (y > 0 && !location.getWorld().getBlockAt(location.getBlockX(), y - 1, location.getBlockZ()).getType().isAir() && !location.getWorld().getBlockAt(location.getBlockX(), y - 1, location.getBlockZ()).isLiquid()) {
            return y;
        }
        // Fallback to a default safe height if no solid block is found below
        return 64; // Default safe height
    }

    public void cleanup() {
        if (enabled) {
            toggleArena();
        }
        playersInArena.clear();
    }
}
