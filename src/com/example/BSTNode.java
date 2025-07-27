package com.example;

public class BSTNode {
    private Playlist playlist;
    private BSTNode left;
    private BSTNode right;

    public BSTNode(Playlist playlist) {
        this.playlist = playlist;
        this.left = null;
        this.right = null;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public BSTNode getLeft() {
        return left;
    }

    public void setLeft(BSTNode left) {
        this.left = left;
    }

    public BSTNode getRight() {
        return right;
    }

    public void setRight(BSTNode right) {
        this.right = right;
    }
}
