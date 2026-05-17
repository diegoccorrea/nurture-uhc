package us.polarismc.polarisuhc.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.config.rates.RatesManager;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class RatesListener implements Listener {

    private static final Set<Material> LEAF_TYPES = Set.of(
        Material.OAK_LEAVES, Material.BIRCH_LEAVES, Material.SPRUCE_LEAVES,
        Material.JUNGLE_LEAVES, Material.ACACIA_LEAVES, Material.DARK_OAK_LEAVES,
        Material.MANGROVE_LEAVES, Material.CHERRY_LEAVES, Material.AZALEA_LEAVES,
        Material.FLOWERING_AZALEA_LEAVES
    );

    private final Main plugin;

    public RatesListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private RatesManager getRates() {
        return plugin.uhc.getRates();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (player == null) return;

        Material type = event.getBlock().getType();

        if (LEAF_TYPES.contains(type)) {
            if (ThreadLocalRandom.current().nextInt(100) < getRates().getAppleRate()) {
                event.getBlock().getWorld().dropItemNaturally(
                    event.getBlock().getLocation(),
                    new ItemStack(Material.APPLE)
                );
            }
            return;
        }

        if (type == Material.GRAVEL) {
            if (ThreadLocalRandom.current().nextInt(100) < getRates().getFlintRate()) {
                event.getBlock().getWorld().dropItemNaturally(
                    event.getBlock().getLocation(),
                    new ItemStack(Material.FLINT)
                );
            }
            return;
        }

        if (type == Material.SAND) {
            if (ThreadLocalRandom.current().nextInt(100) < getRates().getGlassRate()) {
                event.getBlock().getWorld().dropItemNaturally(
                    event.getBlock().getLocation(),
                    new ItemStack(Material.GLASS)
                );
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Player victim)) return;

        Player killer = victim.getKiller();
        if (killer == null) return;

        int bonusXP = getRates().getXpKillRate();
        if (bonusXP > 0) {
            event.setDroppedExp(event.getDroppedExp() + bonusXP);
        }
    }
}