package vn.edu.hcmute.mp.g4mediaplayer.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;

import vn.edu.hcmute.mp.g4mediaplayer.R;
import vn.edu.hcmute.mp.g4mediaplayer.adapter.SongAdapter;
import vn.edu.hcmute.mp.g4mediaplayer.model.entity.Artist;
import vn.edu.hcmute.mp.g4mediaplayer.model.entity.Song;
import vn.edu.hcmute.mp.g4mediaplayer.model.service.ArtistService;

public class SongOfArtistsActivity extends AppCompatActivity {

    private TextView txtNameOfArtist;
    private RecyclerView rVListSongOfArtist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_of_artists);
        txtNameOfArtist = this.findViewById(R.id.txtName_Artists);
        rVListSongOfArtist = this.findViewById(R.id.rcl_song_list_artist);
        rVListSongOfArtist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rVListSongOfArtist.setHasFixedSize(true);

        Intent intent = this.getIntent();

        int idArtist = intent.getIntExtra("idArtist", 1);
        String nameArtist = intent.getStringExtra("nameArtist");

        txtNameOfArtist.setText(nameArtist);

        try{
            ArtistService service = new ArtistService(getApplicationContext());
            ArrayList<Song> songs = service.getAllSongOfArtist(idArtist);

            SongAdapter songAdapter = new SongAdapter(getApplicationContext(), songs);
//            songAdapter.setOnItemClickListener(this::adapterOnItemClick);
            rVListSongOfArtist.setAdapter(songAdapter);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
