package us.polarismc.polarisuhc.scenarios;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import us.polarismc.polarisuhc.managers.scenario.*;
import us.polarismc.polarisuhc.managers.uhc.UHCState;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.bukkit.Material.AIR;
import static org.bukkit.Material.CHEST;

@Scenario(
        name = "SafeLoot",
        description = {
                "Upon killing a player a chest containing their loot will spawn and can only be opened by the killer."
        },
        icon = Material.BARREL,
        properties = ScenarioProperty.IN_DEVELOPMENT,
        incompatibleWith = {
                ScenarioType.TIME_BOMB,
                ScenarioType.GRAVE_ROBBERS,
        }
)
public class SafeLoot extends BaseScenario {

    private final Map<Location, UUID> safeLoot = new HashMap<>();
    //FIXME - you can rob items with a hopper in safeloot

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;
        if (!event.getAction().isRightClick()) return;
        Player p = event.getPlayer();
        if (p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR) {
            return;
        }

        Block b = event.getClickedBlock();
        if (b.getType() != CHEST) return;
        if (safeLoot.isEmpty()) return;
        if (!safeLoot.containsKey(b.getLocation())) return;

        UUID id = safeLoot.get(b.getLocation());
        if (id == null) return;

        if (p.getUniqueId().toString().equals(id.toString())) return;

        event.setCancelled(true);
        plugin.utils.message(p, "<red>This loot doesn't belong to you.");
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player p = event.getPlayer();
        Block b = event.getBlock();
        if (p.getGameMode() == GameMode.CREATIVE) {
            return;
        }
        if (b.getType() != CHEST) return;
        if (safeLoot.isEmpty()) return;
        if (!safeLoot.containsKey(b.getLocation())) return;

        UUID id = safeLoot.get(b.getLocation());
        if (id == null) return;

        if (p.getUniqueId().toString().equals(id.toString())) return;

        event.setCancelled(true);
        plugin.utils.message(p, "<red>This loot doesn't belong to you.");
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onDeath(PlayerDeathEvent event) {
        if (plugin.uhc.getState() == UHCState.IDLE || plugin.uhc.getState() == UHCState.PRESTARTED) return;

        final ScenarioManager scenarioManager = plugin.scen;
        //if (scen.getScenario("NuclearBomb").isEnabled()) return;
        final Player p = event.getEntity();
        final Location loc = p.getLocation().clone();
        if (Math.signum(event.getEntity().getLocation().getBlockY()) == -1.0 || Math.signum(event.getEntity().getLocation().getBlockY()) == 0.0) {
            return;
        }

        final Block leftSide = loc.getBlock();
        final Block rightSide = loc.clone().add(-1, 0, 0).getBlock();

        leftSide.setType(Material.CHEST);
        rightSide.setType(Material.CHEST);

        BlockData leftData = leftSide.getBlockData();
        ((Directional) leftData).setFacing(BlockFace.NORTH);
        leftSide.setBlockData(leftData);

        org.bukkit.block.data.type.Chest chestDataLeft = (org.bukkit.block.data.type.Chest) leftData;
        chestDataLeft.setType(org.bukkit.block.data.type.Chest.Type.RIGHT);
        leftSide.setBlockData(chestDataLeft);

        Chest chestLeft = (Chest) leftSide.getState();

        BlockData rightData = rightSide.getBlockData();
        ((Directional) rightData).setFacing(BlockFace.NORTH);
        rightSide.setBlockData(rightData);

        org.bukkit.block.data.type.Chest chestDataRight = (org.bukkit.block.data.type.Chest) rightData;
        chestDataRight.setType(org.bukkit.block.data.type.Chest.Type.LEFT);
        rightSide.setBlockData(chestDataRight);

        Chest chestRight = (Chest) rightSide.getState();

        for (int i = 0; i < event.getDrops().size(); i++) {
            ItemStack item = event.getDrops().get(i);
            if (item == null || item.getType() == AIR) {
                continue;
            }
            if (i < 27) {
                chestLeft.getInventory().addItem(item);
            } else chestRight.getInventory().addItem(item);
        }

        event.getDrops().clear();

        // SafeLoot code
        Player k = p.getKiller();
        if (k == null) return;

        final ArmorStand stand = loc.getWorld().spawn(chestLeft.getLocation().clone().add(0, 1, 0), ArmorStand.class);
        stand.customName(
                plugin.utils.chat("This loot belongs to" + k.getName())
        );
        stand.setCustomNameVisible(true);
        stand.setSmall(true);
        stand.setGravity(false);
        stand.setVisible(false);
        stand.setMarker(true);

        safeLoot.put(leftSide.getLocation(), k.getUniqueId());
        safeLoot.put(rightSide.getLocation(), k.getUniqueId());
    }

}
