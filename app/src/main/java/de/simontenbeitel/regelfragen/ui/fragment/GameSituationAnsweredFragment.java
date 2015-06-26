package de.simontenbeitel.regelfragen.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.simontenbeitel.regelfragen.R;
import de.simontenbeitel.regelfragen.objects.GameSituationQuestion;

/**
 * @author Simon Tenbeitel
 */
public class GameSituationAnsweredFragment extends QuestionFragment {

    @InjectView(R.id.restartMethod_chosen) TextView restartMethod_chosen;
    @InjectView(R.id.restartMethod_solution) TextView restartMethod_solution;
    @InjectView(R.id.restartMethod_correct) ImageView restartMethod_correct;
    @InjectView(R.id.positionOfRestart_chosen) TextView positionOfRestart_chosen;
    @InjectView(R.id.positionOfRestart_solution) TextView positionOfRestart_solution;
    @InjectView(R.id.positionOfRestart_correct) ImageView positionOfRestart_correct;
    @InjectView(R.id.disciplinarySanction_chosen) TextView disciplinarySanction_chosen;
    @InjectView(R.id.disciplinarySanction_solution) TextView disciplinarySanction_solution;
    @InjectView(R.id.disciplinarySanction_correct) ImageView disciplinarySanction_correct;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_gamesituation_answered, container, false);
        ButterKnife.inject(this, view);
        printContent();
        replaceAnswerContainer(rootView, view);
        return rootView;
    }

    private void printContent() {
        final GameSituationQuestion question = (GameSituationQuestion) mQuestion;
        printChosenAnswers(question);
        printSolutions(question);
    }

    private void printChosenAnswers(final GameSituationQuestion question) {
        restartMethod_chosen.setText(question.getChosenRestartMethod());
        positionOfRestart_chosen.setText(question.getChosenPositionOfRestart());
        disciplinarySanction_chosen.setText(question.getChosenDisciplinarySanction());
    }

    private void printSolutions(final GameSituationQuestion question) {
        question.printSolution(GameSituationQuestion.SpinnerPositions.RESTART_METHOD, restartMethod_solution, restartMethod_correct);
        question.printSolution(GameSituationQuestion.SpinnerPositions.POSITION_OF_RESTART, positionOfRestart_solution, positionOfRestart_correct);
        question.printSolution(GameSituationQuestion.SpinnerPositions.DISCIPLINARY_SANCTION, disciplinarySanction_solution, disciplinarySanction_correct);
    }


}
