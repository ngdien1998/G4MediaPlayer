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
import vn.edu.hcmute.mp.g4mediaplayer.model.entity.Artist;
import vn.edu.hcmute.mp.g4mediaplayer.model.entity.Genre;

public class ArtistsAdapter extends RecyclerView.Adapter<ArtistsAdapter.ArtistsViewHolder>{

    private Context context;
    private ArrayList<Artist> artists;
    private LayoutInflater inflater;

    private ItemClickListener onItemClickListener;
    private BtnMoreClickListener onBtnMoreClickListener;

    public void setOnItemClickListener(ItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnBtnMoreClickListener(BtnMoreClickListener btnMoreClickListener){
        this.onBtnMoreClickListener = btnMoreClickListener;
    }

    public ArtistsAdapter(Context context, ArrayList<Artist> artists) {
        this.context = context;
        this.artists = artists;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ArtistsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.grid_item_playlist, viewGroup, false);
        return new ArtistsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistsViewHolder artistsViewHolder, int i) {
        Artist artist = artists.get(i);
        artistsViewHolder.txtArtistsName.setText(artist.getName());

        artistsViewHolder.parent.setOnClickListener(view -> {
            if(onItemClickListener != null){
                onItemClickListener.onCLick(view, artist, i);
            }
        });

        artistsViewHolder.btnMore.setOnClickListener(view -> {
            if(onBtnMoreClickListener != null){
                btnMoreClick(view, artist);
            }
        });
    }

    private void btnMoreClick(View view, Artist artist) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.setOnMenuItemClickListener(item -> {
            onBtnMoreClickListener.onLick(view, artist, item);
            return true;
        });
        popupMenu.inflate(R.menu.menu_more_artists);
        popupMenu.show();
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    public class ArtistsViewHolder extends RecyclerView.ViewHolder {
        View parent;
        TextView txtArtistsName;
        ImageButton btnMore;

        public ArtistsViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.lyt_parent);
            txtArtistsName = itemView.findViewById(R.id.txt_song_name);
            btnMore = itemView.findViewById(R.id.btn_more);
        }
    }

    public interface ItemClickListener{
        void onCLick(View view, Artist artist, int position);
    }

    public interface  BtnMoreClickListener{
        void onLick(View view, Artist artist, MenuItem item);
    }
}
