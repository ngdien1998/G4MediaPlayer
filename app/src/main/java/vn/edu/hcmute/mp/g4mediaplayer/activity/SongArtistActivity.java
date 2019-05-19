package vn.edu.hcmute.mp.g4mediaplayer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import vn.edu.hcmute.mp.g4mediaplayer.R;
import vn.edu.hcmute.mp.g4mediaplayer.adapter.SongArtistAdapter;
import vn.edu.hcmute.mp.g4mediaplayer.common.Consts;
import vn.edu.hcmute.mp.g4mediaplayer.model.entity.Song;
import vn.edu.hcmute.mp.g4mediaplayer.model.service.ArtistService;

public class SongArtistActivity extends AppCompatActivity {

    private TextView txtNameOfArtist;
    private RecyclerView rVListSongOfArtist;
    private ArtistService service;
    private Button btnPlayAll;

    private ArrayList<Song> songs;
    private SongArtistAdapter songArtistAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_artist);
        txtNameOfArtist = this.findViewById(R.id.txtName_Artists);
        rVListSongOfArtist = this.findViewById(R.id.rcl_song_Artist);
        rVListSongOfArtist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rVListSongOfArtist.setHasFixedSize(true);

        Intent intent = this.getIntent();
        String idArtist = intent.getStringExtra("idArtist");
        String nameArtist = intent.getStringExtra("nameArtist");

        txtNameOfArtist.setText(nameArtist);

        try{
            service = new ArtistService(getApplicationContext());
            songs = service.getAllSongOfArtist(idArtist);

            songArtistAdapter = new SongArtistAdapter(getApplicationContext(), songs);
            songArtistAdapter.setOnItemClick(this::adapterOnItemClick);
            btnPlayAll = findViewById(R.id.btn_play_all);
            btnPlayAll.setOnClickListener(v -> {
                Intent playIntent = new Intent(getApplicationContext(), PlayCenterActivity.class);
                playIntent.putExtra(Consts.SONGS_EXTRA, songs);
                playIntent.putExtra(Consts.SONG_EXTRA, songs.get(0));
                playIntent.putExtra(Consts.SONG_POSITION_EXTRA, 0);

                startActivity(playIntent);
            });

            rVListSongOfArtist.setAdapter(songArtistAdapter);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void adapterOnItemClick(View view, Song song, int position) {
        Intent intent = new Intent(getApplicationContext(), PlayCenterActivity.class);
        intent.putExtra(Consts.SONGS_EXTRA, songs);
        intent.putExtra(Consts.SONG_EXTRA, song);
        intent.putExtra(Consts.SONG_POSITION_EXTRA, position);

        startActivity(intent);
    }

    private void playListes() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getApplicationContext()));
        dialogBuilder.setTitle("New name");

        dialogBuilder.setCancelable(true);


        dialogBuilder.show();
    }

    private void adapterSong_itemMoreClick(View view, Song song, int i, MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_play:
                playListes();
                break;
            case R.id.menu_item_rename:

                break;

        }
    }
}
