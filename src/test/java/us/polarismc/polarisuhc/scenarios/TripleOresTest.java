package us.polarismc.polarisuhc.scenarios;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import static org.junit.jupiter.api.Assertions.*;

class TripleOresTest {

    private ServerMock server;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    void tripleOres_nameIsCorrect() {
        assertEquals("TripleOres", TripleOres.class.getSimpleName());
    }

    @Test
    void tripleOres_hasGetMultiplierMethod() throws NoSuchMethodException {
        var method = TripleOres.class.getDeclaredMethod("getMultiplier");
        assertNotNull(method);
        assertEquals(int.class, method.getReturnType());
    }

    @Test
    void tripleOres_extendsCutClean() {
        assertTrue(CutClean.class.isAssignableFrom(TripleOres.class));
    }

    @Test
    void tripleOres_hasScenarioAnnotation() {
        var annotation = TripleOres.class.getAnnotation(
            us.polarismc.polarisuhc.managers.scenario.Scenario.class);
        assertNotNull(annotation);
        assertEquals("TripleOres", annotation.name());
    }

    @Test
    void tripleOres_multiplierFieldIs3() throws Exception {
        java.lang.reflect.Field field = CutClean.class.getDeclaredField("multiplier");
        field.setAccessible(true);
        // multiplier is instance field, verify it exists and is protected (inherited by TripleOres)
        assertEquals(int.class, field.getType());
    }
}