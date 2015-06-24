package de.simontenbeitel.regelfragen.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.simontenbeitel.regelfragen.R;

/**
 * Implementation of QuestionFragment for game situation questions
 *
 * @author Simon Tenbeitel
 */
public class GameSituationQuestionFragment extends QuestionFragment {

    @InjectView(R.id.spinner_restartMethod) Spinner restartMethod;
    @InjectView(R.id.spinner_positionOfRestart) Spinner positionOfRestart;
    @InjectView(R.id.spinner_disciplinarySanction) Spinner disciplinarySanction;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_gamesituation_question, container, false);
        ButterKnife.inject(this, view);
        replaceAnswerContainer(rootView, view);
        return rootView;
    }

}
