package vn.edu.hcmute.mp.g4mediaplayer.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Objects;

import vn.edu.hcmute.mp.g4mediaplayer.R;
import vn.edu.hcmute.mp.g4mediaplayer.adapter.GenreAdapter;
import vn.edu.hcmute.mp.g4mediaplayer.model.entity.Genre;
import vn.edu.hcmute.mp.g4mediaplayer.model.service.GenreService;
import vn.edu.hcmute.mp.g4mediaplayer.utils.Tools;
import vn.edu.hcmute.mp.g4mediaplayer.widget.SpacingItemDecoration;

public class GenresFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_genres, container, false);

        RecyclerView rclGenres = root.findViewById(R.id.rcl_genres);
        rclGenres.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rclGenres.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(Objects.requireNonNull(getActivity()), 4), true));
        rclGenres.setHasFixedSize(true);

        try {
            GenreService service = new GenreService(getContext());
            ArrayList<Genre> genres = service.getAll();

            GenreAdapter adapter = new GenreAdapter(getContext(), genres);
            rclGenres.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return root;
    }
}