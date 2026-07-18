package us.polarismc.polarisuhc.scenarios;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import us.polarismc.polarisuhc.managers.scenario.BaseScenario;
import us.polarismc.polarisuhc.managers.scenario.Scenario;

@Scenario(name = "SwordLess", author = "volcqnn", icon = Material.DIAMOND_SWORD,
        description = "You cannot use swords.")
public class SwordLess extends BaseScenario {
    @EventHandler
    public void onPlayerInteract(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player && isSword(player.getInventory().getItemInMainHand().getType())) {
            event.setCancelled(true);
            plugin.utils.actionBar(player, "<red>Swords are disabled!");
            plugin.info.bar.sendTemporary(player, "<red>Swords are disabled!");
        }
    }

    @EventHandler
    public void onCraftSword(CraftItemEvent event) {
        if (event.getCurrentItem() != null && isSword(event.getCurrentItem().getType())) {
            event.setCancelled(true);
        }
    }

    private boolean isSword(Material type) {
        return type == Material.WOODEN_SWORD ||
                type == Material.COPPER_SWORD ||
                type == Material.STONE_SWORD ||
                type == Material.IRON_SWORD ||
                type == Material.GOLDEN_SWORD ||
                type == Material.DIAMOND_SWORD ||
                type == Material.NETHERITE_SWORD;
    }
}