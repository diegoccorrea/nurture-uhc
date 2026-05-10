package us.polarismc.polarisuhc.scenarios;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import us.polarismc.polarisuhc.managers.scenario.BaseScenario;
import us.polarismc.polarisuhc.managers.scenario.Scenario;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Scenario(name = "Timber", author = "volcqnn", icon = Material.OAK_LOG,
        description = "Breaking a log block causes all blocks above it to break too.")
public class Timber extends BaseScenario {
    private final Set<UUID> processing = new HashSet<>();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.getBlock().getType().name().endsWith("_LOG")) return;

        UUID uuid = event.getPlayer().getUniqueId();
        if (processing.contains(uuid)) return;

        processing.add(uuid);
        int[] count = {0};

        breakChain(event.getBlock().getLocation(), event.getPlayer(), count);
        processing.remove(uuid);
    }

    private void breakChain(Location loc, Player player, int[] count) {
        int CAP = 30;
        if (count[0] >= CAP) return;

        Location blockLoc = loc.clone().add(0, 1, 0);
        Block block = blockLoc.getBlock();
        if (block.getType() == Material.AIR) return;

        boolean isLog = block.getType().name().endsWith("_LOG");

        if (!isLog) return;

        count[0]++;

        plugin.utils.delay(() -> {
            block.breakNaturally();
            block.getWorld().playSound(block.getLocation(), Sound.BLOCK_WOOD_BREAK, 1.0f, 1.0f);
            breakChain(block.getLocation(), player, count);
        });
    }
}