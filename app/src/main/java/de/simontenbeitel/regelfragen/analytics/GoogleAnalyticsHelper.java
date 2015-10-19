package de.simontenbeitel.regelfragen.analytics;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import de.simontenbeitel.regelfragen.RegelfragenApplication;
import de.simontenbeitel.regelfragen.database.RegelfragenContract;
import de.simontenbeitel.regelfragen.objects.Question;

/**
 * @author Simon Tenbeitel
 */
public class GoogleAnalyticsHelper {

    /**
     * To determine how many questions are being answered, send an event when the user answers a question.
     *
     * @param question the answered {@link Question}
     */
    public static void sendQuestionEvent(Question question) {
        Context context = RegelfragenApplication.getContext();
        ContentResolver contentResolver = context.getContentResolver();
        Cursor guidCursor = contentResolver.query(RegelfragenContract.Question.URI,
                new String[] {RegelfragenContract.Question.GUID},
                BaseColumns._ID + "=?",
                new String[] {Long.toString(question.getId())},
                null);
        String guid;
        if (guidCursor.moveToFirst()) {
            guid = guidCursor.getString(guidCursor.getColumnIndex(RegelfragenContract.Question.GUID));
        } else {
            guid = "GUID not found for question";
        }
        guidCursor.close();

        double faultsCompat = question.getFaults() * 2; // Track only integer values
        int faults = (int) faultsCompat;
        Log.i(GoogleAnalyticsHelper.class.getSimpleName(), "Send answered question event to Google Analytics with GUID " + guid);
        Tracker mTracker = RegelfragenApplication.getDefaultTracker();
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Questions Answered")
                .setAction(question.getQuestionTypeName())
                .setLabel(guid)
                .setValue(faults)
                .build());
    }

}
