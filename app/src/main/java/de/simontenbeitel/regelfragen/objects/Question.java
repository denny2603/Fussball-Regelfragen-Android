package de.simontenbeitel.regelfragen.objects;

/**
 * Root class for all kinds of questions
 *
 * @author Simon Tenbeitel
 */
public abstract class Question {

    public String text;
    private long id; //id in local db

    public Question(String text, long id) {
        this.text = text;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public abstract double getFaults();

}
