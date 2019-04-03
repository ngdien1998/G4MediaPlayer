package vn.edu.hcmute.mp.g4mediaplayer.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Objects;

import vn.edu.hcmute.mp.g4mediaplayer.R;
import vn.edu.hcmute.mp.g4mediaplayer.adapter.SongAdapter;
import vn.edu.hcmute.mp.g4mediaplayer.utils.SongManager;

public class SongsFragment extends Fragment {

    private View root;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_songs, container, false);

        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()),
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            loadResource();
        } else {
            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        return root;
    }

    private void loadResource() {
        RecyclerView rclSong = root.findViewById(R.id.rcl_song_list);
        rclSong.setLayoutManager(new LinearLayoutManager(getActivity()));
        rclSong.setHasFixedSize(true);

        SongAdapter adapter = new SongAdapter(getContext(), SongManager.loadSongs());
        rclSong.setAdapter(adapter);

        adapter.setOnItemClick((view, song, position) -> {
            MediaPlayer player = MediaPlayer.create(getContext(), Uri.parse(song.getFilePath()));
            player.start();
        });

        adapter.setOnMoreItemClick((view, song, item) -> {
            switch (item.getItemId()) {
                case R.id.action_play:

                    break;
                case R.id.action_playlist:

                    break;
                case R.id.action_queue:

                    break;
                case R.id.action_delete:

                    break;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadResource();
        } else {
            Toast.makeText(getActivity(), "You doesn't allow permission, so you can't see your song.", Toast.LENGTH_LONG).show();
        }
    }
}