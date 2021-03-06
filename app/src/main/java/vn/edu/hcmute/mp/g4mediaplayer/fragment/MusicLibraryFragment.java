package vn.edu.hcmute.mp.g4mediaplayer.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.edu.hcmute.mp.g4mediaplayer.R;
import vn.edu.hcmute.mp.g4mediaplayer.adapter.ViewPageAdapter;

public class MusicLibraryFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_library, container, false);

        ViewPageAdapter pageAdapter = new ViewPageAdapter(getChildFragmentManager());

        pageAdapter.addFragment("Playlists", new PlaylistsFragment());
        pageAdapter.addFragment("Artists", new ArtistsFragment());
        pageAdapter.addFragment("Albums", new AlbumsFragment());
        pageAdapter.addFragment("Songs", new SongsFragment());
        pageAdapter.addFragment("Genres", new GenresFragment());

        TabLayout tabLayout = view.findViewById(R.id.tab_layout_library);
        ViewPager pgrMain = view.findViewById(R.id.pgrMain);

        pgrMain.setAdapter(pageAdapter);
        tabLayout.setupWithViewPager(pgrMain);

        return view;
    }

}
