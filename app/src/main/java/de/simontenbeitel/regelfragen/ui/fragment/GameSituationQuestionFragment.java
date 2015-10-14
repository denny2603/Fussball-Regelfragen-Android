package de.simontenbeitel.regelfragen.ui.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.simontenbeitel.regelfragen.R;
import de.simontenbeitel.regelfragen.database.RegelfragenDatabase;
import de.simontenbeitel.regelfragen.database.task.AnswerPossibilitiesGameSituationLoadTask;
import de.simontenbeitel.regelfragen.objects.GameSituationQuestion;

/**
 * Implementation of QuestionFragment for game situation questions
 *
 * @author Simon Tenbeitel
 */
public class GameSituationQuestionFragment extends QuestionFragment implements AdapterView.OnItemSelectedListener {

    @Bind(R.id.spinner_restartMethod) Spinner restartMethod;
    @Bind(R.id.spinner_positionOfRestart) Spinner positionOfRestart;
    @Bind(R.id.spinner_disciplinarySanction) Spinner disciplinarySanction;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_gamesituation_question, container, false);
        ButterKnife.bind(this, view);
        new AnswerPossibilitiesGameSituationLoadTask(mQuestion.getId(), this).execute(0, 1, 2); // TODO: 05.10.2015 check if position of restart is active
        replaceAnswerContainer(rootView, view);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void fillSpinner(Cursor[] answerPossibilitiesCursors) {
        Context context = getContext();
        if (null == context) {
            return;
        }
        final String[] projection = new String[]{RegelfragenDatabase.AnswerColumns.TEXT};
        SimpleCursorAdapter[] adapters = new SimpleCursorAdapter[answerPossibilitiesCursors.length];
        for (int index = 0; index < answerPossibilitiesCursors.length; index++) {
            adapters[index] = new SimpleCursorAdapter(context, android.R.layout.simple_spinner_item, answerPossibilitiesCursors[index], projection, new int[]{android.R.id.text1}, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            adapters[index].setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        }

        // set adapters and set selected items
        GameSituationQuestion question = (GameSituationQuestion) mQuestion;
        restartMethod.setAdapter(adapters[0]);
        restartMethod.setSelection(getSelectionPosition(adapters[0], question.getChosenRestartMethod()));
        if (3 == adapters.length) {
            positionOfRestart.setAdapter(adapters[1]);
            positionOfRestart.setSelection(getSelectionPosition(adapters[1], question.getChosenPositionOfRestart()));
            disciplinarySanction.setAdapter(adapters[2]);
            disciplinarySanction.setSelection(getSelectionPosition(adapters[2], question.getChosenDisciplinarySanction()));
        } else {
            disciplinarySanction.setAdapter(adapters[1]);
            disciplinarySanction.setSelection(getSelectionPosition(adapters[1], question.getChosenDisciplinarySanction()));
        }

        // set onItemSelectedListeners after spinners are filled, because otherwise the onItemSelected method would be called after filling the spinners
        restartMethod.setOnItemSelectedListener(this);
        positionOfRestart.setOnItemSelectedListener(this);
        disciplinarySanction.setOnItemSelectedListener(this);
    }

    private int getSelectionPosition(SimpleCursorAdapter adapter, String item) {
        int cursorPosition = -1;
        Cursor cursor = adapter.getCursor();
        for (int position = 0; position < adapter.getCount(); position++) {
            cursor.moveToPosition(position);
            String cursorValue = cursor.getString(cursor.getColumnIndex(RegelfragenDatabase.AnswerColumns.TEXT));
            if (item.contentEquals(cursorValue)) {
                cursorPosition = position;
                break;
            }
        }
        return cursorPosition;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = (Cursor) parent.getSelectedItem();
        GameSituationQuestion question = (GameSituationQuestion) mQuestion;
        String answer = cursor.getString(cursor.getColumnIndex(RegelfragenDatabase.AnswerColumns.TEXT));
        if (restartMethod == parent) {
            question.answerQuestion(GameSituationQuestion.SpinnerPositions.RESTART_METHOD, answer);
        } else if (positionOfRestart == parent) {
            question.answerQuestion(GameSituationQuestion.SpinnerPositions.POSITION_OF_RESTART, answer);
        } else if (disciplinarySanction == parent) {
            question.answerQuestion(GameSituationQuestion.SpinnerPositions.DISCIPLINARY_SANCTION, answer);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
