package com.bestproject.main.Databases;

import java.sql.*;

public class PlayerInfoDatabase {
    private Connection connection;
    private static final String DATABASE_NAME = "player_database.db";
    private static final String TABLE_NAME = "characters";
    public PlayerInfoDatabase() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_NAME);
            createTable();
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error connecting to the database.");
            e.printStackTrace();
        }
    }
    private void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "characterid INTEGER UNIQUE, " +
            "characterinfo TEXT)";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }
    public void updateCharacterInfo(int characterid, String info) {
        String checkSql = "SELECT characterid FROM " + TABLE_NAME + " WHERE characterid = ?";
        String updateSql = "UPDATE " + TABLE_NAME + " SET characterinfo = ? WHERE characterid = ?";
        String insertSql = "INSERT INTO " + TABLE_NAME + " (characterid, characterinfo) VALUES (?, ?)";

        try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            checkStmt.setInt(1, characterid);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                    updateStmt.setString(1, info);
                    updateStmt.setInt(2, characterid);
                    updateStmt.executeUpdate();
                }
            } else {
                try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
                    insertStmt.setInt(1, characterid);
                    insertStmt.setString(2, info);
                    insertStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.err.println("Error updating character info.");
            e.printStackTrace();
        }
    }
    public String getCharacterInfo(int characterid) {
        String sql = "SELECT characterinfo FROM " + TABLE_NAME + " WHERE characterid = ?";
        String characterInfo = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, characterid);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                characterInfo = rs.getString("characterinfo");
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving character info.");
            e.printStackTrace();
        }
        return characterInfo;
    }
    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing the database connection.");
            e.printStackTrace();
        }
    }

}
