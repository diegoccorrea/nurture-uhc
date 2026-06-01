package us.polarismc.polarisuhc.managers.uhc;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.config.border.BorderManager;
import us.polarismc.polarisuhc.config.customcrafts.CustomCraftManager;
import us.polarismc.polarisuhc.config.duration.DurationManager;
import us.polarismc.polarisuhc.config.potion.PotionManager;
import us.polarismc.polarisuhc.config.rates.RatesManager;
import us.polarismc.polarisuhc.config.toggle.ToggleManager;
import us.polarismc.polarisuhc.config.world.WorldManager;
import us.polarismc.polarisuhc.managers.info.gui.GUIManager;
import us.polarismc.polarisuhc.managers.player.UHCPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
public class UHCManager {
    private final Main plugin;
    public final BorderManager border;
    public final CustomCraftManager crafts;
    public final DurationManager duration;
    public final PotionManager potion;
    public final ToggleManager toggle;
    public final RatesManager rates;
    public final WorldManager world;
    public final GUIManager ui;
    private UHCState state = UHCState.IDLE;
    private UUID host = null;
    private int hostNumber = 0;
    private int number = 0;
    private List<UHCPlayer> alivePlayers = new ArrayList<>();
    private List<UHCPlayer> deadPlayers = new ArrayList<>();

    public UHCManager(Main plugin) {
        this.plugin = plugin;
        this.border = new BorderManager(plugin);
        this.crafts = new CustomCraftManager(plugin);
        this.duration = new DurationManager(plugin);
        this.potion = new PotionManager(plugin);
        this.toggle = new ToggleManager(plugin);
        this.rates = new RatesManager(plugin);
        this.world = new WorldManager(plugin);
        this.ui = new GUIManager(plugin);
    }

    public List<Player> getPlayingPlayers() {
        return alivePlayers.stream().map(UHCPlayer::getOfflinePlayer).filter(OfflinePlayer::isOnline).map(OfflinePlayer::getPlayer).filter(Objects::nonNull).toList();
    }

    public boolean isStarting() {
        return state.getPriority() >= UHCState.PRESTARTED.getPriority() && state.getPriority() < UHCState.STARTED.getPriority();
    }

    public boolean hasStarted() {
        return state.getPriority() >= UHCState.STARTED.getPriority();
    }

    public boolean hasPvPStarted() {
        return state.getPriority() >= UHCState.PVP.getPriority();
    }

    public boolean hasMeetupStarted() {
        return state.getPriority() >= UHCState.MEETUP.getPriority();
    }
    
    public boolean isScattering() {
        return state == UHCState.SCATTERING;
    }

    public boolean isNotIdle() {
        return state != UHCState.IDLE;
    }

    public boolean isFinalized() {
        return state == UHCState.FINALIZED;
    }

    public boolean isPlaying(@NotNull Player player) {
        return alivePlayers.contains(plugin.player.getUHCPlayer(player));
    }
}