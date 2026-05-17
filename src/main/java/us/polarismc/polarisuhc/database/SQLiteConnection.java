package us.polarismc.polarisuhc.database;

import com.zaxxer.hikari.HikariDataSource;
import us.polarismc.polarisuhc.Main;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteConnection {
    private final HikariDataSource dataSource;
    private final Main plugin;

    public SQLiteConnection(Main plugin) {
        this.plugin = plugin;
        HikariDataSource ds = new HikariDataSource();
        File dataDir = new File(plugin.getDataFolder(), "..");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        File dbFile = new File(dataDir, "statistics.db");
        ds.setJdbcUrl("jdbc:sqlite:" + dbFile.getAbsolutePath());
        ds.setMaximumPoolSize(1);
        ds.setPoolName("SQLitePool");
        this.dataSource = ds;
        initializeTables();
    }

    private void initializeTables() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS games (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "host_uuid TEXT NOT NULL," +
                "start_time INTEGER NOT NULL," +
                "end_time INTEGER," +
                "winner_uuid TEXT" +
                ")");
            stmt.execute("CREATE TABLE IF NOT EXISTS player_stats (" +
                "uuid TEXT NOT NULL," +
                "game_id INTEGER NOT NULL," +
                "name TEXT NOT NULL," +
                "kills INTEGER DEFAULT 0," +
                "deaths INTEGER DEFAULT 0," +
                "diamonds_mined INTEGER DEFAULT 0," +
                "is_winner INTEGER DEFAULT 0," +
                "PRIMARY KEY (uuid, game_id)," +
                "FOREIGN KEY (game_id) REFERENCES games(id)" +
                ")");
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to initialize database tables: " + e.getMessage());
            throw new RuntimeException("Database initialization failed", e);
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void close() {
        dataSource.close();
    }
}