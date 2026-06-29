package us.polarismc.polarisuhc.managers.scenario;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import us.polarismc.polarisuhc.Main;

import java.util.Arrays;
import java.util.Collections;


public abstract class BaseScenario implements Listener {
    @Getter private boolean enabled = false;
    protected final Main plugin = Main.getInstance();

    private final Scenario annotation;
    @Getter private ScenarioConfig config;
    private boolean shouldLoad;

    protected BaseScenario() {
        this.annotation = this.getClass().getAnnotation(Scenario.class);
        if (annotation == null) {
            throw new RuntimeException("Scenario must be annotated with @Scenario: " + getClass().getSimpleName());
        }
        initConfig();
    }

    protected void loadDefaults(ScenarioConfig config) {}

    public final boolean shouldLoad() {
        return shouldLoad;
    }

    public final String getName() {
        return annotation.name();
    }

    public final String getDisplayName() {
        String display = annotation.displayName();
        return display.isEmpty() ? annotation.name() : display;
    }

    public final String[] getAuthors() {
        if (annotation.authors().length > 0) {
            return annotation.authors();
        }
        return new String[]{annotation.author()};
    }

    public final String getAuthorString() {
        String[] authors = getAuthors();

        if (authors.length == 0) {
            return "Unknown";
        }

        return String.join(", ", authors);
    }

    public final String[] getCommandNames() {
        if (annotation.commands().length > 0) {
            return annotation.commands();
        }

        return new String[]{annotation.command()};
    }

    public final ItemStack getIcon() {
        return getIconStack();
    }

    public final int getPriority() {
        return annotation.priority();
    }

    public final Component[] getDescription() {
        return Arrays.stream(getDescriptionLines())
                .map(plugin.utils::chat)
                .toArray(Component[]::new);
    }

    public final ScenarioType[] getIncompatibleScenarios() {
        return annotation.incompatibleWith();
    }

    public final boolean isInDevelopment() {
        return Arrays.stream(annotation.properties()).toList().contains(ScenarioProperty.IN_DEVELOPMENT);
    }

    protected String[] getDescriptionLines() {
        return annotation.description();
    }

    protected ItemStack getIconStack() {
        return new ItemStack(annotation.icon());
    }

    public final boolean checkProperties(ScenarioProperty... properties) {
        for (ScenarioProperty property : properties) {
            if (Arrays.stream(annotation.properties()).toList().contains(property)) {
                return true;
            }
        }
        return false;
    }

    public final void enable() {
        if (enabled) return;

        Bukkit.getPluginManager().registerEvents(this, plugin);

        enabled = true;
        plugin.utils.broadcast(getDisplayName() + " has been <blue>enabled</blue>.");

        registerScenarioCommands();
        onEnable();
    }

    public final void disable() {
        if (!enabled) return;

        unregisterScenarioCommands();
        HandlerList.unregisterAll(this);

        enabled = false;
        plugin.utils.broadcast(getDisplayName() + " has been <blue>disabled</blue>.");
        onDisable();
    }

    private void registerScenarioCommands() {
        for (String name : getCommandNames()) {
            if (name.isBlank()) continue;

            PluginCommand command = plugin.getCommand(name);
            if (command == null) {
                plugin.getLogger().warning("Command '" + name + "' not found in plugin.yml (scenario " + getName() + ")");
                continue;
            }

            if (this instanceof CommandExecutor executor) {
                command.setExecutor(executor);
            }
            if (this instanceof TabCompleter completer) {
                command.setTabCompleter(completer);
            }
        }
    }

    private void unregisterScenarioCommands() {
        for (String name : getCommandNames()) {
            if (name.isBlank()) continue;

            PluginCommand command = plugin.getCommand(name);
            if (command == null) continue;

            command.setExecutor((sender, cmd, label, args) -> {
                plugin.utils.message(sender, "<red>" + getDisplayName() + " isn't enabled.");
                return true;
            });
            command.setTabCompleter((sender, cmd, alias, args) -> Collections.emptyList());
        }
    }

    public void initConfig() {
        this.config = new ScenarioConfig(plugin, getDisplayName().toLowerCase().replace(" ", "_"));
        shouldLoad = config.getOrDefault("enabled", true);
        loadDefaults(config);
    }

    public void reloadConfig() {
        config.reload();
        shouldLoad = config.getOrDefault("enabled", true);
        loadDefaults(config);
    }

    protected void onEnable() {}
    protected void onDisable() {}
}