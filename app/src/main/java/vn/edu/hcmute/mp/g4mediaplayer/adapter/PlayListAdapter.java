package vn.edu.hcmute.mp.g4mediaplayer.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.PlayListViewHolder> {

    @NonNull
    @Override
    public PlayListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull PlayListViewHolder playListViewHolder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class PlayListViewHolder extends RecyclerView.ViewHolder {

        public PlayListViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
