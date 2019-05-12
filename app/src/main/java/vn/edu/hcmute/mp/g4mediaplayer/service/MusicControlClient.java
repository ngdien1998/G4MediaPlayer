package vn.edu.hcmute.mp.g4mediaplayer.service;

public interface MusicControlClient {
    void playSong();
    void seekSongTo(int position);
    void pausePlayingSong();
    boolean isSongPlaying();
    int getCurrentSongDuration();
    int getTotalDuration();
    int playNextSong();
    int playPreviousSong();
    boolean isLoopingSong();
    void setLoopingSong(boolean loop);
    boolean isShufflePlaying();
    void setSufflePlaying(boolean shuffle);
}
