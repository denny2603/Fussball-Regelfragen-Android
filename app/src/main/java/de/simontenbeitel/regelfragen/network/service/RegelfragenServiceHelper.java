package de.simontenbeitel.regelfragen.network.service;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import java.util.HashSet;
import java.util.Set;

import de.simontenbeitel.regelfragen.RegelfragenApplication;
import de.simontenbeitel.regelfragen.database.RegelfragenContract;
import de.simontenbeitel.regelfragen.network.processor.Processor;

/**
 * @author Simon Tenbeitel
 */
public class RegelfragenServiceHelper extends ServiceHelper {

    private static RegelfragenServiceHelper sInstance;

    private RegelfragenServiceHelper(Context context, int providerId) {
        super(context, providerId);
    }

    public static synchronized RegelfragenServiceHelper getInstance() {
        if (null == sInstance) {
            sInstance = new RegelfragenServiceHelper(RegelfragenApplication.getContext(), Processor.Providers.REGELFRAGEN);
        }
        return sInstance;
    }

    public Set<Long> loadQuestionsFromAllServers() {
        ContentResolver contentResolver = mContext.getContentResolver();
        Cursor serversCursor = contentResolver.query(RegelfragenContract.Server.URI,
                new String[]{RegelfragenContract.Server.URL},
                null, null, null);
        Set<Long> requestIds = new HashSet<>(serversCursor.getCount());
        if (serversCursor.moveToFirst()) {
            do {
                String serverUrl = serversCursor.getString(serversCursor.getColumnIndex(RegelfragenContract.Server.URL));
                requestIds.add(loadQuestionsFromServer(serverUrl));
            } while (serversCursor.moveToNext());
            serversCursor.close();
        }
        return requestIds;
    }

    public long loadQuestionsFromServer(String serverUrl) {
        Bundle extras = new Bundle();
        extras.putString(RegelfragenContract.Server.URL, serverUrl);
        return runMethod(Processor.Methods.LOAD_QUESTIONS, extras);
    }

}
