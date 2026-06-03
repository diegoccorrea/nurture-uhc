package us.polarismc.polarisuhc.scenarios;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import us.polarismc.polarisuhc.events.LateScatterPlayerEvent;
import us.polarismc.polarisuhc.events.MeetupStartEvent;
import us.polarismc.polarisuhc.events.UHCStartEvent;
import us.polarismc.polarisuhc.managers.player.UHCPlayer;
import us.polarismc.polarisuhc.managers.scenario.BaseScenario;
import us.polarismc.polarisuhc.managers.scenario.Scenario;

@Scenario(
        name = "Sky High",
        description = {
                "If you are below &9Y150 &7you will receive damage"
        },
        author = "volcqnn",
        icon = Material.FEATHER
)
public class SkyHigh extends BaseScenario {

    @EventHandler
    public void onStart(UHCStartEvent e) {
        e.getPlayers().stream()
                .filter(UHCPlayer::isOnline)
                .filter(p -> p.getPlayer() != null)
                .filter(p -> p.getPlayer().getGameMode() == GameMode.SURVIVAL)
                .forEach(p -> giveKit(p.getPlayer()));
    }

    @EventHandler
    public void onLS(LateScatterPlayerEvent e){
        giveKit(e.getPlayer());
    }

    private void giveKit(Player p){
        p.getInventory().addItem(plugin.utils.ib(Material.CHEST)
                .name("<aqua><b>SkyHigh Kit")
                .lore("&fPlace this chest to get the SkyHigh Kit.")
                .addItem(new ItemStack(Material.WHITE_GLAZED_TERRACOTTA, 128))
                .addItem(new ItemStack(Material.PUMPKIN))
                .addItem(new ItemStack(Material.SNOW_BLOCK, 2))
                .addItem(new ItemStack(Material.DIAMOND_SHOVEL))
                .addItem(new ItemStack(Material.STRING, 2))
                .addItem(new ItemStack(Material.FEATHER, 16)).build());
    }

    @EventHandler
    public void onMeetup(MeetupStartEvent e) {
        plugin.utils.runTaskTimer(this::runTask, 0, 300);
    }

    public void runTask() {
        Bukkit.getOnlinePlayers().stream().filter(p -> !p.getWorld().getName().equalsIgnoreCase("lobby"))
                .filter(p -> p.getGameMode() == GameMode.SURVIVAL).filter(p -> p.getLocation().getBlockY() < 150).forEach(p -> {
                    p.damage(4);
                    plugin.utils.message(p, "<red>You took damage. <gray>Go up to a large layer than <blue>150 <gray>so you don't take damage.");
                });
    }

}
