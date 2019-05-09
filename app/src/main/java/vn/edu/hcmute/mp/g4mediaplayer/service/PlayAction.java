package vn.edu.hcmute.mp.g4mediaplayer.service;

public interface PlayAction {
    void loadMedia(String url);
    void release();
    boolean isPlaying();
    void play();
    void reset();
    void pause();
    void seekTo(int position);
}
