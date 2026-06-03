package us.polarismc.polarisuhc.scenarios;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import us.polarismc.polarisuhc.managers.scenario.BaseScenario;
import us.polarismc.polarisuhc.managers.scenario.Scenario;

@Scenario(
        name = "Blood Anvil",
        description = {
                "Anvils will drop a heart when they break.",
                "The heart can be used to repair items in an anvil."
        },
        author = "volcqnn",
        icon = Material.ANVIL
)
public class BloodAnvil extends BaseScenario {

    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.isCancelled()) return;
        if (!(e.getWhoClicked() instanceof Player p)) return;
        if (!(e.getInventory() instanceof AnvilInventory)) return;

        final InventoryView view = e.getView();
        int rawSlot = e.getRawSlot();

        if (rawSlot == view.convertSlot(rawSlot)) {
            if (rawSlot == 2) {
                ItemStack item = e.getCurrentItem();
                if (item == null || item.getType() == Material.AIR) return;

                plugin.utils.damagePlayer(p, 1);
            }
        }
    }

}
