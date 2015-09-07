package de.simontenbeitel.regelfragen.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;

import de.simontenbeitel.regelfragen.R;
import de.simontenbeitel.regelfragen.database.RegelfragenDatabase;
import de.simontenbeitel.regelfragen.network.QuestionLoadService;

/**
 * Created by Simon on 06.08.2015.
 */
public class StartupActivity extends AppCompatActivity {

    private MaterialDialog progressDialog;

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
                            startQuestionLoadService();
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
        SQLiteDatabase db = RegelfragenDatabase.getInstance().getReadableDatabase();
        Cursor cursor = db.query(RegelfragenDatabase.Tables.QUESTION, new String[]{BaseColumns._ID}, null, null, null, null, null, "1");
        return cursor.moveToFirst();
    }

    private void startQuestionLoadService() {
        progressDialog = new MaterialDialog.Builder(this)
                .title(R.string.loadingProgressTitle)
                .content(R.string.loadingProgressMessage)
                .progress(true, 0)
                .build();
        progressDialog.show();

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        broadcastManager.registerReceiver(new QuestionLoadBroadcastReceiver(this), new IntentFilter(QuestionLoadService.ACTION));

        Intent questionLoadServiceIntent = new Intent(this, QuestionLoadService.class);
        questionLoadServiceIntent.setData(Uri.parse("http://regelfragen.simon-tenbeitel.de/api"));
        startService(questionLoadServiceIntent);
    }

    private void startMainActivity() {
        Intent i = new Intent(this, SingleQuestionActivity.class);
        startActivity(i);
        finish();
    }

    class QuestionLoadBroadcastReceiver extends BroadcastReceiver{

        private Context mContext;

        public QuestionLoadBroadcastReceiver(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            progressDialog.cancel();
            if (intent.getBooleanExtra(QuestionLoadService.KEY_SUCCESSFUL, false)) {
                startMainActivity();
            } else {
                AlertDialog dialog = new AlertDialog.Builder(mContext)
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
