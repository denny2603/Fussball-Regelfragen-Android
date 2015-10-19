package de.simontenbeitel.regelfragen.objects;

import android.content.Context;
import android.widget.TextView;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import de.simontenbeitel.regelfragen.R;
import de.simontenbeitel.regelfragen.RegelfragenApplication;

/**
 * Root class for all kinds of questions
 *
 * @author Simon Tenbeitel
 */
public abstract class Question implements Serializable {

    private static final long serialVersionUID = 8409085040398009802L;
    private static final NumberFormat numberFormat = new DecimalFormat();//"##.#");

    public final String text;
    private final long id; //id in local db

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

    /**
     * Get the name of the question type.
     *
     * @return {@link GameSituationQuestion} or {@link MultipleChoiceQuestion} as String
     */
    public abstract String getQuestionTypeName();

    public void printFaults(TextView faultsTextView) {
        double faults = getFaults();
        Context context = RegelfragenApplication.getContext();
        String faultsString = context.getString(R.string.faults);
        faultsString += ": " + numberFormat.format(faults);
        faultsTextView.setText(faultsString);
    }

}
