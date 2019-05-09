package vn.edu.hcmute.mp.g4mediaplayer.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import vn.edu.hcmute.mp.g4mediaplayer.api.model.Song;

public interface SongsService {

    @GET("/songs")
    Call<List<Song>> getSongs();
}
