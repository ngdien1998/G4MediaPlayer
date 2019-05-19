package vn.edu.hcmute.mp.g4mediaplayer.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import vn.edu.hcmute.mp.g4mediaplayer.common.Consts;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent forwardIntent = new Intent(Consts.ACTION_RECEIVE_NOTIFICATION_COMMAND);
        forwardIntent.putExtra(Consts.BUTTON_CLICKED_ID, intent.getIntExtra(Consts.BUTTON_CLICKED_ID, -1));

        LocalBroadcastManager.getInstance(context).sendBroadcast(forwardIntent);
    }
}