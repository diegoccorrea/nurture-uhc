package us.polarismc.polarisuhc.scenarios;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import us.polarismc.polarisuhc.managers.scenario.BaseScenario;
import us.polarismc.polarisuhc.managers.scenario.Scenario;

import java.util.*;

@Scenario(
        name = "BlockRush",
        description = {
                "The first time you break a block type, it will drop a gold ingot."
        },
        author = "volcqnn",
        icon = Material.GOLD_INGOT
)
public class BlockRush extends BaseScenario {

    private final Map<UUID, List<Material>> blocks = new HashMap<>();

    @EventHandler(ignoreCancelled = true)
    public void onBreak(BlockBreakEvent e) {
        final Player player = e.getPlayer();
        if (!plugin.uhc.isPlaying(player)) return;

        final Block b = e.getBlock();
        final List<Material> blockList = blocks.putIfAbsent(player.getUniqueId(), new ArrayList<>());

        if (blockList != null && !blockList.contains(b.getType())) {
            blocks.get(player.getUniqueId()).add(b.getType());
            b.getWorld().dropItemNaturally(b.getLocation(), new ItemStack(Material.GOLD_INGOT));
        }
    }

}
