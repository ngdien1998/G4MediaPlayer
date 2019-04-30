package vn.edu.hcmute.mp.g4mediaplayer.model.service;

import android.content.Context;
import android.database.Cursor;

import java.io.IOException;

public class ArtistService extends SqliteHelper {

    public ArtistService(Context context) throws IOException {
        super(context);
    }

    public String getSongArtist(String idSong) {
        String query = "SELECT Name " +
                       "FROM Artist, Song_Artist " +
                       "WHERE ID = ID_Artist AND ID_Song = ?";
        Cursor cursor = database.rawQuery(query, new String[]{idSong});
        if (cursor.moveToNext()) {
            return cursor.getString(0);
        }
        cursor.close();
        return "";
    }
}
