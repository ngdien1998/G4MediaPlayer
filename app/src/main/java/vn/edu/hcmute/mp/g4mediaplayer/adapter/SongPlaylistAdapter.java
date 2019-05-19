package vn.edu.hcmute.mp.g4mediaplayer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.IOException;
import java.util.ArrayList;

import vn.edu.hcmute.mp.g4mediaplayer.R;
import vn.edu.hcmute.mp.g4mediaplayer.model.entity.Song;
import vn.edu.hcmute.mp.g4mediaplayer.model.service.ArtistService;

public class SongPlaylistAdapter extends RecyclerView.Adapter<SongPlaylistAdapter.SongViewHolder> {

    private Context context;
    private ArrayList<Song> songs;
    private OnItemClickListener onItemClick;
    private OnMoreItemClickListener onMoreItemClick;
    private ArtistService artistService;
    LayoutInflater inflater;

    public SongPlaylistAdapter(Context context, ArrayList<Song> songs) throws IOException {
        this.context = context;
        this.songs = songs;
        artistService = new ArtistService(context);
        inflater = LayoutInflater.from(context);
    }

    public void setOnItemClick(OnItemClickListener onItemClick) {
        this.onItemClick = onItemClick;
    }

    public void setOnMoreItemClick(OnMoreItemClickListener onMoreItemClick) {
        this.onMoreItemClick = onMoreItemClick;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        try {
            View view = inflater.inflate(R.layout.item_song_playlist, viewGroup, false);
            return new SongViewHolder(view);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder songViewHolder, int position) {
        try {
            Song song = songs.get(position);

            byte[] songImg = song.getImage();
            if (songImg != null) {
                Bitmap image = BitmapFactory.decodeByteArray(songImg, 0, songImg.length);
                songViewHolder.imgSong.setImageBitmap(image);
            }

            String songName = song.getName();
            if (!songName.isEmpty()) {
                songViewHolder.txtName.setText(songName);
            }

            String artist = artistService.getSongArtist(song.getId());
            if (!artist.isEmpty()) {
                songViewHolder.txtArtist.setText(artist);
            }

            songViewHolder.lyt_parent.setOnClickListener(view -> {
                if (onItemClick != null) {
                    onItemClick.onItemClick(view, song, position);
                }
            });

            songViewHolder.btnMore.setOnClickListener(view -> {
                if (onMoreItemClick != null) {
                    onMoreButtonClick(view, song, position);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onMoreButtonClick(View view, Song song, int position) {
        try {
            PopupMenu popupMenu = new PopupMenu(context, view);
            popupMenu.inflate(R.menu.menu_more_playlist);
            popupMenu.setOnMenuItemClickListener(item -> {
                onMoreItemClick.onMoreItemClick(view, song, position, item);
                return true;
            });
            popupMenu.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, Song song, int position);
    }

    public interface OnMoreItemClickListener {
        void onMoreItemClick(View view, Song song, int position, MenuItem item);
    }

    static class SongViewHolder extends RecyclerView.ViewHolder {

        CircularImageView imgSong;
        TextView txtName;
        TextView txtArtist;
        ImageButton btnMore;
        View lyt_parent;

        SongViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSong = itemView.findViewById(R.id.img_song);
            txtName = itemView.findViewById(R.id.txt_song_name);
            txtArtist = itemView.findViewById(R.id.txt_artist);
            btnMore = itemView.findViewById(R.id.btnmore);
            lyt_parent = itemView.findViewById(R.id.lyt_parent);

        }
    }
}
