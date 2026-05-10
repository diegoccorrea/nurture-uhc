package us.polarismc.polarisuhc.scenarios;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import us.polarismc.polarisuhc.managers.scenario.BaseScenario;
import us.polarismc.polarisuhc.managers.scenario.Scenario;

import java.util.ArrayList;
import java.util.List;

@Scenario(name = "CutClean", author = "putindeer", icon = Material.IRON_INGOT,
        description = "Ores are auto-smelted and animals drop cooked meat.",
        inDevelopment = true)
public class CutClean extends BaseScenario {
    //TODO - hacer lo de los minerales del cutclean
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        List<ItemStack> drops = new ArrayList<>();
        event.getDrops().forEach(drop -> {
            int amount = drop.getAmount();
            switch (drop.getType()) {
                case PORKCHOP -> drops.add(new ItemStack(Material.COOKED_PORKCHOP, amount));
                case BEEF -> drops.add(new ItemStack(Material.COOKED_BEEF, amount));
                case CHICKEN -> drops.add(new ItemStack(Material.COOKED_CHICKEN, amount));
                case MUTTON -> drops.add(new ItemStack(Material.COOKED_MUTTON, amount));
                case RABBIT -> drops.add(new ItemStack(Material.COOKED_RABBIT, amount));
                case SALMON -> drops.add(new ItemStack(Material.COOKED_SALMON, amount));
                case COD -> drops.add(new ItemStack(Material.COOKED_COD, amount));
                default -> drops.add(drop);
            }
        });

        event.getDrops().clear();
        event.getDrops().addAll(drops);
    }
}