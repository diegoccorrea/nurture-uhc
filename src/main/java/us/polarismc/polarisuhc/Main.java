package us.polarismc.polarisuhc;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import us.polarismc.polarisuhc.managers.arena.ArenaManager;
import us.polarismc.polarisuhc.managers.channel.ChannelManager;
import us.polarismc.polarisuhc.managers.game.GameFlowManager;
import us.polarismc.polarisuhc.managers.game.timer.GameTimer;
import us.polarismc.polarisuhc.managers.hub.HubManager;
import us.polarismc.polarisuhc.managers.info.InfoManager;
import us.polarismc.polarisuhc.managers.player.PlayerManager;
import us.polarismc.polarisuhc.managers.scenario.ScenarioManager;
import us.polarismc.polarisuhc.managers.statistics.StatisticsManager;
import us.polarismc.polarisuhc.managers.team.TeamManager;
import us.polarismc.polarisuhc.managers.uhc.UHCManager;
import us.polarismc.polarisuhc.util.StartThings;
import us.polarismc.polarisuhc.util.Utils;

public class Main extends JavaPlugin {
    @Getter
    public static Main instance;
    public Utils utils;
    public StartThings start;
    public UHCManager uhc;
    public ScenarioManager scen;
    public PlayerManager player;
    public TeamManager team;
    public GameTimer timer;
    public ChannelManager channel;
    public InfoManager info;
    public GameFlowManager game;
    public ArenaManager arena;
    public HubManager hub;
    public StatisticsManager statistics;

    @Override
    public void onEnable() {
        instance = this;
        utils = new Utils(this, "<blue><bold>UHC</bold></blue> <dark_gray>»</dark_gray> <reset>");
        start = new StartThings(this);
        statistics = new StatisticsManager(this);
        arena = new ArenaManager(this);
    }

    @Override
    public void onDisable() {
        if (start != null) {
            start.disable();
        }
        if (arena != null) {
            arena.cleanup(); // Ensure arena is cleaned up on disable
        }
    }
}
