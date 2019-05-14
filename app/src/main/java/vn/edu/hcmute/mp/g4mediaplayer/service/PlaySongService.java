package vn.edu.hcmute.mp.g4mediaplayer.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.Random;

import vn.edu.hcmute.mp.g4mediaplayer.R;
import vn.edu.hcmute.mp.g4mediaplayer.common.Consts;
import vn.edu.hcmute.mp.g4mediaplayer.model.entity.Song;
import vn.edu.hcmute.mp.g4mediaplayer.model.service.ArtistService;

public class PlaySongService extends Service implements MusicControlClient {

    private final IBinder mBinder = new LocalBinder();

    private MediaPlayer mediaPlayer;
    private Song currentSong;
    private ArrayList<Song> songs;

    private boolean isShuffle = false;
    private final Random random = new Random();
    private final ArrayList<Integer> playedSongIds = new ArrayList<>();

    private int currentSongPosition;
    private int currentSongDuration = 0;

    private Handler seekHandler = new Handler();
    private Runnable playSongThread = this::runPlaySongThread;
    private NotificationManager notificationManager;
    ArtistService artistService;

    private static final int NOTIFICATION_ID = 11;
    public static final String NOTIFICATION_CHANNEL = "music_notification_channel";
    RemoteViews collapsedView;
    RemoteViews expandedView;

    private SeekSongListener onSeekSong;
    private PlayingSongChangedListener onPlayingSongChanged;

    public void setOnPlayingSongChanged(PlayingSongChangedListener onPlayingSongChanged) {
        this.onPlayingSongChanged = onPlayingSongChanged;
    }

