package de.simontenbeitel.regelfragen.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.List;

import de.simontenbeitel.regelfragen.objects.Question;

/**
 * Used to retain questions of ExamActivity and SingleQuestionActivity during configuration changes
 * See: http://developer.android.com/intl/ja/guide/topics/resources/runtime-changes.html#RetainingAnObject
 *
 * @author Simon Tenbeitel
 */
public class QuestionListRetainedFragment extends Fragment {

    public static final String TAG_QUESTION_LIST_RETAINED_FRAGMENT = "question_list_retained_fragment";

    private List<Question> mQuestions = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }

    public void setQuestions(List<Question> questions) {
        this.mQuestions = questions;
    }

    public List<Question> getQuestions() {
        return mQuestions;
    }

}
