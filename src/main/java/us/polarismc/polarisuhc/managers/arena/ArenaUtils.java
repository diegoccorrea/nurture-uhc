package us.polarismc.polarisuhc.managers.arena;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import us.polarismc.polarisuhc.Main;

import java.util.Random;

public class ArenaUtils {

    // Placeholder for future utility methods related to arenas

    private static final Main plugin = Main.getInstance();

    /**
     * Safely teleports a player to a target location, ensuring they land on a solid block.
     *
     * @param player The player to teleport.
     * @param targetLocation The desired location.
     */
    public static void teleportPlayerSafely(Player player, Location targetLocation) {
        if (targetLocation.getWorld() == null) {
            plugin.utils.message(player, "<red>El mundo de destino no existe.</red>");
            return;
        }

        // Adjust Y level to find a safe landing spot
        int y = targetLocation.getBlockY();
        while (y > 0 && (targetLocation.getWorld().getBlockAt(targetLocation.getBlockX(), y, targetLocation.getBlockZ()).getType().isAir() || targetLocation.getWorld().getBlockAt(targetLocation.getBlockX(), y, targetLocation.getBlockZ()).isLiquid())) {
            y--;
        }

        // If we found a solid block below, place the player on top of it
        // to prevent player from falling into the block or void.
        if (y > 0 && !targetLocation.getWorld().getBlockAt(targetLocation.getBlockX(), y - 1, targetLocation.getBlockZ()).getType().isAir() && !targetLocation.getWorld().getBlockAt(targetLocation.getBlockX(), y - 1, targetLocation.getBlockZ()).isLiquid()) {
            targetLocation.setY(y);
        } else {
            // Fallback to a default safe height if no solid block is found below
            // This might happen if the target area is a large body of water or the void.
            // You might want to re-evaluate targetLocation or use a predefined safe spawn.
            plugin.utils.message(player, "<yellow>No se pudo encontrar un bloque sólido debajo. Teletransportando a altura predeterminada.</yellow>");
            targetLocation.setY(64); // Default safe height
        }
        
        player.teleport(targetLocation);
    }

    /**
     * Generates a random spawn location within a specified radius around a center point.
     * @param center The center location.
     * @param radius The radius around the center.
     * @return A random Location within the radius.
     */
    public static Location getRandomLocation(Location center, double radius) {
        Random random = new Random();
        double xOffset = (random.nextDouble() - 0.5) * 2 * radius;
        double zOffset = (random.nextDouble() - 0.5) * 2 * radius;
        return center.clone().add(xOffset, 0, zOffset);
    }

    /**
     * Creates a player's basic PvP kit.
     * @param player The player to receive the kit.
     */
    public static void giveBasicPvpKit(Player player) {
        final PlayerInventory inv = player.getInventory();
        inv.clear();

        // Default Arena Kit (Polaris)
        inv.setArmorContents(
                new ItemStack[]{
                        plugin.utils.ib(Material.DIAMOND_HELMET)
//                                .enchant(Enchantment.PROTECTION, 1)
                                .build(),
                        plugin.utils.ib(Material.IRON_CHESTPLATE)
                                .build(),
                        plugin.utils.ib(Material.DIAMOND_LEGGINGS)
                                .build(),
                        plugin.utils.ib(Material.IRON_BOOTS)
                                .build()
                }
        );

        // TODO: Completar el inventario de arena.
        inv.addItem(
                plugin.utils.ib(Material.DIAMOND_SWORD)
                        .build(),
                plugin.utils.ib(Material.IRON_AXE)
                        .build(),

                new ItemStack(Material.WATER_BUCKET),
                new ItemStack(Material.LAVA_BUCKET),
                new ItemStack(Material.COBWEB, 4),
                new ItemStack(Material.GOLDEN_APPLE, 2),
                plugin.utils.ib(Material.CROSSBOW)
                        .enchant(Enchantment.QUICK_CHARGE, 1)
                        .build(),
                plugin.utils.ib(Material.TRIDENT)
                        .enchant(Enchantment.LOYALTY, 1)
                        .build()
        );

        player.updateInventory();
    }

    /**
     * Removes the PvP kit from a player's inventory.
     * In a real scenario, you might want to restore their previous inventory.
     * @param player The player to remove the kit from.
     */
    public static void removePvpKit(Player player) {
        player.getInventory().clear();
        player.updateInventory();
        // TODO: Restore hub items or previous inventory.
    }
}
