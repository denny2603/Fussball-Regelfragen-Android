package de.simontenbeitel.regelfragen.domain.model.question;

/**
 * A multiple choice question like in well known quiz shows from tv.
 *
 * @author Simon Tenbeitel
 */
public class MultipleChoiceQuestion extends Question {

    private final String[] answerPossibilities;
    private final int solutionIndex;

    private int chosenAnswerIndex;

    public MultipleChoiceQuestion(long id, String text, String[] answerPossibilities, int solutionIndex) {
        super(id, text);
        this.answerPossibilities = answerPossibilities;
        this.solutionIndex = solutionIndex;
    }

    /**
     * Save the index of the user's answer
     *
     * @param index index of the chosen answer
     * @return true if index is within the allowed range
     */
    public boolean chooseAnswer(int index) {
        if (0 > index || answerPossibilities.length <= index)
            return false;
        chosenAnswerIndex = index;
        return true;
    }

    public int getChosenAnswerIndex() {
        return chosenAnswerIndex;
    }

    public int getSolutionIndex() {
        return solutionIndex;
    }

    public String[] getAnswerPossibilities() {
        return answerPossibilities;
    }

    @Override
    public double getFaults() {
        return solutionIndex == chosenAnswerIndex ? 0 : 1;
    }
}
