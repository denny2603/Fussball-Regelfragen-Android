package de.simontenbeitel.regelfragen.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.simontenbeitel.regelfragen.R;
import de.simontenbeitel.regelfragen.objects.MultipleChoiceQuestion;

/**
 * Implementation of QuestionFragment for multiple choice questions
 *
 * @author Simon Tenbeitel
 */
public class MultipleChoiceQuestionFragment extends QuestionFragment implements RadioGroup.OnCheckedChangeListener {

    @InjectView(R.id.multipleChoiceQuestion_radioGroup) RadioGroup radioGroup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_multiplechoice_question, container, false);
        ButterKnife.inject(this, view);
        fillRadioGroup();
        replaceAnswerContainer(rootView, view);
        return rootView;
    }

    private void fillRadioGroup() {
        final MultipleChoiceQuestion question = (MultipleChoiceQuestion) mQuestion;
        final int numberOfAnswers = question.answerPossibilities.length;
        final RadioButton[] buttons = new RadioButton[numberOfAnswers];
        final Context context = getActivity();
        for (int i = 0; numberOfAnswers > i; i++) {
            buttons[i] = new RadioButton(context);
            buttons[i].setText(question.answerPossibilities[i]);
            buttons[i].setId(i + 1); // ID should be positive, so don't start with 0
            radioGroup.addView(buttons[i]);
        }
        radioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int id) {
        final int index = id - 1;
        final MultipleChoiceQuestion question = (MultipleChoiceQuestion) mQuestion;
        question.chooseAnswer(index);
    }
}
