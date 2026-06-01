package us.polarismc.polarisuhc.scenarios;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.jetbrains.annotations.NotNull;
import us.polarismc.polarisuhc.managers.scenario.BaseScenario;
import us.polarismc.polarisuhc.managers.scenario.Scenario;

@Scenario(
        name = "WebCage",
        author = "volcqnn",
        icon = Material.COBWEB
)
public class WebCage extends BaseScenario {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        final Location loc = event.getEntity().getLocation().clone();

        createCobwebBox(loc, 3, 3);
    }

    public static void createCobwebBox(@NotNull final Location center, int radius, int height) {
        World world = center.getWorld();

        int baseX = center.getBlockX();
        int baseY = center.getBlockY();
        int baseZ = center.getBlockZ();

        for (int y = 0; y < height; y++) {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {

                    // Solo los bordes de la caja
                    boolean border =
                            Math.abs(x) == radius ||
                                    Math.abs(z) == radius;

                    if (!border) {
                        continue;
                    }

                    world.getBlockAt(
                            baseX + x,
                            baseY + y,
                            baseZ + z
                    ).setType(Material.COBWEB);
                }
            }
        }
    }

}
