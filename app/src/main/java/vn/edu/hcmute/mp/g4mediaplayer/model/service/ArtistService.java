package vn.edu.hcmute.mp.g4mediaplayer.model.service;

import android.content.Context;
import android.database.Cursor;

import java.io.IOException;
import java.util.ArrayList;

import vn.edu.hcmute.mp.g4mediaplayer.model.entity.Artist;
import vn.edu.hcmute.mp.g4mediaplayer.model.entity.Song;

public class ArtistService extends SqliteHelper implements ServiceRepository<Artist> {

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

    @Override
    public ArrayList<Artist> getAll() {
        ArrayList<Artist> artists = new ArrayList<>();

        String query = "SELECT * FROM Artist";
        Cursor cursor = database.rawQuery(query, null);

        Artist artist;

        while(cursor.moveToNext()){
            artist = new Artist();
            artist.setId(cursor.getString(0));
            artist.setName(cursor.getString(1));

            artists.add(artist);
        }

        cursor.close();
        return artists;
    }

    @Override
    public Artist getOne(Object... keys) {
        return null;
    }

    @Override
    public void add(Artist artist) {

    }

    @Override
    public void delete(Object... keys) {

    }

    @Override
    public void edit(Artist oldEntity, Artist newEntity) {

    }

    public ArrayList<Song> getAllSongOfArtist(int idArtist) {
        ArrayList<Song> songs = new ArrayList<>();

//        String query = "select * from Song_Artist, Song where ID_Artist="+idArtist+" and Song.ID=Song_Artist.ID_Song";
        String query = "select * from Song";
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
