package de.simontenbeitel.regelfragen.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import java.util.List;

import de.simontenbeitel.regelfragen.objects.Question;

/**
 * @author Simon Tenbeitel
 */
public class ExamActivity extends NavigationDrawerActivity{

    private FragmentManager mFragmentManager;
    private boolean answeredMode = false;
    private List<Question> mQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isCreated = true;
    }

}
