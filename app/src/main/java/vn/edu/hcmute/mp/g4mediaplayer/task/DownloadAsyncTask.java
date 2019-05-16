package vn.edu.hcmute.mp.g4mediaplayer.task;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import vn.edu.hcmute.mp.g4mediaplayer.api.model.Song;
import vn.edu.hcmute.mp.g4mediaplayer.model.service.ScanSongService;

public class DownloadAsyncTask extends AsyncTask<Song, Void, Boolean> {

    @SuppressLint("StaticFieldLeak")
    private Context context;
    private String savedUrl;
    private DownloadCompeletedListener ondownloadCompeletedListener;
    private static String SAVE_PATH = Environment.getExternalStorageDirectory().getPath() + "/G4 Media Player/";

    public DownloadAsyncTask(Context context) {
        this.context = context;
    }

    public void setOndownloadCompeletedListener(DownloadCompeletedListener ondownloadCompeletedListener) {
        this.ondownloadCompeletedListener = ondownloadCompeletedListener;
    }

    @Override
    protected void onPostExecute(Boolean error) {
        super.onPostExecute(error);
        try {
            if (ondownloadCompeletedListener != null) {
                ondownloadCompeletedListener.invoke(error);
                if (!error) {
                    try {
                        if (savedUrl != null) {
                            ScanSongService service = new ScanSongService(context);
                            service.extractAndSaveFrom(savedUrl, true);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Boolean doInBackground(Song... songs) {
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(songs[0].getUrl());
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return true;
            }

            input = connection.getInputStream();
            File file = new File(SAVE_PATH);
            if (!file.exists() && !file.mkdirs()) {
                return true;
            }
            savedUrl = SAVE_PATH + "/" + songs[0].getFileName();
            output = new FileOutputStream(savedUrl);
            byte[] data = new byte[4096];
            int count;
            while ((count = input.read(data)) != -1) {
                if (isCancelled()) {
                    input.close();
                    return true;
                }
                output.write(data, 0, count);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
                if (input != null) {
                    input.close();
                }
            } catch (IOException ignored) {
            }

            if (connection != null) {
                connection.disconnect();
            }
        }
        return false;
    }

    public interface DownloadCompeletedListener {
        void invoke(boolean error);
    }
}
