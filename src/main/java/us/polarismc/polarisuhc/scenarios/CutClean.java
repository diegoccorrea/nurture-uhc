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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Scenario(name = "CutClean", author = "putindeer", icon = Material.IRON_INGOT,
        description = "Ores are auto-smelted and animals drop cooked meat.",
        inDevelopment = true)
public class CutClean extends BaseScenario {
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
        ItemStack drop = getSmeltedOutput(blockType);
        if (drop == null) {
            return;
        }

        event.setDropItems(false);
        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation().add(.5, .5, .5) , drop);

        if (!hasSilkTouch(event.getPlayer())) {
            spawnOreXP(event.getBlock().getLocation(), getSmeltingXP(blockType));
        }
    }

    private ItemStack getSmeltedOutput(Material ore) {
        return switch (ore) {
            // Iron family
            case IRON_ORE, DEEPSLATE_IRON_ORE -> new ItemStack(Material.IRON_INGOT);
            // Gold family
            case GOLD_ORE, DEEPSLATE_GOLD_ORE, NETHER_GOLD_ORE -> new ItemStack(Material.GOLD_INGOT);
            // Copper family
            case COPPER_ORE, DEEPSLATE_COPPER_ORE -> new ItemStack(Material.COPPER_INGOT);
            // Gems - give XP when smelted, output the gem itself
            case DIAMOND_ORE, DEEPSLATE_DIAMOND_ORE -> new ItemStack(Material.DIAMOND);
            case EMERALD_ORE, DEEPSLATE_EMERALD_ORE -> new ItemStack(Material.EMERALD);
            // Other smeltable ores
            case LAPIS_ORE, DEEPSLATE_LAPIS_ORE -> new ItemStack(Material.LAPIS_LAZULI, ThreadLocalRandom.current().nextInt(4, 9));
            case REDSTONE_ORE, DEEPSLATE_REDSTONE_ORE -> new ItemStack(Material.REDSTONE);
            case NETHER_QUARTZ_ORE -> new ItemStack(Material.QUARTZ);
            case ANCIENT_DEBRIS -> new ItemStack(Material.NETHERITE_SCRAP);
            default -> null;
        };
    }

    private int getSmeltingXP(Material ore) {
        return switch (ore) {
            // Gold-based and gem-based ores give 1.0 XP
            case GOLD_ORE, DEEPSLATE_GOLD_ORE, NETHER_GOLD_ORE,
                 DIAMOND_ORE, DEEPSLATE_DIAMOND_ORE, EMERALD_ORE, DEEPSLATE_EMERALD_ORE,
                 ANCIENT_DEBRIS -> 2;
            // Iron, copper, lapis, redstone, quartz give 0.7 XP (rounds to 1)
            default -> 1;
        };
    }

    private boolean hasSilkTouch(Player player) {
        ItemStack tool = player.getInventory().getItemInMainHand();
        return tool.hasItemMeta() && tool.getItemMeta().hasEnchant(Enchantment.SILK_TOUCH);
    }

    private void spawnOreXP(Location loc, int amount) {
        loc.getWorld().spawn(loc, ExperienceOrb.class, orb -> orb.setExperience(amount));
    }
}