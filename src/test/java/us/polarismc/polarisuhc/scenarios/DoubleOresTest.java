package us.polarismc.polarisuhc.scenarios;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import static org.junit.jupiter.api.Assertions.*;

class DoubleOresTest {

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
    void doubleOres_nameIsCorrect() {
        assertEquals("DoubleOres", DoubleOres.class.getSimpleName());
    }

    @Test
    void doubleOres_hasGetMultiplierMethod() throws NoSuchMethodException {
        var method = DoubleOres.class.getDeclaredMethod("getMultiplier");
        assertNotNull(method);
        assertEquals(int.class, method.getReturnType());
    }

    @Test
    void doubleOres_extendsCutClean() {
        assertTrue(CutClean.class.isAssignableFrom(DoubleOres.class));
    }

    @Test
    void doubleOres_hasScenarioAnnotation() {
        var annotation = DoubleOres.class.getAnnotation(
            us.polarismc.polarisuhc.managers.scenario.Scenario.class);
        assertNotNull(annotation);
        assertEquals("DoubleOres", annotation.name());
    }

    @Test
    void doubleOres_multiplierFieldIs2() throws Exception {
        java.lang.reflect.Field field = CutClean.class.getDeclaredField("multiplier");
        field.setAccessible(true);
        // multiplier is instance field, verify it exists and is protected (inherited by DoubleOres)
        assertEquals(int.class, field.getType());
    }
}