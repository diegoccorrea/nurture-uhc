package us.polarismc.polarisuhc.managers.info.scoreboard;

import org.junit.jupiter.api.Test;
import us.polarismc.polarisuhc.managers.info.scoreboard.ScoreboardManager.StateConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ScoreboardManager components that don't require server runtime.
 * Full integration tests require a running Minecraft server environment.
 */
class ScoreboardManagerTest {

    @Test
    void stateConfig_record_works() {
        // Test the StateConfig record
        StateConfig config = new StateConfig("Test", List.of("Line1", "Line2"));
        assertEquals("Test", config.title());
        assertEquals(2, config.lines().size());
    }

    @Test
    void stateConfig_record_withEmptyLines() {
        StateConfig config = new StateConfig("Title", List.of());
        assertEquals("Title", config.title());
        assertTrue(config.lines().isEmpty());
    }

    @Test
    void stateConfig_record_equality() {
        StateConfig config1 = new StateConfig("Title", List.of("Line1"));
        StateConfig config2 = new StateConfig("Title", List.of("Line1"));
        assertEquals(config1, config2);
    }
}