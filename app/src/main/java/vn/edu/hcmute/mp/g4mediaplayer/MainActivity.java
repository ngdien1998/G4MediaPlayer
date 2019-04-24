package vn.edu.hcmute.mp.g4mediaplayer;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import vn.edu.hcmute.mp.g4mediaplayer.fragment.HelpAndFeedbackFragment;
import vn.edu.hcmute.mp.g4mediaplayer.fragment.ListenNowFragment;
import vn.edu.hcmute.mp.g4mediaplayer.fragment.MusicLibraryFragment;
import vn.edu.hcmute.mp.g4mediaplayer.fragment.RecentsFragment;
import vn.edu.hcmute.mp.g4mediaplayer.fragment.SettingsFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        actionBar = getSupportActionBar();
        assert actionBar != null;

        actionBar.setTitle("Music Library");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_container, new MusicLibraryFragment())
                .commit();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_music_library);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        inflater.inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.item_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // TODO: handle for action setting selected
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_listen_now:
                actionBar.setTitle("Listen now");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.layout_container, new ListenNowFragment())
                        .commit();
                break;
            case R.id.nav_recents:
                actionBar.setTitle("Recents");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.layout_container, new RecentsFragment())
                        .commit();
                break;
            case R.id.nav_music_library:
                actionBar.setTitle("Music Library");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.layout_container, new MusicLibraryFragment())
                        .commit();
                break;
            case R.id.nav_settings:
                actionBar.setTitle("Settings");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.layout_container, new SettingsFragment())
                        .commit();
                break;
            case R.id.nav_help_feedback:
                actionBar.setTitle("Help & Feedback");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.layout_container, new HelpAndFeedbackFragment())
                        .commit();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}