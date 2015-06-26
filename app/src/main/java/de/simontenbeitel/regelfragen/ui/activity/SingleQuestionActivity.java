package de.simontenbeitel.regelfragen.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.simontenbeitel.regelfragen.R;
import de.simontenbeitel.regelfragen.objects.GameSituationQuestion;
import de.simontenbeitel.regelfragen.objects.MultipleChoiceQuestion;
import de.simontenbeitel.regelfragen.objects.Question;
import de.simontenbeitel.regelfragen.ui.fragment.QuestionFragment;

/**
 * In this activity the user answers a question and gets the answer directly afterwards
 *
 * @author Simon Tenbeitel
 */
public class SingleQuestionActivity extends NavigationDrawerActivity {

    private static final String TAG_QUESTION_FRAGMENT = "question_fragment";
    private static final int numberOfQuestionsToLoadEachTime = 5;
    private static final int minBufferOfQuestions = 2;

    private FragmentManager mFragmentManager;
    private boolean answeredMode = false;
    private List<Question> mQuestions;
    private Set<Long> mAnsweredQuestions;

    @InjectView(R.id.question_navigation_button) Button navigationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singlequestion);
        ButterKnife.inject(this);
        mFragmentManager = getSupportFragmentManager();
        mQuestions = new ArrayList<>(numberOfQuestionsToLoadEachTime + minBufferOfQuestions);
        mAnsweredQuestions = new HashSet<>();

        /*
        Test with hardcoded questions
         */
        List<String> restartMethod = new ArrayList<>();
        restartMethod.add("Strafstoß");
        List<String> positionOfRestart = new ArrayList<>();
        positionOfRestart.add("11m Punkt");
        List<String> disciplinarySanction = new ArrayList<>();
        disciplinarySanction.add("Rot");
        Question questionG = new GameSituationQuestion("Ein Spieler spielt den Ball auf der eigenen Torlinie absichtlich mit der Hand und verhindert so, dass der Ball ins Tor geht. Entscheidung?", 1, restartMethod, positionOfRestart, disciplinarySanction);
        Question questionM = new MultipleChoiceQuestion("Ein Spieler spielt den Ball auf der eigenen Torlinie absichtlich mit der Hand und verhindert so, dass der Ball ins Tor geht. Entscheidung?", 2, new String[] {"weiterspielen", "ind. Freistoß", "dir. Freistoß", "Strafstoß, Feldverweis"}, 3);
        mQuestions.add(questionG);
        mQuestions.add(questionM);
        /* END OF TEST */

        showNextQuestion(); //todo normally would call loadNewQuestions, and show it once it's loaded
    }

    private void replaceQuestionFragment(final QuestionFragment fragment) {
        mFragmentManager.beginTransaction().replace(R.id.question_container, fragment, TAG_QUESTION_FRAGMENT).commit();
        setButtonText();
    }

    @OnClick(R.id.question_navigation_button)
    public void onNavigationButtonClick() {
        if (!answeredMode) {
            answeredMode = true;
            final Question question = mQuestions.get(0);
            final QuestionFragment fragment = QuestionFragment.newAnsweredFragment(question);
            replaceQuestionFragment(fragment);
            mAnsweredQuestions.add(question.getId());
            mQuestions.remove(0);
            loadNewQuestions();
        } else {
            showNextQuestion();
        }
    }

    private void setButtonText() {
        navigationButton.setText(answeredMode ? R.string.newQuestionButton : R.string.solveButton);
    }

    private void showNextQuestion() {
        if (mQuestions.isEmpty()) {
            // no new questions
        } else {
            final Question question = mQuestions.get(0);
            final QuestionFragment fragment = QuestionFragment.newInstance(question);
            answeredMode = false;
            replaceQuestionFragment(fragment);
        }
    }

    private void loadNewQuestions() {
        if (minBufferOfQuestions > mQuestions.size()) {
            //todo load from db
        }
    }

}
