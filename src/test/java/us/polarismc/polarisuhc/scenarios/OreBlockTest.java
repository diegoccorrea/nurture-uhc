package us.polarismc.polarisuhc.scenarios;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import static org.junit.jupiter.api.Assertions.*;

class OreBlockTest {

    private ServerMock server;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    void getFamily_ironOre_returnsIronFamily() {
        OreBlock.OreFamily family = OreBlock.getFamily(org.bukkit.Material.IRON_ORE);
        assertEquals(OreBlock.OreFamily.IRON, family);
    }

    @Test
    void getFamily_deepslateIronOre_returnsIronFamily() {
        OreBlock.OreFamily family = OreBlock.getFamily(org.bukkit.Material.DEEPSLATE_IRON_ORE);
        assertEquals(OreBlock.OreFamily.IRON, family);
    }

    @Test
    void getFamily_goldOre_returnsGoldFamily() {
        OreBlock.OreFamily family = OreBlock.getFamily(org.bukkit.Material.GOLD_ORE);
        assertEquals(OreBlock.OreFamily.GOLD, family);
    }

    @Test
    void getFamily_netherGoldOre_returnsGoldFamily() {
        OreBlock.OreFamily family = OreBlock.getFamily(org.bukkit.Material.NETHER_GOLD_ORE);
        assertEquals(OreBlock.OreFamily.GOLD, family);
    }

    @Test
    void getFamily_copperOre_returnsCopperFamily() {
        OreBlock.OreFamily family = OreBlock.getFamily(org.bukkit.Material.COPPER_ORE);
        assertEquals(OreBlock.OreFamily.COPPER, family);
    }

    @Test
    void getFamily_diamondOre_returnsDiamondFamily() {
        OreBlock.OreFamily family = OreBlock.getFamily(org.bukkit.Material.DIAMOND_ORE);
        assertEquals(OreBlock.OreFamily.DIAMOND, family);
    }

    @Test
    void getFamily_emeraldOre_returnsEmeraldFamily() {
        OreBlock.OreFamily family = OreBlock.getFamily(org.bukkit.Material.EMERALD_ORE);
        assertEquals(OreBlock.OreFamily.EMERALD, family);
    }

    @Test
    void getFamily_lapisOre_returnsLapisFamily() {
        OreBlock.OreFamily family = OreBlock.getFamily(org.bukkit.Material.LAPIS_ORE);
        assertEquals(OreBlock.OreFamily.LAPIS, family);
    }

    @Test
    void getFamily_redstoneOre_returnsRedstoneFamily() {
        OreBlock.OreFamily family = OreBlock.getFamily(org.bukkit.Material.REDSTONE_ORE);
        assertEquals(OreBlock.OreFamily.REDSTONE, family);
    }

    @Test
    void getFamily_netherQuartzOre_returnsQuartzFamily() {
        OreBlock.OreFamily family = OreBlock.getFamily(org.bukkit.Material.NETHER_QUARTZ_ORE);
        assertEquals(OreBlock.OreFamily.QUARTZ, family);
    }

    @Test
    void getFamily_ancientDebris_returnsNetheriteFamily() {
        OreBlock.OreFamily family = OreBlock.getFamily(org.bukkit.Material.ANCIENT_DEBRIS);
        assertEquals(OreBlock.OreFamily.NETHERITE, family);
    }

    @Test
    void getFamily_nonOreBlock_returnsNone() {
        OreBlock.OreFamily family = OreBlock.getFamily(org.bukkit.Material.STONE);
        assertEquals(OreBlock.OreFamily.NONE, family);
    }

    @Test
    void isOre_ironOre_returnsTrue() {
        assertTrue(OreBlock.isOre(org.bukkit.Material.IRON_ORE));
    }

    @Test
    void isOre_stone_returnsFalse() {
        assertFalse(OreBlock.isOre(org.bukkit.Material.STONE));
    }

    @Test
    void oreFamily_output_iron_returnsIronIngot() {
        assertEquals(org.bukkit.Material.IRON_INGOT, OreBlock.OreFamily.IRON.output());
    }

    @Test
    void oreFamily_output_gold_returnsGoldIngot() {
        assertEquals(org.bukkit.Material.GOLD_INGOT, OreBlock.OreFamily.GOLD.output());
    }

    @Test
    void oreFamily_output_diamond_returnsDiamond() {
        assertEquals(org.bukkit.Material.DIAMOND, OreBlock.OreFamily.DIAMOND.output());
    }

    @Test
    void oreFamily_variants_iron_hasTwoVariants() {
        assertEquals(2, OreBlock.OreFamily.IRON.variants().size());
    }

    @Test
    void oreFamily_variants_gold_hasThreeVariants() {
        assertEquals(3, OreBlock.OreFamily.GOLD.variants().size());
    }

    @Test
    void oreFamily_variants_none_isEmpty() {
        assertTrue(OreBlock.OreFamily.NONE.variants().isEmpty());
    }
}