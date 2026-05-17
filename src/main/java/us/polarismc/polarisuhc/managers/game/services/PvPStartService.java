package us.polarismc.polarisuhc.managers.game.services;

import io.papermc.paper.registry.keys.SoundEventKeys;
import org.bukkit.Bukkit;
import org.bukkit.GameRules;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.events.PvPStartEvent;
import us.polarismc.polarisuhc.managers.uhc.UHCState;

public class PvPStartService {
    private final Main plugin;
    public PvPStartService(Main plugin) {
        this.plugin = plugin;
    }

    public void startPvP() {
        PvPStartEvent event = new PvPStartEvent();
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }

        plugin.uhc.setState(UHCState.PVP);
        plugin.utils.broadcast(SoundEventKeys.BLOCK_BEACON_POWER_SELECT, "<aqua>PvP<gray> has been enabled, good luck!");
        if (plugin.uhc.toggle.isNether() && (!plugin.scen.hasEnabledNetherInMeetup())) {
            plugin.utils.broadcast("<gray>At Meetup, all the people in the <red>Nether</red> will be teleported to a random location in the Overworld.");
        }

        plugin.uhc.world.applyGameruleToPlayingWorlds(GameRules.PVP, true);
        //TODO - add whitelist implementation
    }
}
