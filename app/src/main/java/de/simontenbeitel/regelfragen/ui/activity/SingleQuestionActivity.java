package de.simontenbeitel.regelfragen.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.simontenbeitel.regelfragen.R;
import de.simontenbeitel.regelfragen.database.task.QuestionLoadTask;
import de.simontenbeitel.regelfragen.database.task.SingleQuestionsLoadTask;
import de.simontenbeitel.regelfragen.objects.Question;
import de.simontenbeitel.regelfragen.ui.fragment.AnsweredQuestionFragment;
import de.simontenbeitel.regelfragen.ui.fragment.QuestionFragment;

/**
 * In this activity the user answers a question and gets the answer directly afterwards
 *
 * @author Simon Tenbeitel
 */
public class SingleQuestionActivity extends NavigationDrawerActivity implements QuestionLoadTask.QuestionLoadCallback {

    private static final String TAG_QUESTION_FRAGMENT = "question_fragment";
    private static final int numberOfQuestionsToLoadEachTime = 5;
    private static final int minBufferOfQuestions = 2;

    private FragmentManager mFragmentManager;
    private boolean answeredMode = false;
    private List<Question> mQuestions;
    private Set<Long> mLoadedQuestions;
    private boolean loadedAllQuestions = false;

    @InjectView(R.id.previous_question_navigation_button) Button navigationButton;
    @InjectView(R.id.questionScrollView) ScrollView questionScrollView;
    @InjectView(R.id.progress_wheel) ProgressWheel loadingCircleProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singlequestion);
        setUpNavigationDrawer();
        ButterKnife.inject(this);
        mFragmentManager = getSupportFragmentManager();
        mQuestions = new ArrayList<>(numberOfQuestionsToLoadEachTime + minBufferOfQuestions);
        mLoadedQuestions = new HashSet<>();
        loadNewQuestions();

        isCreated = true;
    }

    private void replaceQuestionFragment(final QuestionFragment fragment) {
        mFragmentManager.beginTransaction().replace(R.id.question_container, fragment, TAG_QUESTION_FRAGMENT).commit();
        setButtonText();
    }

    private void showLoadingCircle(boolean show) {
        loadingCircleProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        questionScrollView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @OnClick(R.id.previous_question_navigation_button)
    public void onNavigationButtonClick() {
        if (!answeredMode) {
            answeredMode = true;
            final Question question = mQuestions.get(0);
            final QuestionFragment fragment = AnsweredQuestionFragment.newAnsweredFragment(question);
            replaceQuestionFragment(fragment);
            mLoadedQuestions.add(question.getId());
            mQuestions.remove(0);
            if (minBufferOfQuestions > mQuestions.size() && !loadedAllQuestions) loadNewQuestions();
        } else {
            showNextQuestion();
        }
    }

    private void setButtonText() {
        navigationButton.setText(answeredMode ? R.string.newQuestionButton : R.string.solveButton);
    }

    private void showNextQuestion() {
        if (mQuestions.isEmpty()) {
            if (loadedAllQuestions) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.noMoreQuestionsMessage)
                        .setTitle(R.string.noMoreQuestionsTitle);
                builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mLoadedQuestions = new HashSet<>();
                        loadNewQuestions();
                    }
                });
                builder.create().show();
            }
        } else {
            final Question question = mQuestions.get(0);
            final QuestionFragment fragment = QuestionFragment.newInstance(question);
            answeredMode = false;
            replaceQuestionFragment(fragment);
        }
    }

    private void loadNewQuestions() {
        if (mQuestions.isEmpty())
            showLoadingCircle(true);
        new SingleQuestionsLoadTask(this, numberOfQuestionsToLoadEachTime).execute(mLoadedQuestions.toArray(new Long[mLoadedQuestions.size()]));
    }

    @Override
    public void onQuestionsLoadFinished(List<Question> questions) {
        if (numberOfQuestionsToLoadEachTime > questions.size())
            loadedAllQuestions = true;
        for (Question question : questions) {
            mQuestions.add(question);
            mLoadedQuestions.add(question.getId());
        }

        if (mQuestions.size() == questions.size() && 0 < mQuestions.size()) {
            showNextQuestion();
            showLoadingCircle(false);
        }
    }

}
