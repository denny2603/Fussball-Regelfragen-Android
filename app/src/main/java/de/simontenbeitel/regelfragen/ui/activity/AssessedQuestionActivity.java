package de.simontenbeitel.regelfragen.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import de.simontenbeitel.regelfragen.R;
import de.simontenbeitel.regelfragen.objects.Question;
import de.simontenbeitel.regelfragen.ui.fragment.AnsweredQuestionFragment;

/**
 * @author Simon Tenbeitel
 */
public class AssessedQuestionActivity extends AppCompatActivity {

    public static final String QUESTION_NUMBER_EXTRA = "question_number";

    public static final String QUESTION_EXTRA = "question";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessed_question);

        if (null == savedInstanceState) {
            Intent intent = getIntent();
            int questionNumber = intent.getIntExtra(QUESTION_NUMBER_EXTRA, 0);
            String title = String.format(getString(R.string.questionWithNumber), questionNumber);
            setTitle(title);

            // Create the detail fragment and add it to the activity using a fragment transaction.
            Question question = (Question) intent.getSerializableExtra(QUESTION_EXTRA);
            AnsweredQuestionFragment questionFragment = AnsweredQuestionFragment.newAnsweredFragment(question);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.assessed_question_detail_container, questionFragment)
                    .commit();
        }
    }

}
