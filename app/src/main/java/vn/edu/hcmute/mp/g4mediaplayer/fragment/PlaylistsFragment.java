package vn.edu.hcmute.mp.g4mediaplayer.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import vn.edu.hcmute.mp.g4mediaplayer.R;
import vn.edu.hcmute.mp.g4mediaplayer.adapter.PlayListAdapter;
import vn.edu.hcmute.mp.g4mediaplayer.model.entity.PlayList;
import vn.edu.hcmute.mp.g4mediaplayer.model.service.PlaylistService;
import vn.edu.hcmute.mp.g4mediaplayer.utils.Tools;
import vn.edu.hcmute.mp.g4mediaplayer.widget.SpacingItemDecoration;

public class PlaylistsFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_playlists, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.rcl_playlist);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.addItemDecoration(new SpacingItemDecoration(2,
                Tools.dpToPx(Objects.requireNonNull(getContext()), 4), true));
        recyclerView.setHasFixedSize(true);

            try
            {
                PlaylistService service = new PlaylistService(getContext());
                ArrayList<PlayList> playLists = service.getAll();

                PlayListAdapter adapter = new PlayListAdapter(getContext(),playLists);
                recyclerView.setAdapter(adapter);
            } catch (IOException e)
            {
                e.printStackTrace();
            }


        return root;
    }
}
