package us.polarismc.polarisuhc.managers.info.actionbar;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import us.polarismc.polarisuhc.Main;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

public class ActionBarManager {
    private final Main plugin;
    private BukkitTask updateTask;
    private final Map<UUID, ActionBarData> playerData = new ConcurrentHashMap<>();

    public ActionBarManager(Main plugin) {
        this.plugin = plugin;
        startUpdateTask();
    }

    public void setDefault(Player player, Supplier<String> supplier) {
        getOrCreate(player).defaultSupplier = supplier;
    }

    public void setDefault(Player player, Function<Player, Supplier<String>> supplier) {
        getOrCreate(player).defaultSupplier = supplier.apply(player);
    }

    public void setGlobalDefault(Supplier<String> supplier) {
        Bukkit.getOnlinePlayers().forEach(p -> setDefault(p, supplier));
    }

    public void setGlobalDefault(Function<Player, Supplier<String>> supplier) {
        Bukkit.getOnlinePlayers().forEach(p -> setDefault(p, supplier));
    }

    public void sendTemporary(Player player, Supplier<String> supplier, float seconds) {
        ActionBarData data = getOrCreate(player);
        data.temporarySupplier = supplier;

        long ticks = Math.max(1L, (long) (seconds * 20.0f));
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            ActionBarData current = playerData.get(player.getUniqueId());
            if (current != null && current.temporarySupplier == supplier) {
                current.temporarySupplier = null;
            }
        }, ticks);
    }

    public void sendTemporary(Player player, Supplier<String> supplier) {
        sendTemporary(player, supplier, 5);
    }

    public void sendTemporary(Player player, String message, float seconds) {
        sendTemporary(player, () -> message, seconds);
    }

    public void sendTemporary(Player player, String message) {
        sendTemporary(player, () -> message, 5);
    }

    public void sendGlobalTemporary(Supplier<String> supplier) {
        sendGlobalTemporary(supplier, 5);
    }

    public void sendGlobalTemporary(String message) {
        sendGlobalTemporary(() -> message, 5);
    }

    public void sendGlobalTemporary(Supplier<String> supplier, float seconds) {
        Bukkit.getOnlinePlayers().forEach(p -> sendTemporary(p, supplier, seconds));
    }

    public void sendGlobalTemporary(String message, float seconds) {
        sendGlobalTemporary(() -> message, seconds);
    }

    public void clear(Player player) {
        ActionBarData data = playerData.get(player.getUniqueId());
        if (data == null) return;
        data.defaultSupplier = null;
        data.temporarySupplier = null;
    }

    public void clearAll() {
        Bukkit.getOnlinePlayers().forEach(this::clear);
    }

    private void startUpdateTask() {
        if (updateTask != null) updateTask.cancel();

        updateTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Map.Entry<UUID, ActionBarData> entry : playerData.entrySet()) {
                Player player = Bukkit.getPlayer(entry.getKey());
                if (player == null || !player.isOnline()) continue;

                ActionBarData data = entry.getValue();
                Supplier<String> supplier = (data.temporarySupplier != null) ? data.temporarySupplier : data.defaultSupplier;
                if (supplier == null) continue;

                String msg = supplier.get();
                if (msg == null || msg.isEmpty()) continue;

                plugin.utils.actionBar(player, msg);
            }
        }, 0L, 20L);
    }

    public void restartUpdateTask() {
        startUpdateTask();
    }

    private ActionBarData getOrCreate(Player player) {
        return playerData.computeIfAbsent(player.getUniqueId(), k -> new ActionBarData());
    }

    private static final class ActionBarData {
        private Supplier<String> defaultSupplier;
        private Supplier<String> temporarySupplier;
    }
}
