package com.example;

public class PlaylistBST {
    private BSTNode root;

    public PlaylistBST() {
        this.root = null;
    }

    public void addPlaylist(Playlist playlist) {
        root = addRecursive(root, playlist);
    }

    private BSTNode addRecursive(BSTNode current, Playlist playlist) {
        if (current == null) {
            return new BSTNode(playlist);
        }

        int comparison = playlist.getName().compareTo(current.getPlaylist().getName());
        if (comparison < 0) {
            current.setLeft(addRecursive(current.getLeft(), playlist));
        } else if (comparison > 0) {
            current.setRight(addRecursive(current.getRight(), playlist));
        }

        return current;
    }

    public Playlist getPlaylist(String name) {
        return getRecursive(root, name);
    }

    private Playlist getRecursive(BSTNode current, String name) {
        if (current == null) {
            return null;
        }

        int comparison = name.compareTo(current.getPlaylist().getName());
        if (comparison == 0) {
            return current.getPlaylist();
        } 
        return comparison < 0
            ? getRecursive(current.getLeft(), name)
            : getRecursive(current.getRight(), name);
    }
}
