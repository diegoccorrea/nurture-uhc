package us.polarismc.polarisuhc.managers.scenario.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.inventories.ScenGUI;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ScenCommand implements TabExecutor {
    private final Main plugin;

    public ScenCommand(Main plugin) {
        this.plugin = plugin;
        Objects.requireNonNull(plugin.getCommand("scen")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof org.bukkit.entity.Player player)) {
            return true;
        }
        new ScenGUI(player, plugin);
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        return Collections.emptyList();
    }
}