package vn.edu.hcmute.mp.g4mediaplayer.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import vn.edu.hcmute.mp.g4mediaplayer.R;
import vn.edu.hcmute.mp.g4mediaplayer.adapter.SongAdapter;
import vn.edu.hcmute.mp.g4mediaplayer.adapter.SongPlaylistAdapter;
import vn.edu.hcmute.mp.g4mediaplayer.common.Consts;
import vn.edu.hcmute.mp.g4mediaplayer.model.entity.PlayList;
import vn.edu.hcmute.mp.g4mediaplayer.model.entity.Song;
import vn.edu.hcmute.mp.g4mediaplayer.model.service.ArtistService;
import vn.edu.hcmute.mp.g4mediaplayer.model.service.PlaylistService;

public class PlaylistSongActivity extends AppCompatActivity {
    private TextView txtNamePlaylist;
    private RecyclerView rclSongPlaylist;
    private ArrayList<Song> playLists;
    private SongPlaylistAdapter adapter;
    private  PlaylistService  service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_song);
        PlayList playList = (PlayList) getIntent().getSerializableExtra(Consts.PLAY_LIST);

        txtNamePlaylist = findViewById(R.id.txtName_Playlist);
        rclSongPlaylist = findViewById(R.id.rcl_song_Playlist);
        rclSongPlaylist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rclSongPlaylist.setHasFixedSize(true);

        try {
            service = new PlaylistService(getApplicationContext());
            playLists = service.getSongList(playList.getId());

            adapter = new SongPlaylistAdapter(getApplicationContext(), playLists);

            rclSongPlaylist.setAdapter(adapter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}