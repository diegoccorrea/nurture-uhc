package us.polarismc.polarisuhc.scenarios;

import org.bukkit.Material;
import us.polarismc.polarisuhc.managers.scenario.BaseScenario;
import us.polarismc.polarisuhc.managers.scenario.Scenario;
import us.polarismc.polarisuhc.managers.scenario.ScenarioProperty;

@Scenario(name = "GoToHell", author = "putindeer", icon = Material.NETHERRACK,
        description = "If you aren't in the <red>Nether</red> during Meetup, you will receive damage.",
        properties = {ScenarioProperty.IN_DEVELOPMENT, ScenarioProperty.ENABLES_NETHER_IN_MEETUP})
public class GoToHell extends BaseScenario {
}
