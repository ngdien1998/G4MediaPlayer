package vn.edu.hcmute.mp.g4mediaplayer.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import vn.edu.hcmute.mp.g4mediaplayer.R;
import vn.edu.hcmute.mp.g4mediaplayer.api.model.Song;

public class OnlineSongAdapter extends RecyclerView.Adapter<OnlineSongAdapter.OnlineSongViewHolder> {

    private Context context;
    private List<Song> songs;
    private LayoutInflater inflater;

    public OnlineSongAdapter(Context context, List<Song> songs) {
        this.context = context;
        this.songs = songs;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public OnlineSongViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View itemView = inflater.inflate(R.layout.item_row_online_song, viewGroup, false);
        return new OnlineSongViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OnlineSongViewHolder onlineSongViewHolder, int position) {

    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    class OnlineSongViewHolder extends RecyclerView.ViewHolder {

        OnlineSongViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
