package de.simontenbeitel.regelfragen.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.simontenbeitel.regelfragen.R;
import de.simontenbeitel.regelfragen.objects.MultipleChoiceQuestion;

/**
 * @author Simon Tenbeitel
 */
public class MultipleChoiceAnsweredFragment extends AnsweredQuestionFragment {

    @Bind(R.id.multipleChoiceAnswerContainer) LinearLayout answerContainer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_multiplechoice_answered, container, false);
        ButterKnife.bind(this, view);
        printAnswers(inflater, container);
        replaceAnswerContainer(rootView, view);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void printAnswers(LayoutInflater inflater, ViewGroup container) {
        final MultipleChoiceQuestion question = (MultipleChoiceQuestion) mQuestion;
        final String[] answers = question.answerPossibilities;
        final int solutionIndex = question.getSolutionIndex();
        final int chosenAnswerIndex = question.getChosenAnswerIndex();
        for (int i = 0; answers.length > i; i++) {
            View view = inflater.inflate(R.layout.row_multiplechoiceanswer_solution, container, false);
            TextView answerText = (TextView) view.findViewById(R.id.answerText);
            answerText.setText(answers[i]);
            ImageView correctImage = (ImageView) view.findViewById(R.id.correctImage);
            if (solutionIndex == i) {
                correctImage.setImageResource(R.drawable.correct);
            } else if (chosenAnswerIndex == i) {
                correctImage.setImageResource(R.drawable.wrong);
            } else {
                correctImage.setImageDrawable(null);
            }
            answerContainer.addView(view);
        }
    }

}
