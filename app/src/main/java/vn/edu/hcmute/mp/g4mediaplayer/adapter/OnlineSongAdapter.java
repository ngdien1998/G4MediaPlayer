package vn.edu.hcmute.mp.g4mediaplayer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.util.Base64Utils;

import java.util.List;

import vn.edu.hcmute.mp.g4mediaplayer.R;
import vn.edu.hcmute.mp.g4mediaplayer.api.model.Song;

public class OnlineSongAdapter extends RecyclerView.Adapter<OnlineSongAdapter.OnlineSongViewHolder> {

    private List<Song> songs;
    private LayoutInflater inflater;
    private ButtonFunctionClickListener onButtonPlayClick;
    private ButtonFunctionClickListener onButtonDownloadClick;
    private ButtonFunctionClickListener onButtonInterestClick;
    private ButtonFunctionClickListener onItemClick;

    public void setOnButtonPlayClick(ButtonFunctionClickListener onButtonPlayClick) {
        this.onButtonPlayClick = onButtonPlayClick;
    }

    public void setOnButtonDownloadClick(ButtonFunctionClickListener onButtonDownloadClick) {
        this.onButtonDownloadClick = onButtonDownloadClick;
    }

    public void setOnButtonInterestClick(ButtonFunctionClickListener onButtonInterestClick) {
        this.onButtonInterestClick = onButtonInterestClick;
    }

    public void setOnItemClick(ButtonFunctionClickListener onItemClick) {
        this.onItemClick = onItemClick;
    }

    public OnlineSongAdapter(Context context, List<Song> songs) {
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
        Song song = songs.get(position);
        onlineSongViewHolder.txtName.setText(song.getName());
        onlineSongViewHolder.txtArtist.setText(song.getArtist());

        String imageString = song.getImage();
        if (imageString != null) {
            byte[] imageBytes = Base64Utils.decode(imageString);
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            onlineSongViewHolder.imgSong.setImageBitmap(imageBitmap);
        }

        onlineSongViewHolder.btnPlay.setOnClickListener(view -> {
            if (onButtonPlayClick != null) {
                onButtonPlayClick.onClick(view, song, position);
            }
        });

        onlineSongViewHolder.btnDownload.setOnClickListener(view -> {
            if (onButtonDownloadClick != null) {
                onButtonDownloadClick.onClick(view, song, position );
            }
        });

        onlineSongViewHolder.btnInterrest.setOnClickListener(view -> {
            if (onButtonInterestClick != null) {
                onButtonInterestClick.onClick(view, song, position );
            }
        });

        onlineSongViewHolder.lyt_parent.setOnClickListener(view -> {
            if (onItemClick != null) {
                onItemClick.onClick(view, song, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public interface ButtonFunctionClickListener {
        void onClick(View view, Song song, int position);
    }

    class OnlineSongViewHolder extends RecyclerView.ViewHolder {

        TextView txtName;
        TextView txtArtist;
        ImageButton btnPlay;
        ImageButton btnDownload;
        ImageButton btnInterrest;
        View lyt_parent;
        ImageView imgSong;

        OnlineSongViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txt_song_name);
            txtArtist = itemView.findViewById(R.id.txt_artist);
            btnPlay = itemView.findViewById(R.id.btn_play);
            btnDownload = itemView.findViewById(R.id.btn_download);
            btnInterrest = itemView.findViewById(R.id.btn_interested);
            lyt_parent = itemView.findViewById(R.id.lyt_parent);
            imgSong = itemView.findViewById(R.id.img_song);
        }
    }
}
