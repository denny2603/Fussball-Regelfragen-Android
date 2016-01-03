package de.simontenbeitel.regelfragen.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.ListAdapter;

import java.util.List;

import de.simontenbeitel.regelfragen.R;
import de.simontenbeitel.regelfragen.objects.Question;
import de.simontenbeitel.regelfragen.ui.fragment.AnsweredQuestionFragment;
import de.simontenbeitel.regelfragen.ui.fragment.AssessedQuestionListFragment;

/**
 * @author Simon Tenbeitel
 */
public class AssessedExamActivity extends NavigationDrawerActivity implements AssessedQuestionListFragment.Callbacks{

    public static final String QUESTION_LIST_EXTRA = "question_list";

    public static final String TAG_QUESTION_LIST_FRAGMENT = "question_list_fragment";

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessed_exam);
        setUpNavigationDrawer();

        FragmentManager mFragmentManager = getSupportFragmentManager();
        if (null == mFragmentManager.findFragmentByTag(TAG_QUESTION_LIST_FRAGMENT)) {
            Intent intent = getIntent();
            List<Question> questions = (List<Question>) intent.getSerializableExtra(QUESTION_LIST_EXTRA);
            AssessedQuestionListFragment fragment = AssessedQuestionListFragment.newInstance(questions);
            mFragmentManager.beginTransaction()
                    .add(R.id.assessed_question_list, fragment, TAG_QUESTION_LIST_FRAGMENT)
                    .commit();
        }

        if (null != findViewById(R.id.assessed_question_detail_container)) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

//            onItemSelected(0);
        }

        isCreated = true;
    }

    public boolean isTwoPane() {
        return mTwoPane;
    }

    /**
     * Callback method from {@link AssessedQuestionListFragment.Callbacks}
     * indicating that the item at the given position was selected.
     */
    @Override
    public void onItemSelected(int position) {
        AssessedQuestionListFragment listFragment = (AssessedQuestionListFragment)getSupportFragmentManager().findFragmentById(R.id.assessed_question_list);
        ListAdapter listAdapter = listFragment.getListAdapter();
        Question question = (Question) listAdapter.getItem(position);

        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by adding or
            // replacing the detail fragment using a fragment transaction.
            AnsweredQuestionFragment questionFragment = AnsweredQuestionFragment.newAnsweredFragment(question);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.assessed_question_detail_container, questionFragment)
                    .commit();
        } else {
            // In single-pane mode, simply start the detail activity for the selected question.
            Intent detailIntent = new Intent(this, AssessedQuestionActivity.class);
            detailIntent.putExtra(AssessedQuestionActivity.QUESTION_NUMBER_EXTRA, (position + 1));
            detailIntent.putExtra(AssessedQuestionActivity.QUESTION_EXTRA, question);
            startActivity(detailIntent);
        }
    }

}
