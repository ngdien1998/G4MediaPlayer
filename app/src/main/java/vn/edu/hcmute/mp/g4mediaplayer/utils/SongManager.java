package vn.edu.hcmute.mp.g4mediaplayer.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;

import vn.edu.hcmute.mp.g4mediaplayer.model.entity.Song;

public class SongManager {

    private static final String MEDIA_PATH = Environment.getExternalStorageDirectory().getPath() + "/Music/";
    private static final String MP3_EXT = ".mp3";

    private static MediaMetadataRetriever retriever = new MediaMetadataRetriever();

    private static Song from(String source) {
        retriever.setDataSource(source);

        Song song = new Song();

        File file = new File(source);
        String fileName = file.getName();

        song.setName(fileName.replace(".mp3", ""));
        song.setFilePath(file.getPath());

        try {
            byte[] embeddedPicture = retriever.getEmbeddedPicture();
            Bitmap songImage = BitmapFactory.decodeByteArray(embeddedPicture, 0, embeddedPicture.length);
            song.setImage(songImage);
        } catch (Exception e) {
            song.setImage(null);
        }

        String album = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
        song.setAlbum(album);

        String artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        song.setArtist(artist);

        String albumArtist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST);
        song.setAlbumArtist(albumArtist);

        String composer = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_COMPOSER);
        song.setComposer(composer);

        String author = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR);
        song.setAuthor(author);

        String bitrate = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);
        song.setBitrate(bitrate);

        String genre = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);
        song.setGenre(genre);

        return song;
    }

    public static ArrayList<Song> loadSongs() {
        ArrayList<Song> songs = new ArrayList<>();

        File home = new File(MEDIA_PATH);
        File[] files = home.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isDirectory()) {
                    scanInDirectory(songs, file);
                } else if (file.getName().endsWith(MP3_EXT)) {
                    songs.add(from(file.getPath()));
                }
            }
        }
        return songs;
    }

    private static void scanInDirectory(ArrayList<Song> songs, File directory) {
        if (directory == null) {
            return;
        }
        File[] files = directory.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isDirectory()) {
                    scanInDirectory(songs, file);
                } else if (file.getName().endsWith(MP3_EXT)) {
                    songs.add(from(file.getPath()));
                }
            }
        }
    }
}