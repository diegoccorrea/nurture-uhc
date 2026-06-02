package us.polarismc.polarisuhc.managers.arena;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.util.Utils;

import java.util.Objects;

public class ArenaCommand implements CommandExecutor {

    private final Utils utils;
    private final ArenaManager arenaManager;

    public ArenaCommand(Main plugin) {
        Objects.requireNonNull(plugin.getCommand("arena")).setExecutor(this);
        this.arenaManager = plugin.arena;
        this.utils = plugin.utils;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            utils.message(sender, "<red>Solo los jugadores pueden usar este comando.</red>");
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("arena")) {
            if (args.length == 0) {
                arenaManager.joinArena(player);
                return true;
            }

            String subCommand = args[0].toLowerCase();

            switch (subCommand) {
                case "leave":
                    arenaManager.leaveArena(player);
                    break;
                case "toggle":
                    // Only players with permissions can toggle the arena
                    if (!player.hasPermission("polarisuhc.arena.toggle")) {
                        utils.message(player, "<red>No tienes permiso para usar este comando.</red>");
                        return true;
                    }
                    arenaManager.toggleArena();
                    break;
                // If player execute "/arena" command teleport to arena.
                case "join":
                default:
                    arenaManager.joinArena(player);
                    break;
            }
            return true;
        }
        return false;
    }
}
