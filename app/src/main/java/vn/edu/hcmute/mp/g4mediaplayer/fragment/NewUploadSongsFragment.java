package vn.edu.hcmute.mp.g4mediaplayer.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.hcmute.mp.g4mediaplayer.R;
import vn.edu.hcmute.mp.g4mediaplayer.activity.OnlinePlayingCenterActivity;
import vn.edu.hcmute.mp.g4mediaplayer.adapter.OnlineSongAdapter;
import vn.edu.hcmute.mp.g4mediaplayer.api.ApiService;
import vn.edu.hcmute.mp.g4mediaplayer.api.SongsService;
import vn.edu.hcmute.mp.g4mediaplayer.api.model.Song;
import vn.edu.hcmute.mp.g4mediaplayer.common.Consts;
import vn.edu.hcmute.mp.g4mediaplayer.task.DownloadAsyncTask;

import static vn.edu.hcmute.mp.g4mediaplayer.api.ApiService.BASE_URL;

public class NewUploadSongsFragment extends Fragment {

    private int defaultTake = 20;
    private final int TAKE_COUNT = 10;
    ArrayList<Song> songs;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_new_upload_songs, container, false);

        RecyclerView rclNewUploadSongs = root.findViewById(R.id.rcl_new_upload_song);
        rclNewUploadSongs.setLayoutManager(new LinearLayoutManager(getActivity()));
        rclNewUploadSongs.setHasFixedSize(true);

        ProgressDialog dialog = ProgressDialog.show(getContext(), "Loading songs", "Please wait", true, false);

        SongsService songApiService = ApiService.getSongService();
        songApiService.getSongs(defaultTake).enqueue(new Callback<List<Song>>() {

            private  View view;
            private Song song;

            @Override
            public void onResponse(@NonNull Call<List<Song>> call, @NonNull Response<List<Song>> response) {
                if (response.code() == 200) {
                    List<Song> body = response.body();
                    assert body != null;

                    songs = new ArrayList<>(body);
                    for (Song song : songs) {
                        song.setUrl(BASE_URL + "/data/music/" + song.getId());
                    }

                    OnlineSongAdapter adapter = new OnlineSongAdapter(getContext(), songs);
                    rclNewUploadSongs.setAdapter(adapter);

                    adapter.setOnItemClick(this::adapterOnItemClick);
                    adapter.setOnButtonDownloadClick(this::adapterOnButtonDownloadClick);

                    dialog.dismiss();
                }
            }

            private void adapterOnButtonDownloadClick(View view, Song song, int position) {
                this.view = view;
                this.song = song;

                Snackbar.make(view, "Start downloading " + song.getName(), Snackbar.LENGTH_LONG).show();

                DownloadAsyncTask downloadAsyncTask = new DownloadAsyncTask(getContext());
                downloadAsyncTask.setOndownloadCompeletedListener(this::downloadAsyncTaskOnDownloadCompleted);
                downloadAsyncTask.execute(song);
            }

            private void downloadAsyncTaskOnDownloadCompleted(Boolean error) {
                if (!error) {
                    Snackbar.make(view, "Downloaded " + song.getName(), Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(view, "Download " + song.getName() + " failed", Snackbar.LENGTH_LONG).show();
                }
            }

            private void adapterOnItemClick(View view, Song song, int position) {
                Intent intent = new Intent(getContext(), OnlinePlayingCenterActivity.class);
                intent.putExtra(Consts.SONGS_EXTRA, songs);
                intent.putExtra(Consts.SONG_EXTRA, song);
                intent.putExtra(Consts.SONG_POSITION_EXTRA, position);

                startActivity(intent);
            }

            @Override
            public void onFailure(@NonNull Call<List<Song>> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "Request failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
                dialog.dismiss();
            }
        });

        return root;
    }
}