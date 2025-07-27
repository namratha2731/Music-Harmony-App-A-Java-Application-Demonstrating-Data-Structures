package com.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainPane {
    private MusicPlayer musicPlayer = new MusicPlayer();
    private List<Song> songs = new ArrayList<>();
    private ObservableList<String> songTitles = FXCollections.observableArrayList();
    private ListView<String> songListView = new ListView<>(songTitles);
    private Slider playBar = new Slider();
    private Label currentTimeLabel = new Label("0:00");
    private Label durationLabel = new Label("0:00");
    private PlaylistPane playlistPane = new PlaylistPane();
    private Timeline timeline;

    public MainPane(Stage primaryStage) {
        BorderPane root = new BorderPane();

        // Create and configure UI components
        Label titleLabel = new Label("Music Harmony");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-padding: 1px;");

        // Create search bar
        TextField searchField = new TextField();
        searchField.setPromptText("Search for songs or playlists...");
        searchField.textProperty().addListener((obs, oldText, newText) -> filterSongs(newText));

        // Create song controls
        Button playButton = new Button("Play");
        Button pauseButton = new Button("Pause");
        Button skipButton = new Button("Skip");
        Button queueButton = new Button("Queue");

        HBox controls = new HBox(10, playButton, pauseButton, skipButton, queueButton);
        controls.setPadding(new Insets(10));

        // Create song list view
        VBox songLayout = new VBox(10, titleLabel, searchField, songListView);
        songLayout.setPadding(new Insets(10)); // Add padding to the left side

        // Create bottom controls
        VBox bottomControls = new VBox(10, new HBox(10, currentTimeLabel, durationLabel), playBar);
        bottomControls.setAlignment(Pos.CENTER); // Align controls to the center

        // Configure root layout
        root.setLeft(songLayout); // Place song views on the left
        root.setBottom(bottomControls); // Place play bar and controls at the bottom middle
        root.setRight(playlistPane); // Place playlist pane on the right
        root.setCenter(controls); // Set the controls in the center of the BorderPane

        // Configure ListView
        songListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        songListView.setItems(songTitles); // Set the items for the ListView

        // Add event handlers
        playButton.setOnAction(e -> playSelectedSong());
        pauseButton.setOnAction(e -> musicPlayer.pauseSong());
        skipButton.setOnAction(e -> musicPlayer.skipSong());
        queueButton.setOnAction(e -> queueSelectedSong());

        // Playbar value change event
        playBar.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && playBar.isValueChanging()) { // Check if the value is changing by user interaction
                musicPlayer.seekSong(newVal.doubleValue());
            }
        });

        // Update slider and time labels periodically to reflect song progress
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            if (musicPlayer.isPlaying()) {
                double currentTime = musicPlayer.getCurrentTime();
                if (!playBar.isValueChanging()) { // Only update the slider if the user is not interacting with it
                    playBar.setValue(currentTime);
                }
                currentTimeLabel.setText(formatTime(currentTime));
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        primaryStage.setOnCloseRequest(e -> {
            if (timeline != null) {
                timeline.stop(); // Stop the timeline to prevent memory leaks
            }
        });

        // Load songs from the database
        loadSongsFromDatabase();

        Scene scene = new Scene(root, 1000, 800);
        primaryStage.setTitle("Music Harmony");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadSongsFromDatabase() {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM songs")) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String artist = resultSet.getString("artist");
                String filePath = resultSet.getString("file_path");
                double duration = resultSet.getDouble("duration"); // Ensure this column exists in your database

                Song song = new Song(id, title, artist, filePath, duration);
                songs.add(song);
                // Add the song title to the observable list
                songTitles.add(title + " - " + artist);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void playSelectedSong() {
        int selectedIndex = songListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Song selectedSong = songs.get(selectedIndex);
            musicPlayer.playSong(selectedSong);
            durationLabel.setText(formatTime(selectedSong.getDuration()));
            playBar.setMax(selectedSong.getDuration());
        }
    }

    private void queueSelectedSong() {
        int selectedIndex = songListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Song selectedSong = songs.get(selectedIndex);
            musicPlayer.queueSong(selectedSong);
        }
    }

    private void filterSongs(String query) {
        if (query == null || query.isEmpty()) {
            songListView.setItems(songTitles);
            playlistPane.filterPlaylists(""); // Update playlist pane to show all playlists
            return;
        }
    
        ObservableList<String> filteredList = FXCollections.observableArrayList();
        for (String title : songTitles) {
            if (title.toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(title);
            }
        }
        songListView.setItems(filteredList);
        playlistPane.filterPlaylists(query); // Update playlist pane to show filtered playlists
    }

    private String formatTime(double seconds) {
        int minutes = (int) seconds / 60;
        int remainderSeconds = (int) seconds % 60;
        return String.format("%d:%02d", minutes, remainderSeconds);
    }
}
