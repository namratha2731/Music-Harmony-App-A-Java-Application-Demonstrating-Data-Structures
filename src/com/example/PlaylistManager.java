package com.example;

import java.util.HashMap;
import java.util.Map;

public class PlaylistManager {
    private Map<String, Playlist> playlists;

    public PlaylistManager() {
        playlists = new HashMap<>();
    }

    public void addPlaylist(Playlist playlist) {
        playlists.put(playlist.getName(), playlist);
    }

    public Playlist getPlaylist(String name) {
        return playlists.get(name);
    }

    public void updatePlaylist(Playlist playlist) {
        playlists.put(playlist.getName(), playlist);
    }
}
