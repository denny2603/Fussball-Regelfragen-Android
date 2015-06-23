package de.simontenbeitel.regelfragen.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import de.simontenbeitel.regelfragen.ui.fragment.NavigationDrawerFragment;

/**
 * @author Simon Tenbeitel
 */
public abstract class NavigationDrawerActivity extends AppCompatActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    protected final int SINGLE_QUESTION_POSITION = 0;
    protected final int EXAM_POSITION = 1;
    protected final int SETTINGS_POSITION = 2;

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        switch (position) {
            case SINGLE_QUESTION_POSITION:
                Intent intent = new Intent(this, SingleQuestionActivity.class);
                startActivity(intent);
                break;
        }
    }

}
