package de.simontenbeitel.regelfragen.network;

import android.app.IntentService;
import android.content.Intent;

/**
 * @author Simon Tenbeitel
 */
public class QuestionLoadService extends IntentService {

    public QuestionLoadService() {
        super(QuestionLoadService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

}
