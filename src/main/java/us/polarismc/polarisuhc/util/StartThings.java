package us.polarismc.polarisuhc.util;

import fr.mrmicky.fastinv.FastInvManager;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.config.toggle.ToggleSetting;
import us.polarismc.polarisuhc.config.toggle.handlers.NerfedStrength;
import us.polarismc.polarisuhc.debug.*;
import us.polarismc.polarisuhc.events.handlers.DeathHandler;
import us.polarismc.polarisuhc.listeners.RatesListener;
import us.polarismc.polarisuhc.listeners.WhitelistLoginListener;
import us.polarismc.polarisuhc.managers.arena.ArenaManager;
import us.polarismc.polarisuhc.managers.channel.ChannelManager;
import us.polarismc.polarisuhc.managers.game.GameFlowManager;
import us.polarismc.polarisuhc.managers.game.timer.GameTimer;
import us.polarismc.polarisuhc.managers.hub.HubManager;
import us.polarismc.polarisuhc.managers.info.InfoManager;
import us.polarismc.polarisuhc.managers.player.PlayerManager;
import us.polarismc.polarisuhc.managers.player.commands.host.ManualStart;
import us.polarismc.polarisuhc.managers.player.commands.host.QuickStart;
import us.polarismc.polarisuhc.managers.player.commands.host.WorldC;
import us.polarismc.polarisuhc.managers.player.commands.host.legacy.CreateWorld;
import us.polarismc.polarisuhc.managers.player.commands.host.legacy.Toggle;
import us.polarismc.polarisuhc.managers.player.commands.host.legacy.ToggleScenario;
import us.polarismc.polarisuhc.managers.scenario.ScenarioManager;
import us.polarismc.polarisuhc.managers.scenario.commands.ScenCommand;
import us.polarismc.polarisuhc.managers.team.TeamManager;
import us.polarismc.polarisuhc.managers.team.commands.*;
import us.polarismc.polarisuhc.managers.uhc.UHCManager;

public class StartThings {
    private final Main plugin;
    public StartThings(Main plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        enable();
    }

    public void enable() {
        registerCommands();
        registerListeners();
        registerManagers();
        registerLanguage();
    }

    public void disable() {
        plugin.team.disableAndDeleteTeams();
        plugin.uhc.crafts.disableAll();
        plugin.player.removeAllDisplays();
        NerfedStrength strength = (NerfedStrength) plugin.uhc.toggle.getHandler(ToggleSetting.NERFED_STRENGTH);
        strength.removeAllStrengthNerfs();
    }

    public void registerCommands() {
        // Dev commands (uhc.debug)
        new Debug(plugin);
        new GUI(plugin);
        new Hex(plugin);
        new TeamHex(plugin);
        new TestScatter(plugin);
        new TestTeams(plugin);
        new NametagTest(plugin);
        // Player commands (uhc.managers.player.commands)
        // Host commands (uhc.managers.player.commands.host)
        new ManualStart(plugin);
        new QuickStart(plugin);
        new WorldC(plugin);
        // Host config-related commands (uhc.managers.player.commands.host.legacy)
        new CreateWorld(plugin);
        new Toggle(plugin);
        new ToggleScenario(plugin);
        new ScenCommand(plugin);
        // Team commands (uhc.managers.team)
        new BookStuff(plugin);
        new ColorTeam(plugin);
        new EmojiTeam(plugin);
        new MakeTeams(plugin);
        new MinedOres(plugin);
        new Ores(plugin);
        new Solo(plugin);
        new TeamChat(plugin);
        new TeamCommand(plugin);
        new TeamLocation(plugin);
    }

    public void registerListeners() {
        new DeathHandler(plugin);
        new WhitelistLoginListener(plugin);
        new RatesListener(plugin);
    }

    public void registerManagers() {
        FastInvManager.register(plugin);
        plugin.uhc = new UHCManager(plugin);
        plugin.scen = new ScenarioManager(plugin);
        plugin.player = new PlayerManager(plugin);
        plugin.team = new TeamManager(plugin);
        plugin.timer = new GameTimer(plugin);
        plugin.channel = new ChannelManager(plugin);
        plugin.info = new InfoManager(plugin);
        plugin.game = new GameFlowManager(plugin);
        plugin.arena = new ArenaManager(plugin);
        plugin.hub = new HubManager(plugin);
    }

    public void registerLanguage() {
        //TODO - rehacer esto pls
    }
}
