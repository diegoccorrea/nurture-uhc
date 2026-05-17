package us.polarismc.polarisuhc.scenarios;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Location;
import us.polarismc.polarisuhc.managers.scenario.BaseScenario;
import us.polarismc.polarisuhc.managers.scenario.Scenario;
import us.polarismc.polarisuhc.managers.scenario.ScenarioConfig;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Scenario(name = "CutClean", author = "putindeer", icon = Material.IRON_INGOT,
        description = "Ores are auto-smelted and animals drop cooked meat.",
        inDevelopment = true)
public abstract class CutClean extends BaseScenario {

    private final Map<Material, OreBlock.OreFamily> oreFamilyMap = new EnumMap<>(Material.class);

    protected int multiplier = 1;

    protected abstract int getMultiplier();

    protected boolean isSmelting() {
        return getMultiplier() == 1;
    }

    @Override
    protected void loadDefaults(ScenarioConfig config) {
        oreFamilyMap.clear();
        for (OreBlock.OreFamily family : OreBlock.OreFamily.values()) {
            if (family == OreBlock.OreFamily.NONE) continue;
            boolean enabled = config.getOrDefault("ore." + family.name().toLowerCase(), true,
                    "Enable processing for " + family.name().toLowerCase() + " ore");
            if (enabled) {
                for (Material ore : family.variants()) {
                    oreFamilyMap.put(ore, family);
                }
            }
        }
    }

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

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Material blockType = event.getBlock().getType();
        OreBlock.OreFamily family = oreFamilyMap.get(blockType);
        if (family == null) {
            return;
        }

        ItemStack drop = getProcessedOutput(blockType, family);
        if (drop == null) {
            return;
        }

        event.setDropItems(false);
        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation().add(.5, .5, .5), drop);

        if (!hasSilkTouch(event.getPlayer())) {
            spawnOreXP(event.getBlock().getLocation(), getSmeltingXP(blockType, family));
        }
    }

    private ItemStack getProcessedOutput(Material ore, OreBlock.OreFamily family) {
        if (family == OreBlock.OreFamily.NONE) {
            return null;
        }
        int amount = getMultiplier();
        // Lapis and redstone have variable drops
        if (family == OreBlock.OreFamily.LAPIS) {
            return new ItemStack(Material.LAPIS_LAZULI, ThreadLocalRandom.current().nextInt(4, 9) * amount);
        }
        if (family == OreBlock.OreFamily.REDSTONE) {
            return new ItemStack(Material.REDSTONE, amount);
        }
        ItemStack drop = new ItemStack(family.output());
        drop.setAmount(amount);
        return drop;
    }

    private int getSmeltingXP(Material ore, OreBlock.OreFamily family) {
        // Gold-based and gem-based ores give 2 XP; others give 1, multiplied
        int base = switch (family) {
            case GOLD, DIAMOND, EMERALD, NETHERITE -> 2;
            default -> 1;
        };
        return base * getMultiplier();
    }

    private boolean hasSilkTouch(Player player) {
        ItemStack tool = player.getInventory().getItemInMainHand();
        return tool.hasItemMeta() && tool.getItemMeta().hasEnchant(Enchantment.SILK_TOUCH);
    }

    private void spawnOreXP(Location loc, int amount) {
        loc.getWorld().spawn(loc, ExperienceOrb.class, orb -> orb.setExperience(amount));
    }
}