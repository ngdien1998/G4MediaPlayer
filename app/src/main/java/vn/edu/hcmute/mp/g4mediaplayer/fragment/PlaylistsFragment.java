package vn.edu.hcmute.mp.g4mediaplayer.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import vn.edu.hcmute.mp.g4mediaplayer.R;
import vn.edu.hcmute.mp.g4mediaplayer.activity.PlayCenterActivity;
import vn.edu.hcmute.mp.g4mediaplayer.adapter.PlayListAdapter;
import vn.edu.hcmute.mp.g4mediaplayer.common.Consts;
import vn.edu.hcmute.mp.g4mediaplayer.model.entity.PlayList;
import vn.edu.hcmute.mp.g4mediaplayer.model.entity.Song;
import vn.edu.hcmute.mp.g4mediaplayer.model.service.PlaylistService;
import vn.edu.hcmute.mp.g4mediaplayer.utils.Tools;
import vn.edu.hcmute.mp.g4mediaplayer.widget.SpacingItemDecoration;

public class PlaylistsFragment extends Fragment {
    private EditText edtTitle;
    private TextInputLayout tilTitle;

    private PlaylistService service;
    PlayListAdapter adapter;
    ArrayList<PlayList> playLists;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_playlists, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.rcl_playlist);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.addItemDecoration(new SpacingItemDecoration(2,
                Tools.dpToPx(Objects.requireNonNull(getContext()), 4), true));
        recyclerView.setHasFixedSize(true);

        FloatingActionButton fabAddPlaylist = root.findViewById(R.id.fab_add_playlist);

        fabAddPlaylist.setOnClickListener(this::fabAddPlaylistOnClick);


        try {
            service = new PlaylistService(getContext());
            playLists = service.getAll();

            adapter = new PlayListAdapter(getContext(), playLists);
           // adapter.setOnItemClick(this::adapterPlaylist_itemClick);
            adapter.setOnMoreItemClick(this::adapterPlaylist_itemMoreClick);
            recyclerView.setAdapter(adapter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }
    private void adapterPlaylist_itemClick(View view, PlayList playList, int position) {
        Intent playIntent = new Intent(getActivity(), PlayCenterActivity.class);
        playIntent.putExtra(Consts.SONGS_EXTRA, playLists);
        playIntent.putExtra(Consts.SONG_EXTRA, playList);
        playIntent.putExtra(Consts.SONG_POSITION_EXTRA, position);

        startActivity(playIntent);
    }
    private void adapterPlaylist_itemMoreClick(View view, PlayList playList, MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_item_play:
                adapterPlaylist_itemClick(view,playList,0);
                break;
            case R.id.menu_item_rename:
                renamePlaylist(playList);
                break;
            case R.id.menu_item_delete:
                  deletePlaylist(playList);
                break;
        }
    }

    private void renamePlaylist(PlayList playList) {
        LayoutInflater inflater = getLayoutInflater();

        @SuppressLint("InflateParams")
        View enterTitleDialog = inflater.inflate(R.layout.layout_dialog, null);

        tilTitle = enterTitleDialog.findViewById(R.id.tilTitle);
        edtTitle = enterTitleDialog.findViewById(R.id.edtTitle);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        dialogBuilder.setTitle("New name");
        dialogBuilder.setView(enterTitleDialog);
        dialogBuilder.setCancelable(true);
        dialogBuilder.setNegativeButton("Cancel", this::dialogOnNegativeButtonClick);
        dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    service = new PlaylistService(getContext());
                    String newname = edtTitle.getText().toString().trim();
                    service.rename(playList.getId(), newname);
                    playLists.clear();
                    playLists.addAll(service.getAll());
                    adapter.notifyDataSetChanged();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        dialogBuilder.show();

    }



    private void deletePlaylist(PlayList playList) {
        try {
            service = new PlaylistService(getContext());

        service.deletePLaylist(playList.getId());
        playLists.clear();
        playLists.addAll(service.getAll());
        adapter.notifyDataSetChanged();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    private void fabAddPlaylistOnClick(View view) {
        LayoutInflater inflater = getLayoutInflater();

        @SuppressLint("InflateParams")
        View enterTitleDialog = inflater.inflate(R.layout.layout_dialog, null);

        tilTitle = enterTitleDialog.findViewById(R.id.tilTitle);
        edtTitle = enterTitleDialog.findViewById(R.id.edtTitle);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        dialogBuilder.setTitle("Playlist Name");
        dialogBuilder.setView(enterTitleDialog);
        dialogBuilder.setCancelable(true);
        dialogBuilder.setNegativeButton("Cancel", this::dialogOnNegativeButtonClick);
        dialogBuilder.setPositiveButton("Save", this::dialogOnPositiveButtonClick);

        dialogBuilder.show();
    }

    private void dialogOnPositiveButtonClick(DialogInterface dialogInterface, int i) {
        String title = edtTitle.getText().toString().trim();
        if (title.isEmpty()) {
            tilTitle.setError("The title cannot be empty");
            return;
        }

        tilTitle.setErrorEnabled(false);
        PlayList playList = new PlayList();
        playList.setName(title);
        service.add(playList);
        playLists.clear();
        playLists.addAll(service.getAll());
        adapter.notifyDataSetChanged();

    }

    private void dialogOnNegativeButtonClick(DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
    }

    public List<String> getName() {
        try {
            service = new PlaylistService(getContext());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
            return  service.getName();
    }
}
