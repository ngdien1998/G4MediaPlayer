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
import vn.edu.hcmute.mp.g4mediaplayer.model.entity.Song;

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

    public ArrayList<String> getName() {
        ArrayList<String> playLists = new ArrayList<>();

        String query = "SELECT Name FROM Playlist";
        Cursor cursor = database.rawQuery(query, null);
        PlayList playList;
        while (cursor.moveToNext()) {
            String name = cursor.getString(0);
            playLists.add(name);
        }

        cursor.close();

        return playLists;
    }

    public ArrayList<Song> getSongList(String ID)
    {
        ArrayList<Song> songs = new ArrayList<>();

        String query = "SELECT * FROM Song,Song_PlayList WHERE ID = ID_Song AND ID_PLayList = ?";
        Cursor cursor = database.rawQuery(query, new String[]{ID});
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
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.forLanguageTag("vi-VN"));
            ContentValues values = new ContentValues();
            values.put("ID", String.valueOf(System.currentTimeMillis()));
            values.put("Name", playList.getName());
            values.put("CreateDate", dateFormat.format(new Date()));
            database.insert("Playlist", null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void add(String ID_Playlist, String ID_Song)
    {
        try {
            ContentValues values = new ContentValues();
            values.put("ID_Song", ID_Song );
            values.put("ID_PlayList",ID_Playlist );
            database.insert("Song_PlayList", null, values);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    public  void deletePLaylist(String id)
    {
        try {
            String query = "DELETE FROM Playlist WHERE ID = ?";
            database.execSQL(query, new String[]{id});

             query = "DELETE FROM Song_PlayList WHERE ID_PlayList = ?";
            database.execSQL(query, new String[]{id});

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public  void rename(String id,String newname)
    {
        try {
            String query = "UPDATE Playlist SET Name = ? WHERE ID = ?";
            database.execSQL(query, new String[]{newname, id});

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public void delete(Object... keys) {
        try {
            database.execSQL("DELETE FROM Playlist WHERE ID = ?", new String[]{(String) keys[0]});
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void edit(PlayList oldEntity, PlayList newEntity) {

    }


}

