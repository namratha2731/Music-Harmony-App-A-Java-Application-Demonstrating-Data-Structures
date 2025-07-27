package com.example;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class MusicPlayer {
    private Queue<Song> songQueue = new LinkedList<>();
    private MediaPlayer mediaPlayer;
    private Song currentSong;
    private double songPosition;
    private boolean isPlaying = false;

    // Method to load songs from the database
    public void loadSongsFromDatabase() {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM songs")) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String artist = resultSet.getString("artist");
                String filePath = resultSet.getString("file_path");
                double duration = resultSet.getDouble("duration");

                Song song = new Song(id, title, artist, filePath, duration);
                songQueue.add(song);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void playSong(Song song) {
        currentSong = song;
        String songPath = new File(song.getFilePath()).toURI().toString();
        System.out.println("Attempting to play song from path: " + songPath); // Print the file path
        Media media = new Media(songPath);
        mediaPlayer = new MediaPlayer(media);
    
        mediaPlayer.setOnReady(() -> {
            System.out.println("Playing song: " + song.getTitle() + " by " + song.getArtist());
            mediaPlayer.play();
            isPlaying = true;
        });
    
        mediaPlayer.setOnEndOfMedia(() -> {
            isPlaying = false;
            skipSong();
        });
    
        mediaPlayer.setOnError(() -> {
            System.err.println("Error occurred while playing song: " + mediaPlayer.getError().getMessage());
        });
    }    

    public void resumeSong() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
            isPlaying = true;
            System.out.println("Resuming song: " + currentSong.getTitle() + " by " + currentSong.getArtist());
        }
    }

    public void pauseSong() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            isPlaying = false;
            System.out.println("Pausing song: " + currentSong.getTitle());
        }
    }

    public void skipSong() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        if (!songQueue.isEmpty()) {
            playSong(songQueue.poll());
        } else {
            System.out.println("No more songs in the queue.");
            isPlaying = false;
        }
    }

    public void queueSong(Song song) {
        songQueue.add(song);
        System.out.println("Queued song: " + song.getTitle() + " by " + song.getArtist());
    }

    // Method to seek to a specific position within the current song
    public void seekSong(double positionInSeconds) {
        if (mediaPlayer != null) {
            mediaPlayer.seek(Duration.seconds(positionInSeconds));
            songPosition = positionInSeconds;
            System.out.println("Seeking to position: " + songPosition + " seconds");
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public double getCurrentTime() {
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentTime().toSeconds();
        }
        return 0.0;
    }

    public double getDuration() {
        if (currentSong != null) {
            return currentSong.getDuration();
        }
        return 0.0;
    }
}
