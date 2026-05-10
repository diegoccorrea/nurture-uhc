package us.polarismc.polarisuhc.scenarios;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import us.polarismc.polarisuhc.managers.scenario.BaseScenario;
import us.polarismc.polarisuhc.managers.scenario.Scenario;

@Scenario(name = "GraveRobbers", author = "volcqnn", icon = Material.MOSSY_COBBLESTONE,
        description = "When a player dies, a grave is created at their death location with a sign showing their name.")
public class GraveRobbersScenario extends BaseScenario {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!isEnabled()) return;

        Location deathLoc = event.getEntity().getLocation();
        String playerName = event.getEntity().getName();

        // Chest 2 blocks below
        Location graveBase = deathLoc.clone().subtract(0, 2, 0);
        graveBase.getBlock().setType(Material.CHEST);

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

        org.bukkit.block.Sign sign = (org.bukkit.block.Sign) signLoc.getBlock().getState();
        sign.setLine(0, playerName);
        sign.setGlowingText(true);
    }
}