package vn.edu.hcmute.mp.g4mediaplayer.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import vn.edu.hcmute.mp.g4mediaplayer.R;
import vn.edu.hcmute.mp.g4mediaplayer.activity.PlayCenterActivity;
import vn.edu.hcmute.mp.g4mediaplayer.common.Consts;
import vn.edu.hcmute.mp.g4mediaplayer.service.PlaySongService;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        PlaySongService service = PlayCenterActivity.service;

        int currentPosition;

        int id = intent.getIntExtra(Consts.BUTTON_CLICKED_ID, -1);

        switch (id) {
            case R.id.btn_play:
                service.pausePlayingSong();
                break;
            case R.id.btn_next:
                service.playNextSong();
                break;
            case R.id.btn_prev:
                service.playPreviousSong();
                break;
        }
    }
}