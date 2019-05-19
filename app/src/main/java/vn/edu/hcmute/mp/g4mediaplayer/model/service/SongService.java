package vn.edu.hcmute.mp.g4mediaplayer.model.service;

import android.content.Context;
import android.database.Cursor;

import java.io.IOException;
import java.util.ArrayList;

import vn.edu.hcmute.mp.g4mediaplayer.model.entity.Song;

public class SongService extends SqliteHelper implements ServiceRepository<Song> {

    public SongService(Context context) throws IOException {
        super(context);
    }

    @Override
    public ArrayList<Song> getAll() {
        ArrayList<Song> songs = new ArrayList<>();

        String query = "SELECT * FROM Song";
        Cursor cursor = database.rawQuery(query, null);
        Song song;
        while (cursor.moveToNext()) {
            song = new Song();
            song.setId(cursor.getString(0));
            song.setName(cursor.getString(1));
            song.setAuthor(cursor.getString(2));
            song.setComposer(cursor.getString(3));
            song.setBitrate(cursor.getString(4));
            song.setImage(cursor.getBlob(5));
            song.setFilePath(cursor.getString(6));

            songs.add(song);
        }

        cursor.close();
        return songs;
    }

    @Override
    public Song getOne(Object... keys) {
        return null;
    }

    @Override
    @Deprecated
    public void add(Song song) {
    }

    @Override
    public void delete(Object... keys) {
        if (keys != null && keys.length > 0) {
            String query = "DELETE FROM Song WHERE ID = ?";
            database.execSQL(query, new String[]{(String) keys[0]});
        }
    }

    public void delete(String idSong, boolean alsoDeleteOnDevice) {
    }

    @Override
    public void edit(Song oldEntity, Song newEntity) {
        String query="UPDATE Song SET Name = ? WHERE ID=?";
        database.execSQL(query, new String[]{newEntity.getName(),oldEntity.getId()});
    }

    public ArrayList<Song>  getDownloadedSongs() {
        ArrayList<Song> songs = new ArrayList<>();

        String query = "SELECT * FROM Song WHERE Downloaded = 1";
        Cursor cursor = database.rawQuery(query, null);
        Song song;
        while (cursor.moveToNext()) {
            song = new Song();
            song.setId(cursor.getString(0));
            song.setName(cursor.getString(1));
            song.setAuthor(cursor.getString(2));
            song.setComposer(cursor.getString(3));
            song.setBitrate(cursor.getString(4));
            song.setImage(cursor.getBlob(5));
            song.setFilePath(cursor.getString(6));

            songs.add(song);
        }

        cursor.close();
        return songs;
    }
}