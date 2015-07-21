package de.simontenbeitel.regelfragen.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * @author Simon Tenbeitel
 */
public class RegelfragenRestAdapter {

    private static RestAdapter sRestAdapter;
    private static RegelfragenApi sApi;

    public static RegelfragenApi getApi(String url) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
        sRestAdapter = new RestAdapter.Builder()
                .setEndpoint(url)
                .setConverter(new GsonConverter(gson))
                .build();
        sApi = sRestAdapter.create(RegelfragenApi.class);
        return sApi;
    }

}
