package vn.edu.hcmute.mp.g4mediaplayer.model.service;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import vn.edu.hcmute.mp.g4mediaplayer.model.entity.PlayList;

public class PlaylistService extends SqliteHelper implements ServiceRepository<PlayList> {

    public PlaylistService(Context context) throws IOException
    {
        super(context);
    }

    @Override
    public ArrayList<PlayList> getAll() {
        ArrayList<PlayList> playLists = new ArrayList<>();
            try {
                String query = "SELECT * FROM Playlist";
                Cursor cursor = database.rawQuery(query, null);
                PlayList playList;
                while (cursor.moveToNext()) {
                    playList = new PlayList();
                    playList.setId(cursor.getString(0));
                    playList.setName(cursor.getString(1));


                    Date date = null;
                    try {
                        date = new SimpleDateFormat("dd/MM/yyyy").parse(cursor.getString(2));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    playList.setCreatedDate(date);
                    playList.setImage(cursor.getBlob(3));

                    playLists.add(playList);
                }

                cursor.close();
            } catch (Exception ignored) {
            }

        return playLists;
    }

    public int getSongNumber(String id)
    {

            String query = "SELECT count(*) FROM Song_PlayList WHERE ID_PlayList = ?";
            Cursor cursor = database.rawQuery(query, new String[]{id});
            if (cursor.moveToNext()) {
                return cursor.getInt(0);
            }
            cursor.close();

        return 0;
    }

    @Override
    public PlayList getOne(Object... keys) {
        return null;
    }

    @Override
    public void add(PlayList playList) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.forLanguageTag("vi-VN"));
        ContentValues values = new ContentValues();
        values.put("ID",String.valueOf(System.currentTimeMillis()));
        values.put("Name", playList.getName());
        values.put("CreateDate",dateFormat.format(new Date()));
        database.insert("PlayList",null,values);
    }

    @Override
    public void delete(Object... keys) {

    }

    @Override
    public void edit(PlayList oldEntity, PlayList newEntity) {

    }


}
