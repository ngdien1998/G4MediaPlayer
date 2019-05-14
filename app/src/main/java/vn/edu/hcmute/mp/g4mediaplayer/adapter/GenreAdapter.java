package vn.edu.hcmute.mp.g4mediaplayer.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import vn.edu.hcmute.mp.g4mediaplayer.R;
import vn.edu.hcmute.mp.g4mediaplayer.model.entity.Genre;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder> {

    private Context context;
    private ArrayList<Genre> genres;
    private LayoutInflater inflater;

    private ItemClickListener onItemClickListener;
    private BtnMoreClickListener onBtnMoreClickListener;

    public void setOnItemClickListener(ItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnBtnMoreClickListener(BtnMoreClickListener onBtnMoreClickListener) {
        this.onBtnMoreClickListener = onBtnMoreClickListener;
    }

    public GenreAdapter(Context context, ArrayList<Genre> genres) {
        this.context = context;
        this.genres = genres;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = inflater.inflate(R.layout.grid_item_playlist, viewGroup, false);
        return new GenreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder genreViewHolder, int position) {
        Genre genre = genres.get(position);
        genreViewHolder.txtGeneName.setText(genre.getName());

        genreViewHolder.parent.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onClick(view,genre, position);
            }
        });

        genreViewHolder.btnMore.setOnClickListener(view -> {
            if (onBtnMoreClickListener != null) {
                btnMoreClick(view, genre);
            }
        });
    }

    private void btnMoreClick(View view, Genre genre) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.setOnMenuItemClickListener(item -> {
            onBtnMoreClickListener.onClick(view, genre, item);
            return true;
        });
        popupMenu.inflate(R.menu.menu_more_genre);
        popupMenu.show();
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

    class GenreViewHolder extends RecyclerView.ViewHolder {

        View parent;
        TextView txtGeneName;
        ImageButton btnMore;

        GenreViewHolder(@NonNull View itemView) {
            super(itemView);

            parent = itemView.findViewById(R.id.lyt_parent);
            txtGeneName = itemView.findViewById(R.id.txt_song_name);
            btnMore = itemView.findViewById(R.id.btn_more);
        }
    }

    interface ItemClickListener {
        void onClick(View view, Genre genre, int position);
    }

    interface BtnMoreClickListener {
        void onClick(View view, Genre genre, MenuItem item);
    }
}
