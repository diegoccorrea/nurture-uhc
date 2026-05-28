package us.polarismc.polarisuhc.scenarios;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import us.polarismc.polarisuhc.managers.scenario.Scenario;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// TODO - Agregar funciones sin CUTLCLEAN
// Por ahora solo es compatible con CutClean.
@Scenario(name = "VeinMiner", author = "volcqnn", icon = Material.GOLDEN_PICKAXE,
        description = "Mining an ore block mines all connected ore blocks of the same type.")
public class VeinMiner extends CutClean {

    @SuppressWarnings("unchecked")
    @EventHandler (ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void blockBreak(BlockBreakEvent event) {
        Material blockType = event.getBlock().getType();
        OreBlock.OreFamily family = oreFamilyMap.get(blockType);
        if (family == null) {
            return;
        }

        ItemStack drop = getProcessedOutput(family);
        if (drop == null) {
            return;
        }

        event.setDropItems(true);
        mineVine(event.getBlock(), family);
    }

    @Override
    protected int getMultiplier() {
        return multiplier; // No se modifica el multiplicador
    }

    private void mineVine(Block b, OreBlock.OreFamily oreFamily) {
        List<Block> connectedBlocks = new ArrayList<>();
        getConnectedBlocks(b, connectedBlocks);

        // TODO - Cuando se agreguen los escenarios de Blood, agregar el daño correspondiente a cada uno
        //switch (b.getType()) {
//            case GOLD_ORE, DEEPSLATE_GOLD_ORE,NETHER_GOLD_ORE,GILDED_BLACKSTONE -> {
//                up.addGold(connectedBlocks.isEmpty() ? 1 : connectedBlocks.size());
//
//                if (plugin.scenManager.getScenario("BloodGold").isEnabled())
//                    Utils.damagePlayer(up.getPlayer(), connectedBlocks.isEmpty() ? 1 : connectedBlocks.size());
//            }
//
//            case LAPIS_ORE,DEEPSLATE_LAPIS_ORE -> {
//                if (plugin.scenManager.getScenario("BloodLapis").isEnabled())
//                    Utils.damagePlayer(up.getPlayer(), connectedBlocks.isEmpty() ? 1 : connectedBlocks.size());
//            }

//            case DIAMOND_ORE, DEEPSLATE_DIAMOND_ORE -> {
//                up.addDiamond(connectedBlocks.isEmpty() ? 1 : connectedBlocks.size());
//
//                if (plugin.scenManager.getScenario("BloodDiamonds").isEnabled())
//                    Utils.damagePlayer(up.getPlayer(), connectedBlocks.isEmpty() ? 1 : connectedBlocks.size());
//            }

//            case ANCIENT_DEBRIS -> up.addScrap(connectedBlocks.isEmpty() ? 1 : connectedBlocks.size()); -> ???
        //}

        if (connectedBlocks.isEmpty()) {
            breakBlock(b, getExperience(oreFamily));
            b.getLocation().getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getBlockData());
            return;
        }

        breakBlock(b, getMultiplier());

        int delay = 0;
        for (Block bl : connectedBlocks) {
            plugin.utils.delay(1 + delay, () -> {
                breakBlock(bl, getExperience(oreFamily));
                bl.getLocation().getWorld().playEffect(bl.getLocation(), Effect.STEP_SOUND, bl.getBlockData());
            });
            delay++;
        }
    }

    private void breakBlock(@NotNull final Block b, int xp) {
        b.setType(Material.AIR);
        b.getState().update();
        b.getWorld().dropItemNaturally(b.getLocation(), Objects.requireNonNull(getProcessedOutput(OreBlock.getFamily(b.getType()))));
        if (xp > 0)
            b.getWorld().spawn(b.getLocation(), ExperienceOrb.class, orb -> orb.setExperience(xp));
    }


    private static final BlockFace[] FACES = {
            BlockFace.NORTH,
            BlockFace.SOUTH,
            BlockFace.EAST,
            BlockFace.WEST,
            BlockFace.UP,
            BlockFace.DOWN
    };

    public void getConnectedBlocks(Block block, List<Block> list) {
        for (BlockFace face : FACES) {
            Block relative = block.getRelative(face);

            if (relative.getType() != block.getType()) {
                continue;
            }

            if (list.contains(relative)) {
                continue;
            }

            list.add(relative);
            getConnectedBlocks(relative, list);
        }
    }
}
