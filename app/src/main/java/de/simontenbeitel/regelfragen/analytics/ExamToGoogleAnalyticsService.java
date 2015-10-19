package de.simontenbeitel.regelfragen.analytics;

import android.app.IntentService;
import android.content.Intent;

import com.google.android.gms.analytics.Tracker;

import java.util.List;

import de.simontenbeitel.regelfragen.RegelfragenApplication;
import de.simontenbeitel.regelfragen.objects.Question;

/**
 * {@link IntentService} to send data from exam to Google Analytics in background.
 *
 * @author Simon Tenbeitel
 */
public class ExamToGoogleAnalyticsService extends IntentService {

    public static final String QUESTION_LIST_EXTRA = "question_list";

    private List<Question> mQuestions;

    private Tracker mTracker;

    public ExamToGoogleAnalyticsService() {
        super(ExamToGoogleAnalyticsService.class.getName());

        // Obtain the shared Tracker instance.
        mTracker = RegelfragenApplication.getDefaultTracker();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mQuestions = (List<Question>) intent.getSerializableExtra(QUESTION_LIST_EXTRA);

        for (Question question : mQuestions) {
            GoogleAnalyticsHelper.sendQuestionEvent(question);
        }
    }

}
