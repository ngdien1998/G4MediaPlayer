package vn.edu.hcmute.mp.g4mediaplayer.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;

import vn.edu.hcmute.mp.g4mediaplayer.R;
import vn.edu.hcmute.mp.g4mediaplayer.common.Consts;
import vn.edu.hcmute.mp.g4mediaplayer.model.entity.Song;
import vn.edu.hcmute.mp.g4mediaplayer.service.PlayMode;
import vn.edu.hcmute.mp.g4mediaplayer.service.PlaySongService;

public class PlayCenterActivity extends AppCompatActivity implements ServiceConnection {

    private PlaySongService playSongService;
    private boolean isBounded = false;

    private Song song;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_play_center);

            Intent intent = getIntent();
            Bundle bundle = intent.getBundleExtra(Consts.SONG_BUNDLE_EXTRA);
            song = (Song) bundle.getSerializable(Consts.SONG_EXTRA);

            Bundle serviceBundle = new Bundle();
            serviceBundle.putSerializable(Consts.SONG_EXTRA, song);
            serviceBundle.putSerializable(Consts.PLAY_MODE_EXTRA, PlayMode.DEFAULT);

            Intent serviceIntent = new Intent(PlayCenterActivity.this, PlaySongService.class);
            serviceIntent.putExtra(Consts.PLAY_SERVICE_BUNDLE_EXTRA, serviceBundle);


            bindService(serviceIntent, this, Context.BIND_AUTO_CREATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        PlaySongService.PlaySongServiceBinder serviceBinder = (PlaySongService.PlaySongServiceBinder) service;
        playSongService = serviceBinder.getService();
        playSongService.play();
        isBounded = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        isBounded = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(this);
    }
}