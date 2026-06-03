package us.polarismc.polarisuhc.scenarios;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import us.polarismc.polarisuhc.events.MeetupStartEvent;
import us.polarismc.polarisuhc.managers.player.UHCPlayer;
import us.polarismc.polarisuhc.managers.scenario.BaseScenario;
import us.polarismc.polarisuhc.managers.scenario.Scenario;

@Scenario(
        name = "Fallout",
        description = {
                "If you are above Y50, you will take damage."
        },
        author = "volcqnn",
        icon = org.bukkit.Material.STONE
)
public class Fallout extends BaseScenario {
    @EventHandler
    public void onMeetup(MeetupStartEvent e) {
        plugin.utils.runTaskTimer(this::taskRun,0, 300);
    }

    public void taskRun() {
        plugin.player.getPlayerList().stream()
                .filter(UHCPlayer::isOnline)
                .filter(UHCPlayer::isPlaying)
                .filter(p -> p.getPlayer() != null)
                .filter(p -> p.getPlayer().getLocation().getBlockY() > 50)
                .forEach(p -> {
                    p.getPlayer().damage(4);
                    plugin.utils.message(p.getPlayer(), "<red>You took damage. &7Go down to a lower layer than &950 &7so you don't take damage.");
                });
    }

}
