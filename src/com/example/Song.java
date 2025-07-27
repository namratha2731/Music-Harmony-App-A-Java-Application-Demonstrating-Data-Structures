package com.example;

public class Song {
    private int id;
    private String title;
    private String artist;
    private String filePath;
    private double duration;

    public Song(int id, String title, String artist, String filePath, double duration) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.filePath = filePath;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getFilePath() {
        return filePath;
    }

    public double getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return title + " - " + artist;
    }
}