    public void setOnSeekSong(SeekSongListener onSeekSong) {
        this.onSeekSong = onSeekSong;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            artistService = new ArtistService(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Handle when incoming call
        PhoneStateListener phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String phoneNumber) {
                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE:
                        playSong();
                        break;
                    case TelephonyManager.CALL_STATE_RINGING:
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        pausePlayingSong();
                }
                super.onCallStateChanged(state, phoneNumber);
            }
        };

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

        // Get data
        songs = (ArrayList<Song>) intent.getSerializableExtra(Consts.SONGS_EXTRA);
        currentSong = (Song) intent.getSerializableExtra(Consts.SONG_EXTRA);
        currentSongPosition = intent.getIntExtra(Consts.SONG_POSITION_EXTRA, 0);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(currentSong.getFilePath()));
        mediaPlayer.setOnCompletionListener(this::mediaPlayerOnCompletion);

        return START_NOT_STICKY;
    }

    private void mediaPlayerOnCompletion(MediaPlayer mediaPlayer) {
        if (!this.mediaPlayer.isLooping()) {
            if (isShuffle) {
                int randomIndex;
                do {
                    randomIndex = random.nextInt(songs.size());
                } while (playedSongIds.contains(randomIndex));

                currentSongPosition = randomIndex;
                playCurrentSong();

                // Phát quá 70% số lượng bài hát thì cho random lại trên toàn danh sách
                boolean mustReset = ((playedSongIds.size() * 100) / songs.size()) > 70;
                if (mustReset) {
                    playedSongIds.clear();
                }
            } else {
                playNextSong();
            }

            if (onPlayingSongChanged != null) {
                onPlayingSongChanged.onPlayingSongChanged(currentSongPosition);
            }
        }
    }

    private PendingIntent getClickableIntent(@IdRes int id) {
        Intent clickIntent = new Intent("click_intent");
        clickIntent.putExtra(Consts.BUTTON_CLICKED_ID, id);
        clickIntent.putExtra(Consts.SONGS_EXTRA, songs);
        clickIntent.putExtra(Consts.SONG_EXTRA, currentSong);
        clickIntent.putExtra(Consts.SONG_POSITION_EXTRA, currentSongPosition);

        return PendingIntent.getBroadcast(this, id, clickIntent, 0);
    }

    private void enableNotification() {
        try {
            cancelNotification();

            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            collapsedView = new RemoteViews(getPackageName(), R.layout.notification_collapsed);
            expandedView = new RemoteViews(getPackageName(), R.layout.notification_expanded);

            String songNameText = "Playing " + currentSong.getName();
            String artistText = artistService.getSongArtist(currentSong.getId());

            expandedView.setTextViewText(R.id.txt_song_name, songNameText);
            collapsedView.setTextViewText(R.id.txt_song_name, songNameText);
            expandedView.setTextViewText(R.id.txt_artist, artistText);
            collapsedView.setTextViewText(R.id.txt_artist, artistText);

            byte[] bytesOfImage = currentSong.getImage();
            if (bytesOfImage != null) {
                Bitmap imageBitmap = BitmapFactory.decodeByteArray(bytesOfImage, 0, bytesOfImage.length);

                expandedView.setImageViewBitmap(R.id.img_song, imageBitmap);
                collapsedView.setImageViewBitmap(R.id.img_song, imageBitmap);
            }

            expandedView.setOnClickPendingIntent(R.id.btn_play, getClickableIntent(R.id.btn_play));
            expandedView.setOnClickPendingIntent(R.id.btn_repeat, getClickableIntent(R.id.btn_repeat));
            expandedView.setOnClickPendingIntent(R.id.btn_prev, getClickableIntent(R.id.btn_prev));
            expandedView.setOnClickPendingIntent(R.id.btn_next, getClickableIntent(R.id.btn_next));

            Notification notification = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL)
                    .setSmallIcon(R.drawable.ic_play_circle)
                    .setCustomContentView(collapsedView)
                    .setCustomBigContentView(expandedView)
                    .build();

            notificationManager.notify(NOTIFICATION_ID, notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cancelNotification() {
        if (notificationManager != null) {
            notificationManager.cancel(NOTIFICATION_ID);
        }
    }

    @Override
    public void playSong() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            enableNotification();

            seekHandler.postDelayed(playSongThread, 0);
        }
    }

    @Override
    public void pausePlayingSong() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    public boolean isSongPlaying() {
        if (mediaPlayer != null) {
            return mediaPlayer.isPlaying();
        }
        return false;
    }

    @Override
    public int getCurrentSongDuration() {
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        }
        return -1;
    }

    @Override
    public int getTotalDuration() {
        if (mediaPlayer != null) {
            return mediaPlayer.getDuration();
        }
        return 0;
    }

    @Override
    public int playNextSong() {
        int nextIndex = ++currentSongPosition;
        if (nextIndex >= songs.size()) {
            nextIndex = currentSongPosition = 0;
        }

        playCurrentSong();

        return nextIndex;
    }

    private void playCurrentSong() {
        currentSong = songs.get(currentSongPosition);

        releasePlayer();
        enableNotification();

        mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(currentSong.getFilePath()));
        mediaPlayer.setOnCompletionListener(this::mediaPlayerOnCompletion);
        mediaPlayer.start();
    }

    private void releasePlayer() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public int playPreviousSong() {
        int previousIndex = --currentSongPosition;
        if (previousIndex < 0) {
            previousIndex = currentSongPosition = songs.size() - 1;
        }

        playCurrentSong();

        return previousIndex;
    }

    @Override
    public boolean isLoopingSong() {
        if (mediaPlayer != null) {
            return mediaPlayer.isLooping();
        }
        return false;
    }

    @Override
    public void setLoopingSong(boolean loop) {
        if (mediaPlayer != null) {
            mediaPlayer.setLooping(loop);
        }
    }

    @Override
    public boolean isShufflePlaying() {
        return isShuffle;
    }

    @Override
    public void setSufflePlaying(boolean shuffle) {
        isShuffle = shuffle;
    }

    @Override
    public void seekSongTo(int position) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(position);
            currentSongDuration = position;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private void runPlaySongThread() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            currentSongDuration = mediaPlayer.getCurrentPosition();
            if (onSeekSong != null) {
                onSeekSong.onSeek(currentSongDuration);
            }
        }
        seekHandler.postDelayed(playSongThread, 1000);
    }

    public class LocalBinder extends Binder {
        public PlaySongService getService() {
            return PlaySongService.this;
        }
    }

    public interface SeekSongListener {
        void onSeek(int postion);
    }

    public interface PlayingSongChangedListener {
        void onPlayingSongChanged(int newPosition);
    }
}