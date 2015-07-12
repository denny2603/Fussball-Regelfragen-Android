package de.simontenbeitel.regelfragen.ui.activity;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

import de.simontenbeitel.regelfragen.R;
import de.simontenbeitel.regelfragen.ui.fragment.NavigationDrawerFragment;

/**
 * @author Simon Tenbeitel
 */
public abstract class NavigationDrawerActivity extends AppCompatActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    protected final int SINGLE_QUESTION_POSITION = 0;
    protected final int EXAM_POSITION = 1;
    protected final int SETTINGS_POSITION = 2;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    protected NavigationDrawerFragment mNavigationDrawerFragment;

    protected void setUpNavigationDrawer() {
        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
    }

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
