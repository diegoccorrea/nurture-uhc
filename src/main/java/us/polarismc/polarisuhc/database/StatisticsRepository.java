package us.polarismc.polarisuhc.database;

import us.polarismc.polarisuhc.Main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class StatisticsRepository {
    private final SQLiteConnection sqlite;
    private final Main plugin;

    public StatisticsRepository(Main plugin) {
        this.plugin = plugin;
        this.sqlite = new SQLiteConnection(plugin);
    }

    public void initialize() {
        // Tables are created in SQLiteConnection constructor
    }

    public int createGame(UUID hostUuid) {
        String sql = "INSERT INTO games (host_uuid, start_time) VALUES (?, ?)";
        try (Connection conn = sqlite.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, hostUuid.toString());
            stmt.setLong(2, System.currentTimeMillis());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to create game: " + e.getMessage());
        }
        return -1;
    }

    public void savePlayerStats(UUID uuid, String name, int gameId, int kills, int deaths, int diamonds, boolean isWinner) {
        String sql = "INSERT INTO player_stats (uuid, game_id, name, kills, deaths, diamonds_mined, is_winner) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?) " +
            "ON CONFLICT(uuid, game_id) DO UPDATE SET " +
            "name = excluded.name, kills = excluded.kills, deaths = excluded.deaths, " +
            "diamonds_mined = excluded.diamonds_mined, is_winner = excluded.is_winner";
        try (Connection conn = sqlite.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, uuid.toString());
            stmt.setInt(2, gameId);
            stmt.setString(3, name);
            stmt.setInt(4, kills);
            stmt.setInt(5, deaths);
            stmt.setInt(6, diamonds);
            stmt.setInt(7, isWinner ? 1 : 0);
            stmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to save player stats: " + e.getMessage());
        }
    }

    public Optional<PlayerStatistics> getPlayerStats(UUID uuid, int gameId) {
        String sql = "SELECT * FROM player_stats WHERE uuid = ? AND game_id = ?";
        try (Connection conn = sqlite.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, uuid.toString());
            stmt.setInt(2, gameId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new PlayerStatistics(
                        UUID.fromString(rs.getString("uuid")),
                        rs.getString("name"),
                        rs.getInt("game_id"),
                        rs.getInt("kills"),
                        rs.getInt("deaths"),
                        rs.getInt("diamonds_mined"),
                        rs.getInt("is_winner") == 1
                    ));
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to get player stats: " + e.getMessage());
        }
        return Optional.empty();
    }

    public void endGame(int gameId, UUID winnerUuid) {
        String sql = "UPDATE games SET end_time = ?, winner_uuid = ? WHERE id = ?";
        try (Connection conn = sqlite.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, System.currentTimeMillis());
            stmt.setString(2, winnerUuid != null ? winnerUuid.toString() : null);
            stmt.setInt(3, gameId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to end game: " + e.getMessage());
        }
    }

    public void close() {
        sqlite.close();
    }
}