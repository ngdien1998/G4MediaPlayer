package vn.edu.hcmute.mp.g4mediaplayer.api;

import retrofit2.Retrofit;

public class ApiService {

    public static final String BASE_URL = "http://192.168.1.5:2211/";
    private static Retrofit client = RetrofitClient.getInstance(BASE_URL);

    private ApiService() {
    }

    public static SongsService getSongService() {
        return client.create(SongsService.class);
    }
}