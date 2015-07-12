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

    public static synchronized RegelfragenApi getApi() {
        if (null == sApi) {
            initialize();
        }
        return sApi;
    }

    private static void initialize() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
        sRestAdapter = new RestAdapter.Builder()
                .setEndpoint(RegelfragenApi.URL)
                .setConverter(new GsonConverter(gson))
                .build();
        sApi = sRestAdapter.create(RegelfragenApi.class);
    }

}
