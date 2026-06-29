package us.polarismc.polarisuhc.managers.game.services.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import us.polarismc.polarisuhc.Main;
import us.polarismc.polarisuhc.managers.player.UHCPlayer;
import us.polarismc.polarisuhc.managers.scenario.ScenarioProperty;
import us.polarismc.polarisuhc.managers.team.UHCTeam;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class LocationFinderService {
    private final Main plugin;

    public LocationFinderService(Main plugin) {
        this.plugin = plugin;
    }

    public @Nullable Location getScatterLocationFor(@NotNull Player player) {
        UHCPlayer uhcPlayer = plugin.player.getUHCPlayer(player);
        UHCTeam team = uhcPlayer.getTeam();

        if (team != null) {
            Location teamSpawn = team.getTeamSpawn();
            if (teamSpawn == null) {
                teamSpawn = findSafeScatterLocation();
                team.setTeamSpawn(teamSpawn);
            }
            return teamSpawn;
        }

        return findSafeScatterLocation();
    }

    private int getMaxY() {
        return plugin.getConfig().getInt("settings.location-finder.max-y", 110);
    }

    private int getSearchAttempts() {
        return plugin.getConfig().getInt("settings.location-finder.search-attempts", 200);
    }

    public Location findSafeScatterLocation() {
        boolean disabledOverworld = plugin.scen.hasProperty(ScenarioProperty.DISABLES_OVERWORLD);

        World world = disabledOverworld ? plugin.uhc.world.getNetherWorld() : plugin.uhc.world.getUhcWorld();
        if (world == null) return null;

        int radius = (disabledOverworld ? plugin.uhc.border.getCurrentNetherBorder() : plugin.uhc.border.getCurrentBorder()) / 2;


        for (int i = 0; i < getSearchAttempts(); i++) {
            Location loc = getScatterLocation(world, disabledOverworld, radius, false);
            if (loc != null) return loc;
        }

        return getScatterLocation(world, disabledOverworld, radius, true);
    }

    private @Nullable Location getScatterLocation(@NotNull World world, boolean isNether, int radius, boolean isFallback) {
        Random random = ThreadLocalRandom.current();
        int x = random.nextInt(-radius, radius);
        int z = random.nextInt(-radius, radius);

        if (isNether) {
            for (int y = 20; y < getMaxY(); y++) {
                Location loc = new Location(world, x + 0.5, y, z + 0.5);
                if (isSafe(loc)) return loc;
            }
        } else {
            int y = world.getHighestBlockYAt(x, z) + 1;
            Location loc = new Location(world, x + 0.5, y, z + 0.5);
            if (isSafe(loc)) return loc;
        }

        if (!isFallback) return null;
        return new Location(world, x + 0.5, isNether ? random.nextInt(20, 110) : world.getHighestBlockYAt(x, z) + 1, z + 0.5);
    }

    private boolean isSafe(@NotNull Location loc) {
        Block feet = loc.getBlock();
        Block below = feet.getRelative(0, -1, 0);
        Block head = feet.getRelative(0, 1, 0);

        return below.getType().isSolid()
                && !feet.isLiquid()
                && !head.isLiquid()
                && feet.getType() == Material.AIR
                && head.getType() == Material.AIR;
    }
}