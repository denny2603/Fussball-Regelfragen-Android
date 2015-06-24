package de.simontenbeitel.regelfragen.ui.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.simontenbeitel.regelfragen.R;
import de.simontenbeitel.regelfragen.database.QuestionDatabase;
import de.simontenbeitel.regelfragen.database.task.AnswerPossibilitiesGameSituationLoadTask;

/**
 * Implementation of QuestionFragment for game situation questions
 *
 * @author Simon Tenbeitel
 */
public class GameSituationQuestionFragment extends QuestionFragment implements AdapterView.OnItemSelectedListener {

    @InjectView(R.id.spinner_restartMethod) Spinner restartMethod;
    @InjectView(R.id.spinner_positionOfRestart) Spinner positionOfRestart;
    @InjectView(R.id.spinner_disciplinarySanction) Spinner disciplinarySanction;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_gamesituation_question, container, false);
        ButterKnife.inject(this, view);
        new AnswerPossibilitiesGameSituationLoadTask(mQuestion.getId(), this).execute(0, 1, 2);
        replaceAnswerContainer(rootView, view);
        return rootView;
    }

    public void fillSpinner(Cursor[] answerPossibilitiesCursors) {
        final String[] projection = new String[]{QuestionDatabase.AnswerColumns.TEXT};
        SimpleCursorAdapter[] adapters = new SimpleCursorAdapter[answerPossibilitiesCursors.length];
        for (int index = 0; index < answerPossibilitiesCursors.length; index++) {
            adapters[index] = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_spinner_item, answerPossibilitiesCursors[index], projection, new int[]{android.R.id.text1}, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            adapters[index].setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        }
        restartMethod.setAdapter(adapters[0]);
        if (3 == adapters.length) {
            positionOfRestart.setAdapter(adapters[1]);
            disciplinarySanction.setAdapter(adapters[2]);
        } else disciplinarySanction.setAdapter(adapters[1]);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
