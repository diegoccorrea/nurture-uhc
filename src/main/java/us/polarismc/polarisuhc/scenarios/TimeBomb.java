package us.polarismc.polarisuhc.scenarios;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import us.polarismc.polarisuhc.managers.scenario.BaseScenario;
import us.polarismc.polarisuhc.managers.scenario.Scenario;
import us.polarismc.polarisuhc.managers.scenario.ScenarioConfig;
import us.polarismc.polarisuhc.managers.scenario.ScenarioProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Scenario(name = "TimeBomb", author = "volcqnn", icon = Material.TNT,
        description = "When a player dies, a TNT chest appears at their death location. After 30 seconds, it explodes.",
        properties = ScenarioProperty.IN_DEVELOPMENT)
public class TimeBomb extends BaseScenario {
    private int COUNTDOWN_SECONDS = 30;
    private float EXPLOSION_POWER = 4.0f;

    @Override
    protected void loadDefaults(ScenarioConfig config) {
        COUNTDOWN_SECONDS = config.getOrDefault("countdown-seconds", 30, "The amount of seconds that pass before the chest explodes");
        EXPLOSION_POWER = config.getOrDefault("explosion-power", 4.0f, "The explosion power of the chest");
    }

    private final ConcurrentMap<Location, ArmorStand> holograms = new ConcurrentHashMap<>();

    @EventHandler (priority = EventPriority.LOW)
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!isEnabled()) return;

        Location deathLoc = event.getEntity().getLocation();
        List<ItemStack> drops = new ArrayList<>(event.getDrops());
        event.getDrops().clear();

        Location secondLoc = findSecondChestLocation(deathLoc);

        deathLoc.getBlock().setType(Material.CHEST);
        secondLoc.getBlock().setType(Material.CHEST);

        Chest chest1 = (Chest) deathLoc.getBlock().getState();
        Chest chest2 = (Chest) secondLoc.getBlock().getState();

        if (event.getEntity().getKiller() != null) {
            chest1.customName(plugin.utils.chat("Time Bomb"));
        }

        for (int i = 0; i < drops.size(); i++) {
            Chest target = (i % 2 == 0) ? chest1 : chest2;
            target.getInventory().addItem(drops.get(i));
        }

        double midX = (deathLoc.getX() + secondLoc.getX()) / 2.0 + 0.5;
        double midZ = (deathLoc.getZ() + secondLoc.getZ()) / 2.0 + 0.5;
        Location hologramLoc = new Location(deathLoc.getWorld(), midX, deathLoc.getY() + 2.5, midZ);

        ArmorStand hologram = (ArmorStand) deathLoc.getWorld().spawnEntity(hologramLoc, EntityType.ARMOR_STAND);
        hologram.setVisible(false);
        hologram.setGravity(false);
        hologram.setInvulnerable(true);
        hologram.setCustomNameVisible(true);
        hologram.customName(plugin.utils.chat("<red>Explodes in " + COUNTDOWN_SECONDS + "s"));

        List<Location> chestLocations = List.of(deathLoc, secondLoc);
        holograms.put(deathLoc, hologram);

        plugin.utils.delay(COUNTDOWN_SECONDS * 20, () -> explode(chestLocations, hologram));
    }

    private Location findSecondChestLocation(Location primary) {
        int[][] offsets = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int[] offset : offsets) {
            Location candidate = primary.clone().add(offset[0], 0, offset[1]);
            if (candidate.getBlock().getType() == Material.AIR || candidate.getBlock().getType() == Material.CHEST) {
                return candidate;
            }
        }
        return primary.clone().add(1, 0, 0);
    }

    private void explode(List<Location> chestLocations, ArmorStand hologram) {
        for (Location loc : chestLocations) {
            loc.getBlock().setType(Material.AIR);
        }

        Location center = chestLocations.getFirst();
        center.getWorld().createExplosion(center, EXPLOSION_POWER, false, false);

        cleanup(hologram);
    }

    private void cleanup(ArmorStand hologram) {
        if (hologram != null && !hologram.isDead()) {
            hologram.remove();
        }
    }

    @Override
    protected void onDisable() {
        holograms.values().forEach(this::cleanup);
        holograms.clear();
    }
}