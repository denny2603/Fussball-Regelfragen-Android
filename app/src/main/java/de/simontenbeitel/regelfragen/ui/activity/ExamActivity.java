package de.simontenbeitel.regelfragen.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.simontenbeitel.regelfragen.R;
import de.simontenbeitel.regelfragen.RegelfragenApplication;
import de.simontenbeitel.regelfragen.analytics.ExamToGoogleAnalyticsService;
import de.simontenbeitel.regelfragen.database.task.ExamLoadTask;
import de.simontenbeitel.regelfragen.database.task.QuestionLoadTask;
import de.simontenbeitel.regelfragen.objects.Question;
import de.simontenbeitel.regelfragen.ui.fragment.QuestionFragment;
import de.simontenbeitel.regelfragen.ui.fragment.QuestionListRetainedFragment;
import de.simontenbeitel.regelfragen.ui.provider.ExamTimer;

/**
 * In this activity an exam is simulated.
 * The user has to answer all questions and gets the answers at the end, after he hands in the exam.
 * This are usually 30 questions per exam, but also 15 is quite common, or any other number is possible.
 *
 * @author Simon Tenbeitel
 */
public class ExamActivity extends NavigationDrawerActivity implements QuestionLoadTask.QuestionLoadCallback, ExamTimer.TimerListener {

    private static final String TAG_QUESTION_FRAGMENT = "question_fragment";
    private static final String KEY_POSITION = "position";

    private FragmentManager mFragmentManager;
    private List<Question> mQuestions;
    private int mPosition = 0; //current question

    @Bind(R.id.progress_wheel)
    ProgressWheel loadingCircleProgressBar;
    @Bind(R.id.questionScrollView)
    ScrollView questionScrollView;
    @Bind(R.id.examProgress_TextView)
    TextView examProgressTextView;
    @Bind(R.id.examTimer_TextView)
    TextView examTimerTextView;
    @Bind(R.id.previous_question_navigation_button)
    Button previousQuestionButton;
    @Bind(R.id.next_question_navigation_button)
    Button nextQuestionButton;

    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        setUpNavigationDrawer();
        ButterKnife.bind(this);
        showLoadingCircle(true);
        mFragmentManager = getSupportFragmentManager();

        if (null != savedInstanceState) {
            mPosition = savedInstanceState.getInt(KEY_POSITION);
        }

        // find the retained fragment on activity restarts
        QuestionListRetainedFragment dataFragment = (QuestionListRetainedFragment) mFragmentManager.findFragmentByTag(QuestionListRetainedFragment.TAG_QUESTION_LIST_RETAINED_FRAGMENT);
        // create the retained fragment and add it to fragment manager
        if (null == dataFragment) {
            dataFragment = new QuestionListRetainedFragment();
            mFragmentManager.beginTransaction().add(dataFragment, QuestionListRetainedFragment.TAG_QUESTION_LIST_RETAINED_FRAGMENT).commit();
        }

        mQuestions = dataFragment.getQuestions();
        if (null != mQuestions) {
            showLoadingCircle(false);
            ExamTimer.getTimer(this);
            showCurrentQuestion();
        } else {
            startExam();
        }

        // Obtain the shared Tracker instance.
        mTracker = RegelfragenApplication.getDefaultTracker();

