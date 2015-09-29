package de.simontenbeitel.regelfragen.ui.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import de.simontenbeitel.regelfragen.R;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}
