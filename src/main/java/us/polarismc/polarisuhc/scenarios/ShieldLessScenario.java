package us.polarismc.polarisuhc.scenarios;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import us.polarismc.polarisuhc.managers.scenario.BaseScenario;
import us.polarismc.polarisuhc.managers.scenario.Scenario;

@Scenario(name = "ShieldLess", author = "volcqnn", icon = Material.SHIELD,
        description = "You cannot use shields.")
public class ShieldLessScenario extends BaseScenario {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!isEnabled()) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        if (event.getItem() != null && event.getItem().getType() == Material.SHIELD) {
            event.setCancelled(true);
            event.getPlayer().sendActionBar(Component.text("<red>Shields are disabled!"));
        }
    }
}