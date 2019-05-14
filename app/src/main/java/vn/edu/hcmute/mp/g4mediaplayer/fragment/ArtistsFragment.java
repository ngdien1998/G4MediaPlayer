package vn.edu.hcmute.mp.g4mediaplayer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Objects;

import vn.edu.hcmute.mp.g4mediaplayer.R;
import vn.edu.hcmute.mp.g4mediaplayer.activity.SongOfArtistsActivity;
import vn.edu.hcmute.mp.g4mediaplayer.adapter.ArtistsAdapter;
import vn.edu.hcmute.mp.g4mediaplayer.model.entity.Artist;
import vn.edu.hcmute.mp.g4mediaplayer.model.service.ArtistService;
import vn.edu.hcmute.mp.g4mediaplayer.utils.Tools;
import vn.edu.hcmute.mp.g4mediaplayer.widget.SpacingItemDecoration;

public class ArtistsFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artists, container, false);

        // Load data
        RecyclerView recyclerView = view.findViewById(R.id.rcl_artists);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.addItemDecoration(new SpacingItemDecoration(2,
                Tools.dpToPx(Objects.requireNonNull(getContext()), 4), true));
        recyclerView.setHasFixedSize(true);

        try{
            ArtistService service = new ArtistService(getContext());
            ArrayList<Artist> artists = service.getAll();

            ArtistsAdapter artistsAdapter = new ArtistsAdapter(getContext(), artists);
            artistsAdapter.setOnItemClickListener(this::adapterOnItemClick);
            recyclerView.setAdapter(artistsAdapter);
        }catch (Exception e){
            e.printStackTrace();
        }

        return view;
    }

    private void adapterOnItemClick(View view, Artist artist, int i) {
        Intent intentArtist = new Intent(getContext(),SongOfArtistsActivity.class);

        intentArtist.putExtra("idArtist", artist.getId());
        intentArtist.putExtra("nameArtist", artist.getName());

        this.startActivity(intentArtist);
        Log.e("TAG", artist.getName());
    }
}