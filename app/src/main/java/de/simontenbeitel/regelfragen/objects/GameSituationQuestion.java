package de.simontenbeitel.regelfragen.objects;

import android.widget.ImageView;
import android.widget.TextView;

import java.security.InvalidParameterException;
import java.util.List;

import de.simontenbeitel.regelfragen.R;

/**
 * @author Simon Tenbeitel
 */
public class GameSituationQuestion extends Question {

    public  interface SpinnerPositions{
        int RESTART_METHOD = 0;
        int POSITION_OF_RESTART = 1;
        int DISCIPLINARY_SANCTION = 2;
    }

    // sets of correct answers for each category
    private List<String> restartMethod;
    private List<String> positionOfRestart;
    private List<String> disciplinarySanction;

    // answers given by the user
    private String chosenRestartMethod;
    private String chosenPositionOfRestart;
    private String chosenDisciplinarySanction;

    public GameSituationQuestion(String text, long id, List<String> restartMethod, List<String> positionOfRestart, List<String> disciplinarySanction) {
        super(text, id);
        this.restartMethod = restartMethod;
        this.positionOfRestart = positionOfRestart;
        this.disciplinarySanction = disciplinarySanction;

        setInitialAnswers();
    }

    private void setInitialAnswers() {
        // todo: currently only German questions are supported, so edit this method once other languages become available
        chosenRestartMethod = "weiterspielen";
        chosenPositionOfRestart = "weiterspielen";
        chosenDisciplinarySanction = "keine persönliche Strafe";
    }

    public void answerQuestion(int position, String answer) {
        switch (position) {
            case SpinnerPositions.RESTART_METHOD:
                chosenRestartMethod = answer;
                break;
            case SpinnerPositions.POSITION_OF_RESTART:
                chosenPositionOfRestart = answer;
                break;
            case SpinnerPositions.DISCIPLINARY_SANCTION:
                chosenDisciplinarySanction = answer;
                break;
        }
    }

    @Override
    public double getFaults() {
        boolean correctRestartMethod = restartMethod.contains(chosenRestartMethod);
        boolean correctPositionOfRestart = positionOfRestart.contains(chosenPositionOfRestart); //todo what happens when .contains(null)? I want true, otherwise check first if chosen answer is null (then the user prefers to answer a question without position of restart)
        boolean correctDisciplinarySanction = disciplinarySanction.contains(chosenDisciplinarySanction);

        if (correctRestartMethod && correctPositionOfRestart) {
            if (correctDisciplinarySanction) {
                return 0;
            } else {
                return 0.5;
            }
        }
        return 1;
    }

    /**
     * Prints the solution to the given widgets
     *
     * @param position of which position should the answer be printed
     * @param textView all correct answers will be printed here
     * @param imageView indicate whether the chosen answer is correct
     */
    public void printSolution(int position, TextView textView, ImageView imageView) {
        List<String> solutions;
        switch (position) {
            case SpinnerPositions.RESTART_METHOD:
                boolean correctRestartMethod = restartMethod.contains(chosenRestartMethod);
                imageView.setImageResource(correctRestartMethod ? R.drawable.correct : R.drawable.wrong);
                solutions = restartMethod;
                break;
            case SpinnerPositions.POSITION_OF_RESTART:
                boolean correctPositionOfRestart = positionOfRestart.contains(chosenPositionOfRestart);
                imageView.setImageResource(correctPositionOfRestart ? R.drawable.correct : R.drawable.wrong);
                solutions = positionOfRestart;
                break;
            case SpinnerPositions.DISCIPLINARY_SANCTION:
                boolean correctDisciplinarySanction = disciplinarySanction.contains(chosenDisciplinarySanction);
                imageView.setImageResource(correctDisciplinarySanction ? R.drawable.correct : R.drawable.wrong);
                solutions = disciplinarySanction;
                break;
            default:
                throw new InvalidParameterException("Position must be a number between 0 and 2");
        }
        String solutionOutput = "";
        for (int i = 0; i < solutions.size(); i++) {
            if (i != 0)
                solutionOutput += "\n";
            solutionOutput += solutions.get(i);
        }
        textView.setText(solutionOutput);
    }

}
