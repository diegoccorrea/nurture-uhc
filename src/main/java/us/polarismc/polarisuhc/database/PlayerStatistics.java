package us.polarismc.polarisuhc.database;

import java.util.UUID;

public record PlayerStatistics(
    UUID uuid,
    String name,
    int gameId,
    int kills,
    int deaths,
    int diamondsMined,
    boolean isWinner
) {}