package vn.edu.hcmute.mp.g4mediaplayer.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit client;

    private RetrofitClient() {
    }

    static Retrofit getInstance(final String baseUrl) {
        if (client == null) {
            client = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return client;
    }
}
