package us.polarismc.polarisuhc.scenarios;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Llama;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.LootTables;
import us.polarismc.polarisuhc.managers.scenario.BaseScenario;
import us.polarismc.polarisuhc.managers.scenario.Scenario;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Scenario(
        name = "Loot Llamas",
        description = {
                "When a player kills a llama, a chest with a",
                "random loot table is placed at the llama's death spot."
        },
        author = "volcqnn",
        icon = org.bukkit.Material.CHEST
)
public class LootLlamas extends BaseScenario {

    private final List<LootTables> lootLlamaLootTables = new ArrayList<>();

    @Override
    public void onEnable() {
        addLootTables();
    }

    @Override
    public void onDisable() {
        lootLlamaLootTables.clear();
    }

    @EventHandler
    public void onLlamaDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Llama llama) {
            if (llama.getKiller() != null) {
                generateLootChest(llama.getLocation());
            }
        }
    }

    private void generateLootChest(Location location) {
        final Block chestBlock = location.getBlock();
        chestBlock.setType(Material.CHEST);
        Chest chest = (Chest) chestBlock.getState();

        final LootTables randomLootTable = lootLlamaLootTables.get(
                ThreadLocalRandom.current().nextInt(lootLlamaLootTables.size())
        );

        LootTable lootTable = plugin.getServer().getLootTable(randomLootTable.getKey());
        Inventory chestInventory = chest.getInventory();
        assert lootTable != null;
        chestInventory.clear();
        chest.setLootTable(lootTable);
        chest.update();
    }

    private void addLootTables() {
        lootLlamaLootTables.add(LootTables.ABANDONED_MINESHAFT);
        lootLlamaLootTables.add(LootTables.BASTION_BRIDGE);
        lootLlamaLootTables.add(LootTables.BASTION_HOGLIN_STABLE);
        lootLlamaLootTables.add(LootTables.BASTION_OTHER);
        lootLlamaLootTables.add(LootTables.BASTION_TREASURE);
        lootLlamaLootTables.add(LootTables.BURIED_TREASURE);
        lootLlamaLootTables.add(LootTables.DESERT_PYRAMID);
        lootLlamaLootTables.add(LootTables.END_CITY_TREASURE);
        lootLlamaLootTables.add(LootTables.IGLOO_CHEST);
        lootLlamaLootTables.add(LootTables.JUNGLE_TEMPLE);
        lootLlamaLootTables.add(LootTables.NETHER_BRIDGE);
        lootLlamaLootTables.add(LootTables.PILLAGER_OUTPOST);
        lootLlamaLootTables.add(LootTables.RUINED_PORTAL);
        lootLlamaLootTables.add(LootTables.SHIPWRECK_SUPPLY);
        lootLlamaLootTables.add(LootTables.SHIPWRECK_TREASURE);
        lootLlamaLootTables.add(LootTables.SIMPLE_DUNGEON);
        lootLlamaLootTables.add(LootTables.STRONGHOLD_CORRIDOR);
        lootLlamaLootTables.add(LootTables.STRONGHOLD_CROSSING);
        lootLlamaLootTables.add(LootTables.STRONGHOLD_LIBRARY);
        lootLlamaLootTables.add(LootTables.VILLAGE_ARMORER);
        lootLlamaLootTables.add(LootTables.VILLAGE_CARTOGRAPHER);
        lootLlamaLootTables.add(LootTables.VILLAGE_DESERT_HOUSE);
        lootLlamaLootTables.add(LootTables.VILLAGE_FLETCHER);
        lootLlamaLootTables.add(LootTables.VILLAGE_TEMPLE);
        lootLlamaLootTables.add(LootTables.VILLAGE_TOOLSMITH);
        lootLlamaLootTables.add(LootTables.VILLAGE_WEAPONSMITH);
        lootLlamaLootTables.add(LootTables.WOODLAND_MANSION);
    }

}
