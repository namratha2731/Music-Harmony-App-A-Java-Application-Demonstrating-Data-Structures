package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:music_harmony.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initializeDatabase() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT NOT NULL UNIQUE," +
                "password TEXT NOT NULL);";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(createTableSQL);

            String createSongsTable = "CREATE TABLE IF NOT EXISTS songs (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "title TEXT NOT NULL," +
                    "artist TEXT NOT NULL," +
                    "file_path TEXT NOT NULL," +
                    "duration REAL NOT NULL);";

            statement.execute(createSongsTable);

            String checkData = "SELECT COUNT(*) AS count FROM songs";
            ResultSet resultSet = statement.executeQuery(checkData);
            int count = resultSet.getInt("count");

            if (count == 0) {
                String insertSampleData = "INSERT INTO songs (title, artist, file_path, duration) VALUES (?, ?, ?, ?)";

                PreparedStatement preparedStatement = connection.prepareStatement(insertSampleData);

                // Sample data entries
                String[][] sampleData = {
                    {"One Man Les", "Artist 1", "C:\\Users\\samee\\Downloads\\FamousEnglishSongs\\One Man Les.mp3", "210.0"},
                    {"The Teddy Bears", "Artist 2", "C:\\Users\\samee\\Downloads\\FamousEnglishSongs\\The Teddy Bears.mp3", "180.0"},
                    {"One Man Disney", "Artist 1", "C:\\Users\\samee\\Downloads\\FamousEnglishSongs\\One Man Disney.mp3" , "210.0"},
                    {"More In Love With You", "Artist 2", "C:\\Users\\samee\\Downloads\\FamousEnglishSongs\\More In Love With You.mp3", "180.0"},
                    {"A Thousand Years", "Artist 1", "C:\\Users\\samee\\Downloads\\FamousEnglishSongs\\A Thousand Years.mp3", "210.0"},
                    {"Let Me Go", "Artist 2", "C:\\Users\\samee\\Downloads\\FamousEnglishSongs\\Let Me Go.mp3", "180.0"},
                    {"When I’m Gone", "Artist 1", "C:\\Users\\samee\\Downloads\\FamousEnglishSongs\\When I’m Gone.mp3", "210.0"},
                    {"I See Fire", "Artist 2", "C:\\Users\\samee\\Downloads\\FamousEnglishSongs\\The Teddy Bears.mp3", "180.0"},
                    {"The Civil Wars", "Artist 1", "C:\\Users\\samee\\Downloads\\FamousEnglishSongs\\One Man Les.mp3", "210.0"},
                    {"That’s Me Right There", "Artist 2", "C:\\Users\\samee\\Downloads\\FamousEnglishSongs\\The Teddy Bears.mp3", "180.0"},
                    {"Radio active", "Artist 1", "C:\\Users\\samee\\Downloads\\FamousEnglishSongs\\One Man Les.mp3", "210.0"},
                    {"Give Me Love", "Artist 2", "C:\\Users\\samee\\Downloads\\FamousEnglishSongs\\The Teddy Bears.mp3", "180.0"},
                    {"Dance Like Ciara", "Artist 1", "C:\\Users\\samee\\Downloads\\FamousEnglishSongs\\One Man Les.mp3", "210.0"},
                    {"Cry Me A River", "Artist 2", "C:\\Users\\samee\\Downloads\\FamousEnglishSongs\\The Teddy Bears.mp3", "180.0"},
                    {"Girl In A Country", "Artist 1", "C:\\Users\\samee\\Downloads\\FamousEnglishSongs\\One Man Les.mp3", "210.0"},
                    {"Do It Again", "Artist 2", "C:\\Users\\samee\\Downloads\\FamousEnglishSongs\\The Teddy Bears.mp3", "180.0"}
                };

                for (String[] data : sampleData) {
                    preparedStatement.setString(1, data[0]);
                    preparedStatement.setString(2, data[1]);
                    preparedStatement.setString(3, data[2]);
                    preparedStatement.setDouble(4, Double.parseDouble(data[3]));
                    preparedStatement.addBatch();
                }

                preparedStatement.executeBatch();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
