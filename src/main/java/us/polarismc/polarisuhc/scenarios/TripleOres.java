package us.polarismc.polarisuhc.scenarios;

import org.bukkit.Material;
import us.polarismc.polarisuhc.managers.scenario.Scenario;
import us.polarismc.polarisuhc.managers.scenario.ScenarioProperty;

@Scenario(name = "TripleOres", author = "volcqnn", icon = Material.RAW_GOLD,
        description = "Mining ore blocks yields triple the output.",
        properties = ScenarioProperty.IN_DEVELOPMENT)
public class TripleOres extends CutClean {

    @Override
    protected int getMultiplier() {
        return 3;
    }
}