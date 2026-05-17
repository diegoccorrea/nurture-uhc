package us.polarismc.polarisuhc.scenarios;

import org.bukkit.Material;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

/**
 * Utility class for ore block detection and family lookup.
 * Provides shared ore detection for CutClean and multiplier scenarios.
 */
public final class OreBlock {

    private OreBlock() {}

    private static final Map<Material, OreFamily> ORE_MAP = new EnumMap<>(Material.class);
    private static final Set<Material> EMPTY_SET = Collections.emptySet();

    static {
        registerFamily(OreFamily.IRON, Material.IRON_ORE, Material.DEEPSLATE_IRON_ORE);
        registerFamily(OreFamily.GOLD, Material.GOLD_ORE, Material.DEEPSLATE_GOLD_ORE, Material.NETHER_GOLD_ORE);
        registerFamily(OreFamily.COPPER, Material.COPPER_ORE, Material.DEEPSLATE_COPPER_ORE);
        registerFamily(OreFamily.DIAMOND, Material.DIAMOND_ORE, Material.DEEPSLATE_DIAMOND_ORE);
        registerFamily(OreFamily.EMERALD, Material.EMERALD_ORE, Material.DEEPSLATE_EMERALD_ORE);
        registerFamily(OreFamily.LAPIS, Material.LAPIS_ORE, Material.DEEPSLATE_LAPIS_ORE);
        registerFamily(OreFamily.REDSTONE, Material.REDSTONE_ORE, Material.DEEPSLATE_REDSTONE_ORE);
        registerFamily(OreFamily.QUARTZ, Material.NETHER_QUARTZ_ORE);
        registerFamily(OreFamily.NETHERITE, Material.ANCIENT_DEBRIS);
        OreFamily.NONE.setVariants(EMPTY_SET);
    }

    private static void registerFamily(OreFamily family, Material... variants) {
        for (Material m : variants) {
            ORE_MAP.put(m, family);
        }
        family.setVariants(Set.of(variants));
    }

    /**
     * Returns the OreFamily for the given material, or OreFamily.NONE if not an ore.
     */
    public static OreFamily getFamily(Material material) {
        OreFamily family = ORE_MAP.get(material);
        return family != null ? family : OreFamily.NONE;
    }

    /**
     * Returns true if the material is a registered ore block.
     */
    public static boolean isOre(Material material) {
        return ORE_MAP.containsKey(material);
    }

    /**
     * Represents a family of ore blocks that share the same output.
     */
    public enum OreFamily {
        IRON(Material.IRON_INGOT),
        GOLD(Material.GOLD_INGOT),
        COPPER(Material.COPPER_INGOT),
        DIAMOND(Material.DIAMOND),
        EMERALD(Material.EMERALD),
        LAPIS(Material.LAPIS_LAZULI),
        REDSTONE(Material.REDSTONE),
        QUARTZ(Material.QUARTZ),
        NETHERITE(Material.NETHERITE_SCRAP),
        NONE(null);

        private final Material output;
        private Set<Material> variants;

        OreFamily(Material output) {
            this.output = output;
        }

        void setVariants(Set<Material> variants) {
            this.variants = variants;
        }

        public Material output() {
            return output;
        }

        public Set<Material> variants() {
            return variants;
        }
    }
}