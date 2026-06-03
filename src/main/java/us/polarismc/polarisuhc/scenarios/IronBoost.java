package us.polarismc.polarisuhc.scenarios;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import us.polarismc.polarisuhc.managers.scenario.BaseScenario;
import us.polarismc.polarisuhc.managers.scenario.Scenario;

@Scenario(
        name = "Iron Boost",
        description = {
                "Right-clicking Iron Blocks grants you Resistance 3 for 3 seconds."
        },
        author = "volcqnn",
        icon = org.bukkit.Material.IRON_INGOT
)
public class IronBoost extends BaseScenario {

        @EventHandler
        public void onInteract(PlayerInteractEvent event) {
                if (event.getHand() != EquipmentSlot.HAND) return;
                if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
                if (event.getItem() == null || event.getItem().getType() != Material.IRON_BLOCK) return;


                final Player p = event.getPlayer();
                if (!plugin.uhc.isPlayingArena(p) || !plugin.uhc.isPlayingArena(p)) return;

                if (event.getItem().getAmount() == 1)
                        p.getInventory().removeItem(event.getItem());
                else
                        event.getItem().setAmount(event.getItem().getAmount() - 1);

                p.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 60, 2));
                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_BURP, 0.6f, 0.8f);
        }

        @EventHandler
        public void onPlacing(BlockPlaceEvent e){
                if (e.getBlockPlaced().getType() == Material.IRON_BLOCK) e.setCancelled(true);
        }

}
