package vn.edu.hcmute.mp.g4mediaplayer.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import vn.edu.hcmute.mp.g4mediaplayer.common.Consts;
import vn.edu.hcmute.mp.g4mediaplayer.model.entity.Song;

public class PlaySongService extends Service implements PlayAction {

    private MediaPlayer mediaPlayer;
    private PlayMode playMode;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Bundle bundle = intent.getBundleExtra(Consts.PLAY_SERVICE_BUNDLE_EXTRA);
        Song song = (Song) bundle.getSerializable(Consts.SONG_EXTRA);
        playMode = (PlayMode) bundle.getSerializable(Consts.PLAY_MODE_EXTRA);
        assert song != null;
        loadMedia(song.getFilePath());
        return new PlaySongServiceBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Toast.makeText(getBaseContext(), "On unbind", Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public void loadMedia(String url) {
        mediaPlayer = MediaPlayer.create(getBaseContext(), Uri.parse(url));
        if (playMode != null && playMode == PlayMode.LOOP_INFINITE) {
            mediaPlayer.setLooping(true);
        }
    }

    @Override
    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public boolean isPlaying() {
        if (mediaPlayer != null) {
            return mediaPlayer.isPlaying();
        }
        return false;
    }

    @Override
    public void play() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    @Override
    public void reset() {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
        }
    }

    @Override
    public void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    public void seekTo(int position) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(position);
        }
    }

    public class PlaySongServiceBinder extends Binder {
        public PlaySongService getService() {
            return PlaySongService.this;
        }
    }
}