package us.polarismc.polarisuhc.scenarios;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import us.polarismc.polarisuhc.managers.scenario.BaseScenario;
import us.polarismc.polarisuhc.managers.scenario.Scenario;

import java.util.concurrent.ThreadLocalRandom;

@Scenario(
        name = "RiskyFall",
        description = {
                "There is a &9probability &7that when receiving",
                "fall damage is &9duplicated &7or &9cancelled"
        },
        icon = Material.LEATHER_BOOTS
)
public class RiskyFall extends BaseScenario {

    @EventHandler(
            priority = EventPriority.HIGH,
            ignoreCancelled = true
    )
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player p && e.getCause() == EntityDamageEvent.DamageCause.FALL
                && plugin.uhc.isPlaying(p)) {
            final ThreadLocalRandom random = ThreadLocalRandom.current();
            switch (random.nextInt(2)) {
                case 0 -> e.setDamage(e.getFinalDamage() * 2);
                case 1 -> e.setCancelled(true);
            }
        }
    }

}
