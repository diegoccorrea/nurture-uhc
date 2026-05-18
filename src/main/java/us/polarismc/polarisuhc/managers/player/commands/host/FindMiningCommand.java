package us.polarismc.polarisuhc.managers.player.commands.host;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.inventories.FindMiningGUI;

import java.util.Objects;

public class FindMiningCommand implements CommandExecutor {
    private final Main plugin;

    public FindMiningCommand(Main plugin) {
        this.plugin = plugin;
        Objects.requireNonNull(plugin.getCommand("findmining")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }
        new FindMiningGUI(player, plugin);
        return true;
    }
}