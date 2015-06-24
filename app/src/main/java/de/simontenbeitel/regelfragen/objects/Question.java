package de.simontenbeitel.regelfragen.objects;

import java.io.Serializable;

/**
 * Root class for all kinds of questions
 *
 * @author Simon Tenbeitel
 */
public abstract class Question  implements Serializable {

    private static final long serialVersionUID = -6718157748174058765L;

    public String text;
    private long id; //id in local db

    public Question(String text, long id) {
        this.text = text;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    /**
     * Calculates the faults for this particular question
     *
     * @return 0 if everything is correct, 0.5 if only disciplinary sanction is wrong, 1 otherwise
     */
    public abstract double getFaults();

}
