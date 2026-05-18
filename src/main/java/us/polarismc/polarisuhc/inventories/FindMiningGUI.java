package us.polarismc.polarisuhc.inventories;

import fr.mrmicky.fastinv.FastInv;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.polarismc.polarisuhc.Main;

import java.util.ArrayList;
import java.util.List;

public class FindMiningGUI extends FastInv {

    private static final int CLOSE_SLOT = 49;
    private static final int CAPACITY = 54;

    public FindMiningGUI(Player viewer, Main plugin) {
        super(owner -> Bukkit.createInventory(owner, 54, plugin.utils.chat("Mining Players")));

        List<Player> miners = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getLocation().getBlockY() < 55) {
                miners.add(player);
            }
        }

        if (miners.isEmpty()) {
            ItemStack empty = plugin.utils.ib(Material.GRAY_STAINED_GLASS_PANE)
                    .name("<gray>No players below Y=55")
                    .lore("Nobody is mining deep underground")
                    .build();
            setItem(22, empty);
        } else {
            int i = 0;
            for (Player miner : miners) {
                if (i >= CAPACITY) break;
                int y = miner.getLocation().getBlockY();
                ItemStack head = plugin.utils.ib(Material.PLAYER_HEAD)
                        .profile(miner)
                        .name(miner.getName())
                        .lore("<gold>Y: " + y)
                        .build();
                Player target = miner;
                setItem(i, head, e -> {
                    if (!target.isOnline()) return;
                    e.getWhoClicked().closeInventory();
                    Bukkit.getScheduler().runTask(plugin, () ->
                            ((Player) e.getWhoClicked()).teleport(target.getLocation()));
                });
                i++;
            }
        }

        ItemStack close = plugin.utils.ib(Material.BARRIER)
                .name("<red>Close")
                .lore("Click to close")
                .build();
        setItem(CLOSE_SLOT, close, e -> e.getWhoClicked().closeInventory());

        open(viewer);
    }
}