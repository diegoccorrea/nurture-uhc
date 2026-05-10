package us.polarismc.polarisuhc.managers.scenario;

import lombok.Getter;
import us.polarismc.polarisuhc.scenarios.*;

@Getter
public enum ScenarioType {
    ABSORPTION_LESS(AbsorptionLessScenario.class),
    CUT_CLEAN(CutClean.class),
    FIRE_LESS(FireLessScenario.class),
    FORTUNE_BABIES(FortuneBabies.class),
    FORTUNE_BOYS(FortuneBoys.class),
    FORTUNE_BOYS_PLUS(FortuneBoysPlus.class),
    GO_TO_HELL(GoToHell.class),
    GRAVE_ROBBERS(GraveRobbersScenario.class),
    HADES(Hades.class),
    HASTEY_BABIES(HasteyBabies.class),
    HASTEY_BOYS(HasteyBoys.class),
    HASTEY_BOYS_PLUS(HasteyBoysPlus.class),
    SHIELD_LESS(ShieldLessScenario.class),
    SWITCHEROO(SwitcherooScenario.class),
    SWORD_LESS(SwordLessScenario.class),
    TEAM_INVENTORY(TeamInventory.class),
    TIMBER(TimberScenario.class),
    TIME_BOMB(TimeBombScenario.class),
    COLD_WEAPONS(ColdWeaponsScenario.class),
    UNBREAKABLE(Unbreakable.class);

    private final Class<? extends BaseScenario> scenarioClass;

    ScenarioType(Class<? extends BaseScenario> scenarioClass) {
        this.scenarioClass = scenarioClass;
    }
}