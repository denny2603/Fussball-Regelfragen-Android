package de.simontenbeitel.regelfragen;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Einstellungen extends PreferenceActivity {
	
	@SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.einstellungen);
    }

	@Override
	public void onBackPressed()
	{
		boolean anwaerter_active = true;
		boolean normal_active = true;
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean limit_difficulties = sharedPrefs.getBoolean("pref_key_limit_difficulties", false);
        if(limit_difficulties)
        {
        	anwaerter_active = sharedPrefs.getBoolean("pref_key_anwaerter_active", true);
        	normal_active = sharedPrefs.getBoolean("pref_key_normal_active", true);
        }
        if (anwaerter_active==false && normal_active==false)
        {
        	new AlertDialog.Builder(this)
		    .setMessage("Bitte mindestens einen Schwierigkeitsgrad auswählen, oder die Beschränkung aufheben")
		    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// Nichts machen, da der Nutzer an den Einstellungen arbeiten muss					
				}		        
		     }).show();
        }
        else
        {
        	finish();
        }
	}
}
