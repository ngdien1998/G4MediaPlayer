package vn.edu.hcmute.mp.g4mediaplayer.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.util.Strings;
import android.widget.EditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.ArrayList;

import vn.edu.hcmute.mp.g4mediaplayer.R;
import vn.edu.hcmute.mp.g4mediaplayer.activity.PlayCenterActivity;
import vn.edu.hcmute.mp.g4mediaplayer.adapter.PlayListAdapter;
import vn.edu.hcmute.mp.g4mediaplayer.adapter.SongAdapter;
import vn.edu.hcmute.mp.g4mediaplayer.common.Consts;
import vn.edu.hcmute.mp.g4mediaplayer.model.entity.PlayList;
import vn.edu.hcmute.mp.g4mediaplayer.model.entity.Song;
import vn.edu.hcmute.mp.g4mediaplayer.model.service.PlaylistService;
import vn.edu.hcmute.mp.g4mediaplayer.model.service.SongService;

public class SongsFragment extends Fragment {

    private EditText edtTitle;
    private TextInputLayout tilTitle;
    private ArrayList<Song> songs;
    private  ArrayList<PlayList> playLists;
    private PlaylistService playlistService;
    SongService songService;
    SongAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_songs, container, false);

        RecyclerView rclSong = root.findViewById(R.id.rcl_song_list);
        rclSong.setLayoutManager(new LinearLayoutManager(getActivity()));
        rclSong.setHasFixedSize(true);

        try {
            //Get song
            SongService songService = new SongService(getContext());
            songService = new SongService(getContext());
            songs = songService.getAll();
            adapter = new SongAdapter(getContext(), songs);
            rclSong.setAdapter(adapter);

            //Get Playlist
            playlistService = new PlaylistService(getContext());
            playLists = playlistService.getAll();


            // control buttotn
            adapter.setOnItemClick(this::adapterSong_itemClick);
            adapter.setOnMoreItemClick(this::adapterSong_itemMoreClick);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return root;
    }

    private void adapterSong_itemClick(View view, Song song, int position) {
        Intent playIntent = new Intent(getActivity(), PlayCenterActivity.class);
        playIntent.putExtra(Consts.SONGS_EXTRA, songs);
        playIntent.putExtra(Consts.SONG_EXTRA, song);
        playIntent.putExtra(Consts.SONG_POSITION_EXTRA, position);

        startActivity(playIntent);
    }

    private void adapterSong_itemMoreClick(View view, Song song, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_play:
                adapterSong_itemClick(view, song, 0);
                break;
            case R.id.action_playlist:
                showPopup(song);
                break;
            case R.id.action_queue:

                break;
            case R.id.action_rename:
                LayoutInflater inflater = getLayoutInflater();

                @SuppressLint("InflateParams")
                View enterTitleDialog = inflater.inflate(R.layout.layout_dialog, null);

                tilTitle = enterTitleDialog.findViewById(R.id.tilTitle);
                edtTitle = enterTitleDialog.findViewById(R.id.edtTitle);
                edtTitle.setText(song.getName());

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                dialogBuilder.setTitle("Enter playlist's name");
                dialogBuilder.setView(enterTitleDialog);
                dialogBuilder.setCancelable(true);
                dialogBuilder.setNegativeButton("Cancel", ((dialog, which) -> dialog.cancel()));
                dialogBuilder.setPositiveButton("Save", (dialog, which) -> {
                    Song newsong = new Song();
                    newsong.setName(edtTitle.getText().toString());
                    songService.edit(song, newsong);

                    songs.clear();
                    songs.addAll(songService.getAll());
                    adapter.notifyDataSetChanged();
                });

                dialogBuilder.show();
                break;
            case R.id.action_delete:
                AlertDialog.Builder confirmDialog = new AlertDialog.Builder(getContext());
                confirmDialog.setTitle("Do you want to delete this song?");
                confirmDialog.setMessage("Delete " + song.getName());
                confirmDialog.setPositiveButton("Yes", (dialog, which) -> {
                    songService.delete(song.getId());

                    songs.clear();
                    songs.addAll(songService.getAll());
                    adapter.notifyDataSetChanged();
                });
                confirmDialog.setNegativeButton("No", (dialog, which) -> dialog.cancel());
                confirmDialog.show();
                break;
        }
    }

    private void showPopup(Song song) {

        List<String> PlaylistName = new ArrayList<String>();
        try
        {
            for (PlayList playlist : playLists)
            {
                PlaylistName.add(playlist.getName());

            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        //Create sequence of items
        final CharSequence[] ListName = PlaylistName.toArray(new String[PlaylistName.size()]);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setTitle("Playlist");
        dialogBuilder.setMessage("");
        dialogBuilder.setItems(ListName, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                playlistService.add(playLists.get(item).getId(),song.getId());
                int count  = playlistService.getSongNumber(playLists.get(item).getId());
        }
        });

        LayoutInflater inflater = LayoutInflater.from(getContext());

        View view = inflater.inflate(R.layout.item_playlist_menu, null);
        ListView lvPlayList = view.findViewById(R.id.lv_play_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, PlaylistName);
        lvPlayList.setAdapter(adapter);
        lvPlayList.setOnItemClickListener((parent, view1, position, id) -> {

        });

        dialogBuilder.setView(view);
        //Create alert dialog object via builder
        AlertDialog alertDialogObject = dialogBuilder.create();
        //Show the dialog
        alertDialogObject.show();
    }
}