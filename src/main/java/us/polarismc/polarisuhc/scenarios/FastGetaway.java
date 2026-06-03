package us.polarismc.polarisuhc.scenarios;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import us.polarismc.polarisuhc.managers.scenario.BaseScenario;
import us.polarismc.polarisuhc.managers.scenario.Scenario;

@Scenario(
        name = "Fast Getaway",
        description = {
                "When you take damage, you get Speed 2 for 5 seconds."
        },
        author = "volcqnn",
        icon = org.bukkit.Material.SUGAR
)
public class FastGetaway extends BaseScenario {

    @EventHandler(priority = EventPriority.LOW)
    public void onDeath(PlayerDeathEvent e){
        final Player p = e.getEntity().getKiller();
        if (p != null && (plugin.uhc.isPlaying(p) || plugin.uhc.isPlayingArena(p)))
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1200, 1, false, false));
    }

}