        isCreated = true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Log opening this screen in Google Analytics
        mTracker.setScreenName(ExamActivity.class.getName());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_POSITION, mPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
            ExamTimer.destroy();
        } else {
            // configuration change
        }
    }

    private void startExam() {
        new ExamLoadTask(this).execute();
    }

    private void replaceQuestionFragment(final QuestionFragment fragment) {
        mFragmentManager.beginTransaction().replace(R.id.question_container, fragment, TAG_QUESTION_FRAGMENT).commit();
    }

    @OnClick(R.id.next_question_navigation_button)
    public void showNextQuestion() {
        mPosition++;
        if (mQuestions.size() == mPosition) {
            showHandInDialog();
            return;
        }
        showCurrentQuestion();
    }

    @OnClick(R.id.previous_question_navigation_button)
    public void showPreviousQuestion() {
        mPosition--;
        showCurrentQuestion();
    }

    private void showLoadingCircle(boolean show) {
        loadingCircleProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        examProgressTextView.setVisibility(show ? View.GONE : View.VISIBLE);
        examTimerTextView.setVisibility(show ? View.GONE : View.VISIBLE);
        questionScrollView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void setNavigationButtons() {
        previousQuestionButton.setClickable(0 < mPosition);
        nextQuestionButton.setText(mQuestions.size() - 1 == mPosition ? R.string.handInButton : R.string.nextButton);
    }

    private void showCurrentQuestion() {
        setNavigationButtons();
        String progressText = String.format(getString(R.string.examProgressText), (mPosition + 1), mQuestions.size()); //question mPosition + 1 of mQuestions.size()
        final Question question = mQuestions.get(mPosition);
        final QuestionFragment fragment = QuestionFragment.newInstance(question);
        examProgressTextView.setText(progressText);
        replaceQuestionFragment(fragment);
    }

    private void showHandInDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.handInDialogTitle)
                .setMessage(R.string.handInDialogMessage)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handInExam();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPosition--;
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        mPosition--;
                    }
                })
                .create();
        dialog.show();
    }

    private void handInExam() {
        Intent serviceIntent = new Intent(this, ExamToGoogleAnalyticsService.class);
        serviceIntent.putExtra(ExamToGoogleAnalyticsService.QUESTION_LIST_EXTRA, (Serializable) mQuestions);
        startService(serviceIntent);

        Intent intent = new Intent(this, AssessedExamActivity.class);
        intent.putExtra(AssessedExamActivity.QUESTION_LIST_EXTRA, (Serializable) mQuestions);
        startActivity(intent);
        finish();
    }

    private void showExitExamDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.exitExamDialogTitle)
                .setMessage(R.string.exitExamDialogMessage)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create();
        dialog.show();
    }

    @Override
    public void onQuestionsLoadFinished(List<Question> questions) {
        if (!(questions instanceof Serializable)) {
            throw new RuntimeException("Questions list for exam must implement Serializable!");
        }
        mQuestions = questions;
        QuestionListRetainedFragment dataFragment = (QuestionListRetainedFragment) mFragmentManager.findFragmentByTag(QuestionListRetainedFragment.TAG_QUESTION_LIST_RETAINED_FRAGMENT);
        dataFragment.setQuestions(mQuestions);
        showLoadingCircle(false);
        showCurrentQuestion();
        ExamTimer.getTimer(this);
    }

    @Override
    public void OnTimerTick(int seconds) {
        int remainingSeconds = (mQuestions.size()/* * 60*/) - seconds;
        if (0 > remainingSeconds) {
            return;
        } else if (0 == remainingSeconds) {
            showTimeElapsedDialog();
        }
        setRemainingTime(remainingSeconds);
    }

    private void setRemainingTime(int remainingSeconds) {
        Date date = new Date();
        date.setMinutes(remainingSeconds / 60);
        date.setSeconds(remainingSeconds % 60);
        String timeRemaining = new SimpleDateFormat("mm:ss").format(date);
        examTimerTextView.setText(timeRemaining);
    }

    private void showTimeElapsedDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.timeElapsedDialogTitle)
                .setMessage(R.string.timeElapsedDialogMessage)
                .setPositiveButton(R.string.timeElapsedDialogHandInButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handInExam();
                    }
                })
                .setNegativeButton(R.string.timeElapsedDialogContinueButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        examTimerTextView.setTextColor(Color.RED);
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        examTimerTextView.setTextColor(Color.RED);
                    }
                })
                .create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        showExitExamDialog();
    }
}
