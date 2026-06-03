package us.polarismc.polarisuhc.util;

import me.putindeer.api.util.PluginUtils;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.polarismc.polarisuhc.Main;

public class Utils extends PluginUtils {
    private final Main plugin;
    public Utils(Main plugin, String prefix) {
        super(plugin, prefix);
        this.plugin = plugin;
    }

    public ItemStack goBack() {
        return ib(Material.BARRIER).name("Go Back").lore("Click this to go back to the previous menu.").build();
    }

    public String getBorderSizeString(Player player) {
        if (player.getWorld().getEnvironment() == World.Environment.NETHER) {
            return String.valueOf((int) plugin.uhc.world.getNetherWorld().getWorldBorder().getSize());
        } else {
            return String.valueOf((int) plugin.uhc.world.getUhcWorld().getWorldBorder().getSize());
        }
    }

    //todo - (add primary color compatibility)
    public String teamPrefix() {
        return "<blue>Team <dark_gray>» </dark_gray>";
    }

    public String teamPrefix(Player player) {
        return "<blue>Team <dark_gray>» </dark_gray>";
    }

    public boolean hasHostPermission(CommandSender sender) {
        return sender.hasPermission("uhc.host");
    }

    public void damagePlayer(Player player, double damage) {
        if (player.getHealth() - damage <= 0) {
            player.setHealth(0);
        } else {
            player.setHealth(player.getHealth() - damage);
        }
    }

    // TODO - Agregar soporte para Folia
    public void runTaskTimer(Runnable task, long delay, long period) {
        plugin.getServer().getScheduler().runTaskTimer(plugin, task, delay, period);
    }

    public ItemStack head() {
        //TODO - add ghead
        return new ItemStack(Material.PLAYER_HEAD);
    }
}