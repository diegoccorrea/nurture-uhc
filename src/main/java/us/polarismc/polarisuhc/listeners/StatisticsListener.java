package us.polarismc.polarisuhc.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.events.UHCStartEvent;
import us.polarismc.polarisuhc.managers.player.UHCPlayer;

public class StatisticsListener implements Listener {
    private final Main plugin;

    public StatisticsListener(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onUHCStart(UHCStartEvent event) {
        plugin.statistics.startGame(event.getPlayers().isEmpty() ? null : event.getPlayers().get(0).getUniqueId());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getPlayer();
        Player killer = victim.getKiller();
        if (killer == null) return;
        UHCPlayer killerUHC = plugin.player.getUHCPlayer(killer);
        UHCPlayer victimUHC = plugin.player.getUHCPlayer(victim);
        plugin.statistics.trackKill(killerUHC, victimUHC);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Material block = event.getBlock().getType();
        if (block != Material.DIAMOND_ORE && block != Material.DEEPSLATE_DIAMOND_ORE) return;
        Player player = event.getPlayer();
        UHCPlayer uhcPlayer = plugin.player.getUHCPlayer(player);
        if (uhcPlayer == null || !uhcPlayer.isPlaying()) return;
        plugin.statistics.trackDiamond(uhcPlayer);
    }
}
