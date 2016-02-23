package de.simontenbeitel.regelfragen.domain.model;

/**
 * Root class for all kinds of questions
 *
 * @author Simon Tenbeitel
 */
public abstract class Question {

    private final long id; //id in local db
    private final String text;

    public Question(String text, long id) {
        this.text = text;
        this.id = id;
    }

    public long getId() {
        return id;
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
