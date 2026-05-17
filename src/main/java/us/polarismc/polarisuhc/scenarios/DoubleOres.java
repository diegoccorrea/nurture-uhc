package us.polarismc.polarisuhc.scenarios;

import org.bukkit.Material;
import us.polarismc.polarisuhc.managers.scenario.Scenario;
import us.polarismc.polarisuhc.managers.scenario.ScenarioConfig;

@Scenario(name = "DoubleOres", author = "volcqnn", icon = Material.RAW_IRON,
        description = "Mining ore blocks yields double the output.",
        inDevelopment = true)
public class DoubleOres extends CutClean {

    @Override
    protected int getMultiplier() {
        return 2;
    }
}