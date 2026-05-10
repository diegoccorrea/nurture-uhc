package us.polarismc.polarisuhc.scenarios;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import us.polarismc.polarisuhc.managers.scenario.BaseScenario;
import us.polarismc.polarisuhc.managers.scenario.Scenario;

import java.util.Set;

@Scenario(name = "FireLess", author = "volcqnn", icon = Material.WATER_BUCKET,
        description = "You cannot take fire or lava damage.")
public class FireLess extends BaseScenario {
    private final Set<EntityDamageEvent.DamageCause> FIRE_DAMAGE_TYPES = Set.of(
            EntityDamageEvent.DamageCause.FIRE,
            EntityDamageEvent.DamageCause.FIRE_TICK,
            EntityDamageEvent.DamageCause.LAVA,
            EntityDamageEvent.DamageCause.HOT_FLOOR
    );

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (FIRE_DAMAGE_TYPES.contains(event.getCause())) {
            event.setCancelled(true);
        }
    }
}