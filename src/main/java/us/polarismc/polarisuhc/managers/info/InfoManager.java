package us.polarismc.polarisuhc.managers.info;

import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.managers.info.actionbar.ActionBarManager;
import us.polarismc.polarisuhc.managers.info.gui.GUIManager;
import us.polarismc.polarisuhc.managers.info.motd.MOTDManager;
import us.polarismc.polarisuhc.managers.info.nametag.NametagManager;
import us.polarismc.polarisuhc.managers.info.scoreboard.ScoreboardManager;
import us.polarismc.polarisuhc.managers.info.tab.TabManager;

@SuppressWarnings("FieldCanBeLocal")
public class InfoManager {
    private final Main plugin;
    public final ActionBarManager bar;
    public final GUIManager ui;
    public final MOTDManager motd;
    public final NametagManager nametag;
    public final ScoreboardManager board;
    public final TabManager tab;
    public InfoManager(Main plugin) {
        this.plugin = plugin;
        bar = new ActionBarManager(plugin);
        ui = new GUIManager(plugin);
        motd = new MOTDManager(plugin);
        nametag = new NametagManager(plugin);
        board = new ScoreboardManager(plugin);
        tab = new TabManager(plugin);
    }

    public void restartAllTasks() {
        bar.restartUpdateTask();
        board.reloadConfig();
    }

    public void handleInfoStart() {
        plugin.timer.start();
        plugin.info.bar.setGlobalDefault(player -> () -> plugin.timer.actionBarNext(player));
        plugin.info.restartAllTasks();
    }
}
