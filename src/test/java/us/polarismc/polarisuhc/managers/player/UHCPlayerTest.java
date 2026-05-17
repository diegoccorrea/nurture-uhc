package us.polarismc.polarisuhc.managers.player;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import us.polarismc.polarisuhc.Main;

import static org.junit.jupiter.api.Assertions.*;

class UHCPlayerTest {
    private Main plugin;

    @BeforeEach
    void setUp() {
        plugin = MockBukkit.load(Main.class);
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    void addKill_incrementsKills() {
        UHCPlayer player = new UHCPlayer(java.util.UUID.randomUUID(), "TestPlayer");
        assertEquals(0, player.getKills());
        player.addKill();
        assertEquals(1, player.getKills());
        player.addKill();
        assertEquals(2, player.getKills());
    }

    @Test
    void addDeath_incrementsDeaths() {
        UHCPlayer player = new UHCPlayer(java.util.UUID.randomUUID(), "TestPlayer");
        assertEquals(0, player.getDeaths());
        player.addDeath();
        assertEquals(1, player.getDeaths());
        player.addDeath();
        assertEquals(2, player.getDeaths());
    }

    @Test
    void incrementWins_incrementsWins() {
        UHCPlayer player = new UHCPlayer(java.util.UUID.randomUUID(), "TestPlayer");
        assertEquals(0, player.getWins());
        player.incrementWins();
        assertEquals(1, player.getWins());
        player.incrementWins();
        assertEquals(2, player.getWins());
    }

    @Test
    void addDiamond_incrementsDiamondMinedResource() {
        UHCPlayer player = new UHCPlayer(java.util.UUID.randomUUID(), "TestPlayer");
        assertEquals(0, player.getMinedResource(MinedResource.DIAMOND));
        player.addDiamond();
        assertEquals(1, player.getMinedResource(MinedResource.DIAMOND));
        player.addDiamond();
        assertEquals(2, player.getMinedResource(MinedResource.DIAMOND));
    }
}