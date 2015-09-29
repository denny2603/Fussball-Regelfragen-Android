package de.simontenbeitel.regelfragen.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * @author Simon Tenbeitel
 */
public class RegelfragenRestAdapter {

    private static final HashMap<String, RegelfragenApi> sApis = new HashMap<>();

    public static synchronized RegelfragenApi getApi(String url) {
        if (!sApis.containsKey(url)) {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(url)
                    .setConverter(new GsonConverter(gson))
                    .build();
            RegelfragenApi api = restAdapter.create(RegelfragenApi.class);
            sApis.put(url, api);
        }
        return sApis.get(url);
    }

}
