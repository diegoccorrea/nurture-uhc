package us.polarismc.polarisuhc.managers.game;

import net.kyori.adventure.text.Component;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.managers.uhc.UHCState;

import java.util.UUID;

public class WhitelistManager {

    private final Main plugin;

    public WhitelistManager(Main plugin) {
        this.plugin = plugin;
    }

    public boolean shouldBlockLogin(AsyncPlayerPreLoginEvent event) {
        UHCState state = plugin.uhc.getState();

        // Only block during MEETUP or FINALIZED
        if (state != UHCState.MEETUP && state != UHCState.FINALIZED) {
            return false;
        }

        UUID uuid = event.getUniqueId();

        // Alive players can always rejoin
        return plugin.uhc.getAlivePlayers().stream().noneMatch(p -> p.getUniqueId().equals(uuid));

        // TODO: Check if host permission should be checked differently here (with vault)
        // For now, we'll allow since we don't have the Player object yet
        // Hosts can be handled via permission check after player object is available
    }

    public Component getKickMessage() {
        return Component.text("UHC has already ended. Only players who were in-game can rejoin.");
    }
}