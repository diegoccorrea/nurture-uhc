package us.polarismc.polarisuhc.config.toggle.handlers;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import us.polarismc.polarisuhc.config.toggle.ToggleHandler;
import us.polarismc.polarisuhc.config.toggle.ToggleInfo;
import us.polarismc.polarisuhc.events.MeetupStartEvent;
import us.polarismc.polarisuhc.managers.player.WarnReason;
import us.polarismc.polarisuhc.managers.scenario.ScenarioProperty;

import java.util.Objects;

@ToggleInfo(id = "auto-mining-warn", icon = Material.IRON_PICKAXE, displayName = "Auto Mining Warn")
public class AutoMiningWarn extends ToggleHandler {
    @EventHandler
    public void onMeetupStart(MeetupStartEvent event) {
        if (plugin.scen.hasProperty(ScenarioProperty.ENABLES_MINING_IN_MEETUP)) return;
        event.getAlivePlayers().stream().filter(player -> !event.getTeleportedPlayers().contains(player))
                .filter(player -> Objects.requireNonNull(player.getPlayer()).getLocation().getBlockY() < 50)
                .forEach(player -> plugin.utils.delay(200, () -> {
                    if (!player.isOnline() || player.getPlayer().getLocation().getBlockY() >= 50) return;
                    player.warn(WarnReason.MINING);
                }));
    }
}
