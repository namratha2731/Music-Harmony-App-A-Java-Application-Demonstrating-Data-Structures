package com.example;

import javafx.application.Application;
import javafx.stage.Stage;

public class MusicHarmonyApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        DatabaseManager.initializeDatabase();
        new SignupPane(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
