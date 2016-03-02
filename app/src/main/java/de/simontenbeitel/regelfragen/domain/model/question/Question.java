package de.simontenbeitel.regelfragen.domain.model.question;

import de.simontenbeitel.regelfragen.domain.model.Model;

/**
 * Root class for all kinds of questions
 *
 * @author Simon Tenbeitel
 */
public abstract class Question extends Model {

    private final String text;

    public Question(long id, String text) {
        super(id);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    /**
     * Calculates the faults for this question
     *
     * @return 0 if everything is correct, 0.5 if only disciplinary sanction is wrong, 1 otherwise
     */
    public abstract double getFaults();

}
