package us.polarismc.polarisuhc.scenarios;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import us.polarismc.polarisuhc.managers.scenario.BaseScenario;

import java.util.EnumMap;
import java.util.Map;

/**
 * Abstract base for ore multiplier scenarios (DoubleOres, TripleOres, etc.).
 * Handles multiplying ore block drops without smelting.
 */
public abstract class OreMultiplier extends BaseScenario {

    protected final Map<Material, OreBlock.OreFamily> oreFamilyMap = new EnumMap<>(Material.class);

    /**
     * Returns the multiplier amount for ore drops (e.g., 2 for DoubleOres, 3 for TripleOres).
     */
    protected abstract int getMultiplier();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Material blockType = event.getBlock().getType();
        OreBlock.OreFamily family = oreFamilyMap.get(blockType);
        if (family == null || family == OreBlock.OreFamily.NONE) {
            return;
        }

        if (hasSilkTouch(event.getPlayer())) {
            return;
        }

        ItemStack drop = new ItemStack(family.output());
        drop.setAmount(getMultiplier());
        event.setDropItems(false);
        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation().add(.5, .5, .5), drop);
    }

    protected boolean hasSilkTouch(Player player) {
        ItemStack tool = player.getInventory().getItemInMainHand();
        return tool.hasItemMeta() && tool.getItemMeta().hasEnchant(Enchantment.SILK_TOUCH);
    }
}