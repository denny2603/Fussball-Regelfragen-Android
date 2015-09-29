package de.simontenbeitel.regelfragen.ui.activity;

import android.os.Bundle;

import de.simontenbeitel.regelfragen.R;
import de.simontenbeitel.regelfragen.ui.fragment.SettingsFragment;

/**
 * Global settings activity
 */
public class SettingsActivity extends NavigationDrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setUpNavigationDrawer();
        getFragmentManager().beginTransaction()
                .replace(R.id.content, new SettingsFragment())
                .commit();
        isCreated = true;
    }

}
