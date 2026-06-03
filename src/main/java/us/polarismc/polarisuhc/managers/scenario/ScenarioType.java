package us.polarismc.polarisuhc.managers.scenario;

import lombok.Getter;
import us.polarismc.polarisuhc.scenarios.*;

@Getter
public enum ScenarioType {
    ABSORPTION_LESS(AbsorptionLess.class),
    CUT_CLEAN(CutClean.class),
    DOUBLE_ORES(DoubleOres.class),
    TRIPLE_ORES(TripleOres.class),
    FIRE_LESS(FireLess.class),
    FORTUNE_BABIES(FortuneBabies.class),
    FORTUNE_BOYS(FortuneBoys.class),
    FORTUNE_BOYS_PLUS(FortuneBoysPlus.class),
    GO_TO_HELL(GoToHell.class),
    GRAVE_ROBBERS(GraveRobbers.class),
    HADES(Hades.class),
    HASTEY_BABIES(HasteyBabies.class),
    HASTEY_BOYS(HasteyBoys.class),
    HASTEY_BOYS_PLUS(HasteyBoysPlus.class),
    SHIELD_LESS(ShieldLess.class),
    SWITCHEROO(Switcheroo.class),
    SWORD_LESS(SwordLess.class),
    TEAM_INVENTORY(TeamInventory.class),
    TIMBER(Timber.class),
    TIME_BOMB(TimeBomb.class),
    UNBREAKABLE(Unbreakable.class),
    WEB_CAGE(WebCage.class),
    RISKY_FALL(RiskyFall.class),
    BLOCK_RUSH(BlockRush.class),
    BLOOD_ANVIL(BloodAnvil.class),
    FALLOUT(Fallout.class),
    FAST_GETAWAY(FastGetaway.class),
    GAP_ZAP(GapZap.class),
    GUNS_N_ROSES(GunsNRoses.class),
    IRON_BOOST(IronBoost.class),
    SKY_HIGH(SkyHigh.class),
    ROD_LESS(RodLess.class);


    private final Class<? extends BaseScenario> scenarioClass;

    ScenarioType(Class<? extends BaseScenario> scenarioClass) {
        this.scenarioClass = scenarioClass;
    }
}