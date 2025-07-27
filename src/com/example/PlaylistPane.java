package com.example;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class PlaylistPane extends VBox {
    private PlaylistManager playlistManager;
    private MediaPlayer mediaPlayer;
    private ListView<String> playlistView;
    private ObservableList<String> allPlaylistNames = FXCollections.observableArrayList(); 
    public PlaylistPane() {
        playlistManager = new PlaylistManager();

        TextField playlistNameField = new TextField();
        playlistNameField.setPromptText("Enter Playlist Name");

        ListView<Song> songListView = new ListView<>();
        List<Song> allSongs = loadSongsFromDatabase(); 
        songListView.getItems().addAll(allSongs);

        Label selectSongsLabel = new Label("Select Songs:");

        ListView<CheckBox> checkboxListView = new ListView<>();
        ObservableList<CheckBox> checkboxes = FXCollections.observableArrayList();

        for (Song song : allSongs) {
            CheckBox checkbox = new CheckBox(song.getTitle() + " - " + song.getArtist());
            checkboxes.add(checkbox);
        }

        checkboxListView.setItems(checkboxes);
        checkboxListView.setPrefHeight(200); 

        Button addPlaylistButton = new Button("Add Playlist");
        Button updatePlaylistButton = new Button("Update Playlist");
        playlistView = new ListView<>(); 

        addPlaylistButton.setOnAction(event -> {
            String playlistName = playlistNameField.getText();
            Playlist playlist = new Playlist(playlistName);
        
            for (int i = 0; i < checkboxes.size(); i++) {
                if (checkboxes.get(i).isSelected()) {
                    playlist.addSong(allSongs.get(i));
                }
            }
        
            playlistManager.addPlaylist(playlist);
            playlistView.getItems().add(playlistName);
            allPlaylistNames.add(playlistName);
            playlistNameField.clear();
            checkboxes.forEach(checkbox -> checkbox.setSelected(false));
        });
        
        updatePlaylistButton.setOnAction(event -> {
            String selectedPlaylistName = playlistView.getSelectionModel().getSelectedItem();
            if (selectedPlaylistName != null) {
                Playlist selectedPlaylist = playlistManager.getPlaylist(selectedPlaylistName);
                
                // Clear existing songs from the playlist
                selectedPlaylist.clearSongs();
                
                // Add selected songs to the playlist
                for (int i = 0; i < checkboxes.size(); i++) {
                    if (checkboxes.get(i).isSelected()) {
                        selectedPlaylist.addSong(allSongs.get(i));
                    }
                }
                
                // Update playlist in PlaylistManager
                playlistManager.updatePlaylist(selectedPlaylist);
                ObservableList<Song> playlistSongs = FXCollections.observableArrayList(selectedPlaylist.getSongs());
                songListView.setItems(playlistSongs);
            }
        });
        
        playlistView.setOnMouseClicked(event -> {
            String selectedPlaylistName = playlistView.getSelectionModel().getSelectedItem();
            if (selectedPlaylistName != null) {
                Playlist selectedPlaylist = playlistManager.getPlaylist(selectedPlaylistName);
                ObservableList<Song> playlistSongs = FXCollections.observableArrayList(selectedPlaylist.getSongs());
                songListView.setItems(playlistSongs);
            }
        });

        Button playButton = new Button("Play");
        Button stopButton = new Button("Stop");

        playButton.setOnAction(event -> {
            Song selectedSong = songListView.getSelectionModel().getSelectedItem();
            if (selectedSong != null) {
                playSong(selectedSong.getFilePath());
            }
        });

        stopButton.setOnAction(event -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
        });

        getChildren().addAll(playlistNameField, selectSongsLabel, checkboxListView, addPlaylistButton, updatePlaylistButton, playlistView, playButton, stopButton, songListView);
        VBox.setVgrow(checkboxListView, Priority.ALWAYS);
        VBox.setVgrow(songListView, Priority.ALWAYS);
    }

    public void filterPlaylists(String query) {
        if (query == null || query.isEmpty()) {
            playlistView.setItems(allPlaylistNames);
            return;
        }
    
        ObservableList<String> filteredList = FXCollections.observableArrayList();
        for (String playlistName : allPlaylistNames) {
            if (playlistName.toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(playlistName);
            }
        }
        playlistView.setItems(filteredList);
    }

    private List<Song> loadSongsFromDatabase() {
        List<Song> songs = new ArrayList<>();

        try {
            Connection connection = DatabaseManager.getConnection();
            String query = "SELECT * FROM songs";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String artist = resultSet.getString("artist");
                String filePath = resultSet.getString("file_path");
                double duration = resultSet.getDouble("duration");

                Song song = new Song(id, title, artist, filePath, duration);
                songs.add(song);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error loading songs from the database: " + e.getMessage());
        }

        return songs;
    }

    private void playSong(String filePath) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }

        try {
            URI uri = new URI("file:///" + filePath.replace("\\", "/").replace(" ", "%20"));
            Media media = new Media(uri.toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.err.println("Error playing song: " + e.getMessage());
        }
    }
}
