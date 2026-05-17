package us.polarismc.polarisuhc.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.managers.game.WhitelistManager;

public class WhitelistLoginListener implements Listener {

    private final WhitelistManager whitelistManager;

    public WhitelistLoginListener(Main plugin) {
        this.whitelistManager = new WhitelistManager(plugin);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        if (whitelistManager.shouldBlockLogin(event)) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, whitelistManager.getKickMessage());
        }
    }
}