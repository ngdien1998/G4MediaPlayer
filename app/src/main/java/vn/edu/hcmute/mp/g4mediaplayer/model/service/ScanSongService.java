package vn.edu.hcmute.mp.g4mediaplayer.model.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ScanSongService extends SqliteHelper {

    private static final String MP3_EXT = ".mp3";
    private static MediaMetadataRetriever retriever = new MediaMetadataRetriever();
    private static ArrayList<String> MEDIA_PATHs = new ArrayList<>();

    static {
        MEDIA_PATHs.add(Environment.getExternalStorageDirectory().getPath() + "/Music/");

        File[] fileList = new File("/storage/").listFiles();
        for (File file : fileList) {
            String musicPath = file.getAbsolutePath() + "/Music/";
            if (file.isDirectory() && new File(musicPath).exists() && file.canRead()){
                MEDIA_PATHs.add(musicPath);
            }
        }
    }

    public ScanSongService(Context context) throws IOException {
        super(context);
    }

    public void scanAndSave() {
        for (String path : MEDIA_PATHs) {
            File home = new File(path);
            File[] files = home.listFiles();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        scanAndSaveInDirectory(file);
                    } else if (file.getName().endsWith(MP3_EXT)) {
                        extractAndSaveFrom(file.getPath(), false);
                    }
                }
            }
        }
    }

    public void extractAndSaveFrom(String source, boolean isDownloaded) {
        retriever.setDataSource(source);

        String query;
        ContentValues values = new ContentValues();

        File file = new File(source);
        String fileName = file.getName();
        String songName = fileName.replace(MP3_EXT, "");
        String filePath = file.getPath();

        byte[] embeddedPicture = null;
        try {
            embeddedPicture = retriever.getEmbeddedPicture();
        } catch (Exception ignored) {
        }

        String idArtist = null;
        String artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        if (artist != null) {
            query = "SELECT COUNT(*) FROM Artist WHERE Name = ?";
            Cursor cursor2 = database.rawQuery(query, new String[]{artist});
            if (cursor2.moveToNext() && cursor2.getInt(0) <= 0) {
                idArtist = String.valueOf(System.currentTimeMillis());
                values.clear();
                values.put("ID", idArtist);
                values.put("Name", artist);
                database.insert("Artist", null, values);
            }
            cursor2.close();
        }

        String idAlbum = null;
        String album = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
        if (album != null) {
            query = "SELECT COUNT(*) FROM Album WHERE Name = ?";
            Cursor cursor1 = database.rawQuery(query, new String[]{album});
            if (cursor1.moveToNext() && cursor1.getInt(0) <= 0) {
                idAlbum = String.valueOf(System.currentTimeMillis());
                values.clear();
                values.put("ID", idAlbum);
                values.put("Name", album);
                values.put("ID_Artist", idArtist);
                database.insert("Album", null, values);
            }
            cursor1.close();
        }

        String composer = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_COMPOSER);

        String author = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR);

        String bitrate = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);

        String idGenre = null;
        String genre = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);
        if (genre != null) {
            query = "SELECT COUNT(*) FROM Genre WHERE Name = ?";
            Cursor cursor3 = database.rawQuery(query, new String[]{genre});
            if (cursor3.moveToNext() && cursor3.getInt(0) <= 0) {
                idGenre = String.valueOf(System.currentTimeMillis());
                values.clear();
                values.put("ID", idGenre);
                values.put("Name", genre);
                database.insert("Genre", null, values);
            }
            cursor3.close();
        }

        query = "SELECT COUNT(*) FROM Song WHERE Name = ?";
        Cursor cursor4 = database.rawQuery(query, new String[]{songName});
        if (cursor4.moveToNext() && cursor4.getInt(0) <= 0) {
            String idSong = String.valueOf(System.currentTimeMillis());

            values.put("ID", idSong);
            values.put("Name", songName);
            values.put("Author", author);
            values.put("Composer", composer);
            values.put("Bitrate", bitrate);
            values.put("Image", embeddedPicture);
            values.put("FilePath", filePath);
            if (isDownloaded) {
                values.put("Downloaded", 1);
            } else {
                values.put("Downloaded", 0);
            }

            database.insert("Song", null, values);

            if (idArtist != null) {
                values.clear();
                values.put("ID_Song", idSong);
                values.put("ID_Artist", idArtist);
                database.insert("Song_Artist", null, values);

            }

            if (idAlbum != null) {
                values.clear();
                values.put("ID_Song", idSong);
                values.put("ID_Album", idAlbum);
                database.insert("Song_Album", null, values);
            }

            if (idGenre != null) {
                values.clear();
                values.put("ID_Song", idSong);
                values.put("ID_Genre", idGenre);
                database.insert("Song_Genre", null, values);
            }
        }
        cursor4.close();
    }

    private void scanAndSaveInDirectory(File directory) {
        if (directory == null) {
            return;
        }
        File[] files = directory.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isDirectory()) {
                    scanAndSaveInDirectory(file);
                } else if (file.getName().endsWith(MP3_EXT)) {
                    extractAndSaveFrom(file.getPath(), false);
                }
            }
        }
    }
}