package us.polarismc.polarisuhc.scenarios;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import us.polarismc.polarisuhc.managers.scenario.BaseScenario;
import us.polarismc.polarisuhc.managers.scenario.Scenario;

import java.util.concurrent.ThreadLocalRandom;

@Scenario(
        name = "GunsNRoses",
        description = {
                "Breaking a poppy will give you 1 arrow.",
                "Breaking a rose bush will give you 4 arrows and maybe a bow."
        },
        author = "volcqnn",
        icon = Material.POPPY
)
public class GunsNRoses extends BaseScenario {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Block b = e.getBlock();
        if (doGunsNRoses(b))
            e.setDropItems(false);
    }

    public static boolean doGunsNRoses(Block b) {
        Material mat = b.getType();
        if (mat.equals(Material.ROSE_BUSH)) {
            b.getWorld().dropItemNaturally(b.getLocation(), new ItemStack(Material.ARROW, 4));
            if (ThreadLocalRandom.current().nextInt(24) == 5) b.getWorld().dropItemNaturally(b.getLocation(), new ItemStack(Material.BOW, 1));
            return true;
        } else if (mat.equals(Material.POPPY)) {
            b.getWorld().dropItemNaturally(b.getLocation(), new ItemStack(Material.ARROW, 1));
            return true;
        }
        return false;
    }

}
