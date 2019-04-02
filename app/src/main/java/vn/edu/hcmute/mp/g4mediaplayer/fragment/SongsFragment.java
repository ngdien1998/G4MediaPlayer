package vn.edu.hcmute.mp.g4mediaplayer.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.edu.hcmute.mp.g4mediaplayer.R;
import vn.edu.hcmute.mp.g4mediaplayer.adapter.SongAdapter;
import vn.edu.hcmute.mp.g4mediaplayer.utils.SongManager;

public class SongsFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_songs, container, false);

        RecyclerView rclSong = root.findViewById(R.id.rcl_song_list);
        rclSong.setLayoutManager(new LinearLayoutManager(getActivity()));
        rclSong.setHasFixedSize(true);

        SongAdapter adapter = new SongAdapter(getContext(), SongManager.loadSongs());
        rclSong.setAdapter(adapter);

        adapter.setOnItemClick((view, song, position) -> {
            // TODO: handle for song item click
        });

        adapter.setOnMoreItemClick((view, song, item) -> {
            // TODO: handle for song item more button click
        });

        return root;
    }
}