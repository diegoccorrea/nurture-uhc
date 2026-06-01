package us.polarismc.polarisuhc.scenarios;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import us.polarismc.polarisuhc.managers.scenario.BaseScenario;
import us.polarismc.polarisuhc.managers.scenario.Scenario;

@Scenario(name = "GraveRobbers", author = "volcqnn", icon = Material.MOSSY_COBBLESTONE,
        description = "When a player dies, a grave is created at their death location with a sign showing their name.")
public class GraveRobbers extends BaseScenario {

    // FIXME - esta mal hecha la caja.
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Location deathLoc = event.getEntity().getLocation();
        String playerName = event.getEntity().getName();

        // Chest 2 blocks below
        Location graveBase = deathLoc.clone().subtract(0, 2, 0);
        graveBase.getBlock().setType(Material.CHEST);
        Chest chest = (Chest) graveBase.getBlock().getState();
        for (int i = 0; i < event.getDrops().size(); i++) {
            ItemStack item = event.getDrops().get(i);
            chest.getInventory().setItem(i, item);
        }
        event.getDrops().clear();

        // Gravel on top
        deathLoc.getBlock().setType(Material.GRAVEL);

        // Cobblestone or mossy cobblestone sides
        Location side1 = deathLoc.clone().add(1, 0, 0);
        side1.getBlock().setType(Math.random() > 0.5 ? Material.COBBLESTONE : Material.MOSSY_COBBLESTONE);

        Location side2 = deathLoc.clone().add(-1, 0, 0);
        side2.getBlock().setType(Math.random() > 0.5 ? Material.COBBLESTONE : Material.MOSSY_COBBLESTONE);

        Location side3 = deathLoc.clone().add(0, 0, 1);
        side3.getBlock().setType(Math.random() > 0.5 ? Material.COBBLESTONE : Material.MOSSY_COBBLESTONE);

        Location side4 = deathLoc.clone().add(0, 0, -1);
        side4.getBlock().setType(Math.random() > 0.5 ? Material.COBBLESTONE : Material.MOSSY_COBBLESTONE);

        // Warped wall sign with glowing player name
        Location signLoc = deathLoc.clone().add(0, 1, 0);
        signLoc.getBlock().setType(Material.WARPED_WALL_SIGN);

        Sign sign = (org.bukkit.block.Sign) signLoc.getBlock().getState();
        SignSide side = sign.getSide(Side.FRONT);
        side.line(0, plugin.utils.chat(playerName));
        side.setGlowingText(true);
        sign.update();
    }
}