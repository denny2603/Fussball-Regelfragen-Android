package de.simontenbeitel.regelfragen.objects;

/**
 * @author Simon Tenbeitel
 */
public class MultipleChoiceQuestion extends Question {

    public String[] answerPossibilities;
    private int solutionIndex;

    private int chosenAnswerIndex;

    public MultipleChoiceQuestion(String text, long id, String[] answerPossibilities, int solutionIndex) {
        super(text, id);
        this.answerPossibilities = answerPossibilities;
        this.solutionIndex = solutionIndex;
    }

    /**
     * Save the index of the users answer
     *
     * @param index index of the chosen answer
     * @return true if index is within the range
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

    @Override
    public double getFaults() {
        return solutionIndex == chosenAnswerIndex ? 0 : 1;
    }

    public int getSolutionIndex() {
        return solutionIndex;
    }
}
