package de.simontenbeitel.regelfragen.domain.model;

import java.util.Collection;
import java.util.List;

/**
 * A question where a match situation is described. The user has to determine the correct referee's decision.
 * He has to answer if the match must be paused and if so how and where to restart the match. He also has to choose the disciplinary sanction.
 * @author Simon Tenbeitel
 */
public class MatchSituationQuestion extends Question {

    public enum Element {
        RESTART_METHOD,
        POSITION_OF_RESTART,
        DISCIPLINARY_SANCTION
    }

    // sets of correct answers for each category
    private final List<String> possibleRestartMethods;
    private final List<String> possiblePositionsOfRestart;
    private final List<String> possibleDisciplinarySanctions;

    // sets of correct answers for each category
    private final Collection<String> correctRestartMethods;
    private final Collection<String> correctPositionsOfRestart;
    private final Collection<String> correctDisciplinarySanctions;

    // answers given by the user
    private String chosenRestartMethod;
    private String chosenPositionOfRestart;
    private String chosenDisciplinarySanction;

    public MatchSituationQuestion(String text, long id,
                                  List<String> possibleRestartMethods, List<String> possiblePositionsOfRestart, List<String> possibleDisciplinarySanctions,
                                  Collection<String> correctRestartMethods, Collection<String> correctPositionsOfRestart, Collection<String> correctDisciplinarySanctions) {
        super(text, id);
        this.possibleRestartMethods = possibleRestartMethods;
        this.possiblePositionsOfRestart = possiblePositionsOfRestart;
        this.possibleDisciplinarySanctions = possibleDisciplinarySanctions;
        this.correctRestartMethods = correctRestartMethods;
        this.correctPositionsOfRestart = correctPositionsOfRestart;
        this.correctDisciplinarySanctions = correctDisciplinarySanctions;

        setInitialAnswers();
    }

    private void setInitialAnswers() {
        // todo: currently only German questions are supported, so edit this method once other languages become available
        chosenRestartMethod = "weiterspielen";
        chosenPositionOfRestart = "weiterspielen";
        chosenDisciplinarySanction = "keine persönliche Strafe";
    }

    public void chooseAnswer(Element element, String answer) {
        switch (element) {
            case RESTART_METHOD:
                chosenRestartMethod = answer;
                break;
            case POSITION_OF_RESTART:
                chosenPositionOfRestart = answer;
                break;
            case DISCIPLINARY_SANCTION:
                chosenDisciplinarySanction = answer;
                break;
        }
    }

    public String getChosenRestartMethod() {
        return chosenRestartMethod;
    }

    public String getChosenPositionOfRestart() {
        return chosenPositionOfRestart;
    }

    public String getChosenDisciplinarySanction() {
        return chosenDisciplinarySanction;
    }

    @Override
    public double getFaults() {
        boolean correctRestartMethod = correctRestartMethods.contains(chosenRestartMethod);
        boolean correctPositionOfRestart = null == chosenPositionOfRestart || this.correctPositionsOfRestart.contains(chosenPositionOfRestart);
        boolean correctDisciplinarySanction = correctDisciplinarySanctions.contains(chosenDisciplinarySanction);

        if (correctRestartMethod && correctPositionOfRestart) {
            if (correctDisciplinarySanction) {
                return 0;
            } else {
                return 0.5;
            }
        }
        return 1;
    }

}
