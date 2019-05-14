package vn.edu.hcmute.mp.g4mediaplayer.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import vn.edu.hcmute.mp.g4mediaplayer.api.model.Song;

public interface SongsService {

    @GET("/api/songs/take/{count}")
    Call<List<Song>> getSongs(@Path("count") Integer count);

    @GET("/api/songs/{id}")
    Call<Song> getSong(@Path("id") long id);
}
