package vn.edu.hcmute.mp.g4mediaplayer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.ArrayList;

import vn.edu.hcmute.mp.g4mediaplayer.R;
import vn.edu.hcmute.mp.g4mediaplayer.activity.PlayCenterActivity;
import vn.edu.hcmute.mp.g4mediaplayer.adapter.SongAdapter;
import vn.edu.hcmute.mp.g4mediaplayer.common.Consts;
import vn.edu.hcmute.mp.g4mediaplayer.model.entity.Song;
import vn.edu.hcmute.mp.g4mediaplayer.model.service.SongService;

public class SongsFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_songs, container, false);

        RecyclerView rclSong = root.findViewById(R.id.rcl_song_list);
        rclSong.setLayoutManager(new LinearLayoutManager(getActivity()));
        rclSong.setHasFixedSize(true);

        try {
            SongService songService = new SongService(getContext());
            ArrayList<Song> songs = songService.getAll();
            SongAdapter adapter = new SongAdapter(getContext(), songs);
            rclSong.setAdapter(adapter);

            adapter.setOnItemClick(this::adapterSong_itemClick);
            adapter.setOnMoreItemClick(this::adapterSong_itemMoreClick);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return root;
    }

    private void adapterSong_itemClick(View view, Song song, int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Consts.SONG_EXTRA, song);

        Intent playIntent = new Intent(getActivity(), PlayCenterActivity.class);
        playIntent.putExtra(Consts.SONG_BUNDLE_EXTRA, bundle);
        startActivity(playIntent);
    }

    private void adapterSong_itemMoreClick(View view, Song song, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_play:
                adapterSong_itemClick(view, song, 0);
                break;
            case R.id.action_playlist:
               // showPopup();
                break;
            case R.id.action_queue:

                break;
            case R.id.action_delete:

                break;
        }
    }
}