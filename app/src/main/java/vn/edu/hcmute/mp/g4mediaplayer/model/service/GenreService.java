package vn.edu.hcmute.mp.g4mediaplayer.model.service;

import android.content.Context;
import android.database.Cursor;

import java.io.IOException;
import java.util.ArrayList;

import vn.edu.hcmute.mp.g4mediaplayer.model.entity.Genre;

public class GenreService extends SqliteHelper implements ServiceRepository<Genre> {

    public GenreService(Context context) throws IOException {
        super(context);
    }

    @Override
    public ArrayList<Genre> getAll() {
        ArrayList<Genre> genres = new ArrayList<>();

        String query = "SELECT * FROM Genre";
        Cursor cursor = database.rawQuery(query, null);

        Genre genre;
        while (cursor.moveToNext()) {
            genre = new Genre();
            genre.setId(cursor.getString(0));
            genre.setName(cursor.getString(1));

            genres.add(genre);
        }

        cursor.close();

        return genres;
    }

    @Override
    public Genre getOne(Object... keys) {
        return null;
    }

    @Override
    public void add(Genre genre) {

    }

    @Override
    public void delete(Object... keys) {

    }

    @Override
    public void edit(Genre oldEntity, Genre newEntity) {

    }
}
