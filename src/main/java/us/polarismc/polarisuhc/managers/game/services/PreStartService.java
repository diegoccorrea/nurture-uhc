package us.polarismc.polarisuhc.managers.game.services;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.managers.uhc.UHCState;

import java.util.Arrays;
import java.util.Objects;

public class PreStartService {
    private final Main plugin;
    public PreStartService(Main plugin) {
        this.plugin = plugin;
    }

    public void handleJoinDuringPreStart(Player player) {
        if (plugin.uhc.getState() != UHCState.PRESTARTED) return;
        applyPreStartEffects(player);
    }

    public void preStart(Player host) {
        plugin.utils.broadcast("<gray>The scatter is going to start soon...");

        if (plugin.arena.isEnabled()) {
            plugin.arena.toggleArena();
        }

        plugin.uhc.setState(UHCState.PRESTARTED);

        Bukkit.getOnlinePlayers().stream().filter(onlinePlayer -> onlinePlayer.getGameMode() == GameMode.SURVIVAL).forEach(this::applyPreStartEffects);
        plugin.game.advancementService.startResetTask(5L);

        plugin.game.finalizeStep(host);
    }

    @Getter private final Attribute[] prestartAttributes = {Attribute.ATTACK_DAMAGE, Attribute.JUMP_STRENGTH, Attribute.MOVEMENT_SPEED, Attribute.BLOCK_BREAK_SPEED};

    //TODO - reset effects + attributes

    private void applyPreStartEffects(Player player) {
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setLevel(0);
        player.setExp(0.0f);
        player.getInventory().clear();

        Arrays.stream(prestartAttributes).map(player::getAttribute).filter(Objects::nonNull).forEach(attribute -> attribute.setBaseValue(0.0D));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, Integer.MAX_VALUE, 0, false, false));
        player.setInvulnerable(true);

        player.setFlying(false);
        player.setAllowFlight(false);

        plugin.player.removeAllPassengers(player);

        plugin.game.advancementService.enqueueReset(player);
    }
}
