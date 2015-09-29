package de.simontenbeitel.regelfragen.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.Set;

import de.simontenbeitel.regelfragen.R;
import de.simontenbeitel.regelfragen.database.RegelfragenDatabase;
import de.simontenbeitel.regelfragen.network.processor.Processor;
import de.simontenbeitel.regelfragen.network.service.RegelfragenServiceHelper;
import de.simontenbeitel.regelfragen.network.service.ServiceHelper;

/**
 * Created by Simon on 06.08.2015.
 */
public class StartupActivity extends AppCompatActivity implements ServiceHelper.RequestListener {

    private MaterialDialog mProgressDialog;
    private Set<Long> mQuestionLoadRequestIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!areQuestionsLoaded()) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.noQuestionsDialogTitle)
                    .setMessage(R.string.noQuestionsDialogMessage)
                    .setPositiveButton(R.string.noQuestionsDialogDownload, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            loadQuestions();
                        }
                    })
                    .setNegativeButton(R.string.noQuestionsDialogExit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .create();
            dialog.show();
        } else {
            startMainActivity();
        }
    }

    private boolean areQuestionsLoaded() {
        RegelfragenDatabase dbInstance = RegelfragenDatabase.getInstance();
        SQLiteDatabase db = dbInstance.getReadableDatabase();
        Cursor cursor = db.query(RegelfragenDatabase.Tables.QUESTION, new String[]{BaseColumns._ID}, null, null, null, null, null, "1");
        boolean hasCursorEntries = cursor.moveToFirst();
        cursor.close();
        return hasCursorEntries;
    }

    private void loadQuestions() {
        mProgressDialog = new MaterialDialog.Builder(this)
                .title(R.string.loadingProgressTitle)
                .content(R.string.loadingProgressMessage)
                .progress(true, 0)
                .build();
        mProgressDialog.show();

        RegelfragenServiceHelper serviceHelper = RegelfragenServiceHelper.getInstance();
        serviceHelper.registerListener(this);
        mQuestionLoadRequestIds = serviceHelper.loadQuestionsFromAllServers();
        Log.d(StartupActivity.class.getName(), "Question load request ids: " + mQuestionLoadRequestIds.toString());
    }

    private void startMainActivity() {
        Intent i = new Intent(this, SingleQuestionActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onRequestFinished(long requestId, Bundle result) {
        boolean successful = result.getBoolean(Processor.Extras.RESULT_SUCCESSFUL);
        Log.d(StartupActivity.class.getName(), "Finished loading questions request with id " + requestId + " " + (successful ? "successfully" : "unsuccessfully"));
        mQuestionLoadRequestIds.remove(requestId);
        if (mQuestionLoadRequestIds.isEmpty()) {
            mProgressDialog.cancel();
            if (areQuestionsLoaded()) {
                startMainActivity();
            } else {
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle(R.string.loadUnsuccessfulTitle)
                        .setMessage(R.string.loadUnsuccessfulMessage)
                        .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .create();
                dialog.show();
            }
        }
    }

}
