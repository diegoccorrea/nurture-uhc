package us.polarismc.polarisuhc.scenarios;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffectType;
import us.polarismc.polarisuhc.managers.scenario.BaseScenario;
import us.polarismc.polarisuhc.managers.scenario.Scenario;
import us.polarismc.polarisuhc.util.Utils;

@Scenario(
        name = "GapZap",
        description = {
                "If you have a regeneration effect, when",
                "you take damage the effect will wear off."
        },
        author = "volcqnn",
        icon = Material.GOLDEN_APPLE
)
public class GapZap extends BaseScenario {
    private final Utils utils = plugin.utils;

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player p && (plugin.uhc.isPlaying(p) || plugin.uhc.isPlayingArena(p)))
            utils.delay(1, () -> p.removePotionEffect(PotionEffectType.REGENERATION));
    }

}
