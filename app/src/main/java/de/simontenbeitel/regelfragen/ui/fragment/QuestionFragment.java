package de.simontenbeitel.regelfragen.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.simontenbeitel.regelfragen.R;
import de.simontenbeitel.regelfragen.objects.Question;

/**
 * Abstract class for the different kinds of question fragments
 *
 * @author Simon Tenbeitel
 */
public abstract class QuestionFragment extends Fragment {

    public static final String TAG_QUESTION_EXTRA = "question";

    protected Question mQuestion;

    static class ViewHolder {
        @InjectView(R.id.question_text) TextView question_text;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQuestion = (Question) getArguments().getSerializable(TAG_QUESTION_EXTRA);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        ViewHolder holder = new ViewHolder(view);
        holder.question_text.setText(mQuestion.text);
        return view;
    }

    protected void replaceAnswerContainer(View container, View answerView) {
        ViewGroup myParent = (ViewGroup) container.getRootView();
        if (null == myParent)
            throw new RuntimeException("No parent view provided");

        View answerContainer = container.findViewById(R.id.answer_container);
        if (null == answerContainer)
            throw new RuntimeException("Cannot find answer container");

        final int answerContainerIndex = myParent.indexOfChild(answerContainer);
        myParent.removeView(answerContainer);
        myParent.removeView(answerView);
        myParent.addView(answerView, answerContainerIndex);
    }

}
