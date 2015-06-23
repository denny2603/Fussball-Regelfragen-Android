package de.simontenbeitel.regelfragen.ui.activity;

import android.os.Bundle;

import de.simontenbeitel.regelfragen.R;

/**
 * In this activity the user answers a question and gets the answer directly afterwards
 *
 * @author Simon Tenbeitel
 */
public class SingleQuestionActivity extends NavigationDrawerActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singlequestion);
    }
}
