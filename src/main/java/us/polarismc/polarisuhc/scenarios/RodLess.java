package us.polarismc.polarisuhc.scenarios;

import org.bukkit.Material;
import us.polarismc.polarisuhc.managers.scenario.BaseScenario;
import us.polarismc.polarisuhc.managers.scenario.Scenario;

@Scenario(
        name = "RodLess",
        icon = Material.FISHING_ROD,
        description = {
                "You can't craft rods."
        }
)
public class RodLess extends BaseScenario {
}
