package vn.edu.hcmute.mp.g4mediaplayer.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import vn.edu.hcmute.mp.g4mediaplayer.R;
import vn.edu.hcmute.mp.g4mediaplayer.common.Consts;
import vn.edu.hcmute.mp.g4mediaplayer.model.entity.Song;
import vn.edu.hcmute.mp.g4mediaplayer.model.service.ArtistService;
import vn.edu.hcmute.mp.g4mediaplayer.service.PlaySongService;

public class PlayCenterActivity extends AppCompatActivity implements ServiceConnection {

    private ArtistService artistService;
    public static PlaySongService service;
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss", Locale.getDefault());

    ArrayList<Song> songs;
    Song currentSong;
    int currentSongPosition;

    ImageButton btnPlay, btnNext, btnPrev, btnRepeat, btnShuffle;
    SeekBar skbSong;
    TextView txtSongName, txtArtistName, txtCurrentSongTime, txtTotalSongTime;
    ImageView imgSongSmall, imgSongMain;

    Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_center);

        try {
            artistService = new ArtistService(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapControls();
        addEvents();

        getNavigatetedParameters();

        setupService();
    }

    private void setupService() {
        serviceIntent = new Intent(PlayCenterActivity.this, PlaySongService.class);
        serviceIntent.putExtra(Consts.SONGS_EXTRA, songs);
        serviceIntent.putExtra(Consts.SONG_EXTRA, currentSong);
        serviceIntent.putExtra(Consts.SONG_POSITION_EXTRA, currentSongPosition);

        startService(serviceIntent);
        bindService(serviceIntent, this, BIND_AUTO_CREATE);
    }

    private void serviceOnSeekSong(int position) {
        skbSong.setProgress(position);
        txtCurrentSongTime.setText(timeFormat.format(service.getCurrentSongDuration()));
    }

    private void mapControls() {
        btnPlay = findViewById(R.id.btn_play);
        btnNext = findViewById(R.id.btn_next);
        btnPrev = findViewById(R.id.btn_prev);
        skbSong = findViewById(R.id.skb_progress);
        btnRepeat = findViewById(R.id.btn_repeat);
        btnShuffle = findViewById(R.id.btn_shuffle);
        txtSongName = findViewById(R.id.txt_song_name);
        txtArtistName = findViewById(R.id.txt_artist);
        txtTotalSongTime = findViewById(R.id.txt_song_total_duration);
        txtCurrentSongTime = findViewById(R.id.txt_song_current_duration);
        imgSongMain = findViewById(R.id.img_song);
        imgSongSmall = findViewById(R.id.img_song_small);
    }

    private void addEvents() {
        btnPlay.setOnClickListener(this::btnPlayOnClick);
        skbSong.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    service.seekSongTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        btnNext.setOnClickListener(this::btnNextPrevOnClick);
        btnPrev.setOnClickListener(this::btnPrevOnClick);
        btnRepeat.setOnClickListener(this::btnRepeatOnClick);
        btnShuffle.setOnClickListener(this::btnShufleOnClick);
    }

    private void btnShufleOnClick(View view) {
        ImageButton btnShuffle = (ImageButton) view;
        if (service.isShufflePlaying()) {
            service.setSufflePlaying(false);
            btnShuffle.setImageResource(R.drawable.ic_shuffle_normal);
        } else {
            service.setSufflePlaying(true);
            btnShuffle.setImageResource(R.drawable.ic_shuffle_enable);
        }
    }

    private void btnRepeatOnClick(View view) {
        ImageButton btnRepeat = (ImageButton) view;
        if (service.isLoopingSong()) {
            service.setLoopingSong(false);
            btnRepeat.setImageResource(R.drawable.ic_repeat_normal);
        } else {
            service.setLoopingSong(true);
            btnRepeat.setImageResource(R.drawable.ic_repeat_one);
        }
    }

    private void btnPrevOnClick(View view) {
        currentSongPosition = service.playPreviousSong();
        currentSong = songs.get(currentSongPosition);

        setDisplayForCurrentSong();
    }

    private void btnNextPrevOnClick(View view) {
        currentSongPosition = service.playNextSong();
        currentSong = songs.get(currentSongPosition);

        setDisplayForCurrentSong();
    }

    private void btnPlayOnClick(View view) {
        ImageButton btnPlayPause = (ImageButton) view;
        if (service.isSongPlaying()) {
            service.pausePlayingSong();
            btnPlayPause.setImageResource(R.drawable.ic_play);
        } else {
            service.playSong();
            btnPlayPause.setImageResource(R.drawable.ic_pause);
        }
    }

    private void getNavigatetedParameters() {
        Intent intent = getIntent();
        onNewIntent(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        songs = (ArrayList<Song>) intent.getSerializableExtra(Consts.SONGS_EXTRA);
        currentSong = (Song) intent.getSerializableExtra(Consts.SONG_EXTRA);
        currentSongPosition = intent.getIntExtra(Consts.SONG_POSITION_EXTRA, 0);

        //setDisplayForCurrentSong();
    }

    private void setDisplayForCurrentSong() {
        txtSongName.setText(currentSong.getName());
        txtArtistName.setText(artistService.getSongArtist(currentSong.getId()));
        txtTotalSongTime.setText(timeFormat.format(service.getTotalDuration()));
        skbSong.setMax(service.getTotalDuration());

        byte[] imgBytes = currentSong.getImage();
        if (imgBytes != null) {
            Bitmap imgBitmap = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
            imgSongSmall.setImageBitmap(imgBitmap);
            imgSongMain.setImageBitmap(imgBitmap);
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        PlayCenterActivity.service = ((PlaySongService.LocalBinder) service).getService();
        startPlayingSong();
    }

    private void startPlayingSong() {
        btnPlay.setImageResource(R.drawable.ic_pause);

        int totalDuration = service.getTotalDuration();

        skbSong.setMax(totalDuration);
        txtCurrentSongTime.setText(timeFormat.format(0));
        txtTotalSongTime.setText(timeFormat.format(totalDuration));
        txtSongName.setText(currentSong.getName());
        txtArtistName.setText(artistService.getSongArtist(currentSong.getId()));

        service.setOnSeekSong(this::serviceOnSeekSong);
        service.setOnPlayingSongChanged(this::serviceOnPlayingSongChanged);

        service.playSong();
    }

    private void serviceOnPlayingSongChanged(int newPosition) {
        currentSongPosition = newPosition;
        currentSong = songs.get(currentSongPosition);

        txtTotalSongTime.setText(timeFormat.format(service.getTotalDuration()));
        skbSong.setMax(service.getTotalDuration());

        setDisplayForCurrentSong();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        service = null;
    }
}