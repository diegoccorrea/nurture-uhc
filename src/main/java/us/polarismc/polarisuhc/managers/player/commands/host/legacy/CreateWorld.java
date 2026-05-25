package us.polarismc.polarisuhc.managers.player.commands.host.legacy;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import us.polarismc.polarisuhc.Main;

import java.util.List;
import java.util.Objects;

public class CreateWorld implements TabExecutor {
    private final Main plugin;

    public CreateWorld(Main plugin) {
        this.plugin = plugin;
        Objects.requireNonNull(plugin.getCommand("createworld")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {
        //TODO - remove -a flag and replace it with Amplified scenario
        //↑↑↑ ??? ↑↑↑
        try {
            switch (args.length) {
                case 1 -> {
                    if (args[0].equalsIgnoreCase("-a")) {
                        plugin.uhc.world.setAmplified(true);
                    } else {
                        plugin.uhc.world.setGlobalSeed(Long.parseLong(args[0]));
                    }
                }
                case 2 -> {
                    if (args[1].equalsIgnoreCase("-a")) {
                        plugin.uhc.world.setGlobalSeed(Long.parseLong(args[0]));
                        plugin.uhc.world.setAmplified(true);
                    } else {
                        plugin.uhc.world.setSeed(Long.parseLong(args[0]));
                        plugin.uhc.world.setNetherSeed(Long.parseLong(args[1]));
                        plugin.uhc.world.setEndSeed(Long.parseLong(args[0]));
                    }
                }
                case 3 -> {
                    if (args[1].equalsIgnoreCase("-a")) {
                        plugin.uhc.world.setAmplified(true);
                        plugin.uhc.world.setSeed(Long.parseLong(args[0]));
                        plugin.uhc.world.setNetherSeed(Long.parseLong(args[1]));
                        plugin.uhc.world.setEndSeed(Long.parseLong(args[0]));
                    } else {
                        sendUsage(sender);
                        return false;
                    }
                }
                default -> {
                    sendUsage(sender);
                    return false;
                }
            }
        } catch (NumberFormatException e) {
            plugin.utils.message(sender, "<red>Invalid seed format. Please enter a valid number.");
            return false;
        }
        plugin.uhc.world.createWorlds(sender);
        return true;
    }

    private void sendUsage(CommandSender sender) {
        plugin.utils.message(sender, "<red>Usage: /createworld <seed> [-a] <netherseed>",
                "<red>Example: /createworld",
                "<red>Example: /createworld 12345",
                "<red>Example: /createworld 12345 -a",
                "<red>Example: /createworld 12345 67890");
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        return List.of(
                "-a"
        );
    }
}
