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
import vn.edu.hcmute.mp.g4mediaplayer.model.entity.Song;
import vn.edu.hcmute.mp.g4mediaplayer.model.service.ArtistService;
import vn.edu.hcmute.mp.g4mediaplayer.model.service.PlaylistService;

public class PlaylistSong extends AppCompatActivity {
    private TextView txtNamePlaylist;
    private RecyclerView rVListSongPlaylist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_song);
        txtNamePlaylist = this.findViewById(R.id.txtName_Artists);
        rVListSongPlaylist = this.findViewById(R.id.rcl_song_list_artist);
        rVListSongPlaylist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rVListSongPlaylist.setHasFixedSize(true);

        Intent intent = this.getIntent();

        int idArtist = intent.getIntExtra("idArtist", 1);
        String nameArtist = intent.getStringExtra("nameArtist");

        txtNamePlaylist.setText(nameArtist);

        try{
            PlaylistService service = new PlaylistService(getApplicationContext());
         //   ArrayList<Song> songs = service.getAll();

          //  SongAdapter songAdapter = new SongAdapter(getApplicationContext(), songs);
//            songAdapter.setOnItemClickListener(this::adapterOnItemClick);
        //    rVListSongPlaylist.setAdapter(songAdapter);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
