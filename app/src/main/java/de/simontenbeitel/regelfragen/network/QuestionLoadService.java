package de.simontenbeitel.regelfragen.network;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import de.simontenbeitel.regelfragen.database.RegelfragenDatabase;

/**
 * @author Simon Tenbeitel
 */
public class QuestionLoadService extends IntentService {

    public QuestionLoadService() {
        super(QuestionLoadService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String url = intent.getDataString();
        RegelfragenApi api = RegelfragenRestAdapter.getApi(url);
        long start = System.currentTimeMillis();
        Log.d(QuestionLoadService.class.getName(), "Start downloading questions");
        RegelfragenApiJsonObjects.QuestionResponse response = api.getQuestions();
        Log.d(QuestionLoadService.class.getName(), "Downloaded questions in " + (System.currentTimeMillis() - start) + " millis");

        SQLiteDatabase db = RegelfragenDatabase.getInstance().getWritableDatabase();
        long serverId = getServerId(db, url);
        if (0 > serverId) {
            Log.e(QuestionLoadService.class.getName(), "Server URL is not in local db: " + url);
            return;
        }
        start = System.currentTimeMillis();
        Log.d(QuestionLoadService.class.getName(), "Start db transaction");
        db.beginTransaction();
        try {

        } finally {
            db.endTransaction();
        }
    }

    private long getServerId(SQLiteDatabase db, String url) {
        Cursor cursor = db.query(RegelfragenDatabase.Tables.SERVER, new String[] {BaseColumns._ID}, RegelfragenDatabase.ServerColumns.URL + "=?", new String[] {url}, null, null, null);
        if (cursor.moveToFirst()) {
            return cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));
        }
        return -1;
    }

    private void insertLastUpdatedTimeStamp() {

    }

}
