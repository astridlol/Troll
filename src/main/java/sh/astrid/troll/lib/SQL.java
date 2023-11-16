package sh.astrid.troll.lib;

import sh.astrid.troll.Troll;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQL {
    private static final Connection conn;

    static {
        Troll plugin = Troll.getInstance();

        try {
            conn = DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder() + "/data.db");

            execute("""
            CREATE TABLE IF NOT EXISTS statistics(
            player_uuid TEXT NOT NULL PRIMARY KEY UNIQUE,
            trolls TEXT NOT NULL
           );
           """);
        } catch (SQLException e) {
            throw new RuntimeException("Error initializing SQLite connection", e);
        }
    }

    public static void execute(String sql, Object... values) throws SQLException {
        prepare(sql, values).execute();
    }

    public static ResultSet query(String sql, Object... values) throws SQLException {
        return prepare(sql, values).executeQuery();
    }

    public static void close() throws SQLException {
        conn.close();
    }

    private static PreparedStatement prepare(String sql, Object... values) {
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            for (int i = 0; i < values.length; i++) {
                stmt.setObject(i + 1, values[i]);
            }

            System.out.println("Executing SQL Query: " +stmt);

            return stmt;
        } catch (SQLException e) {
            throw new RuntimeException("Error preparing SQL statement", e);
        }
    }
}
