package de.simontenbeitel.regelfragen.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import de.simontenbeitel.regelfragen.network.QuestionLoadService;

/**
 * Created by Simon on 06.08.2015.
 */
public class StartupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent questionLoadServiceIntent = new Intent(this, QuestionLoadService.class);
        questionLoadServiceIntent.setData(Uri.parse("http://regelfragen.simon-tenbeitel.de/api"));
        startService(questionLoadServiceIntent);
        Intent i = new Intent(this, SingleQuestionActivity.class);
        startActivity(i);
    }

}
