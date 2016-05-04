package de.simontenbeitel.regelfragen;

import java.util.ArrayList;
import java.util.Random;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import de.simontenbeitel.regelfragen.activity.EinzelfragenActivity;

public class MainActivity extends Activity implements OnClickListener{
	Intent intent;
	Button btn1, btn11, btn12, btn14, btn15;
	Random random;
	ListView list;
	boolean limit_difficulties;
	boolean anwaerter_active;
	boolean normal_active;
	boolean in_bogenwahl;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);				//Keine Titelleiste
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);	//Hochkant Format
        setContentView(R.layout.activity_main_old);
        
        random = new Random();	//Random bereits hier initialisieren (da von Zeit abhängig!!!)
        
		initialisiere();
		
    }
    
    public void initialisiere()
    {
    	btn1 = (Button) findViewById(R.id.button1);			//Button 1: Prüfung starten
        btn1.setOnClickListener(this);
        btn11 = (Button) findViewById(R.id.button11);		//Button 11: Impressum aufrufen
        btn11.setOnClickListener(this);
        btn12 = (Button) findViewById(R.id.button12);		//Button 12: Einzelfragen-Modus starten
        btn12.setOnClickListener(this);
        btn14 = (Button) findViewById(R.id.button14);		//Button 14: Einstellungen
        btn14.setOnClickListener(this);
        btn15 = (Button) findViewById(R.id.button15);		//Button 15: Statistik
        btn15.setOnClickListener(this);
        in_bogenwahl = false;
    }




	@Override
	public void onClick(View arg0) {
		if (arg0==btn1 || arg0==btn12)
		{
			//Prüfung oder Einzelfrage, daher abfragen, wie die Einstellung ist bezüglich Schwierigkeitsgrade
			SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
	        limit_difficulties = sharedPrefs.getBoolean("pref_key_limit_difficulties", false);
	        if(limit_difficulties)
	        {
	        	anwaerter_active = sharedPrefs.getBoolean("pref_key_anwaerter_active", true);
	        	normal_active = sharedPrefs.getBoolean("pref_key_normal_active", true);
	        }
	        else
	        {
	        	anwaerter_active = true;
	        	normal_active = true;
	        }
		}
		if (arg0==btn1)		//Prüfungsbutton
		{
			
			new AlertDialog.Builder(this)
		    .setTitle("Bogen wählen")
		    .setMessage("Möchten Sie einen zufälligen Bogen, oder selbst einen auswählen?")
		    .setPositiveButton("auswählen", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            bogenwahl();
		        }
		     })
		    .setNegativeButton("zufällig", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) {     
		        	starteRandomPruefung();
		        }
		     })
		     .show();			
		}
		else if (arg0==btn11)
		{
			String versionName = "";
			PackageInfo pinfo;
			try {
				pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
				versionName = pinfo.versionName;
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			final String LicenseInfo = "Fußball Regelfragen V" + versionName + "\n\nEntwicklung:\nSimon Tenbeitel\n\nGrafik:\nJohannes Esseling\n\nInhaltliche Unterstützung:\nMaik Kerstan\nPatrick Terhürne\n\nEmail: simon.tenbeitel@gmail.com";
			AlertDialog.Builder LicenseDialog = new AlertDialog.Builder(MainActivity.this);
		    LicenseDialog.setTitle("Impressum");
		    LicenseDialog.setMessage(LicenseInfo);
		    LicenseDialog.show();
		}
		else if (arg0==btn12)
		{
			intent = new Intent(this, EinzelfragenActivity.class);
	    	startActivity(intent);
		}
		else if (arg0==btn14)
		{
			intent = new Intent(this, Einstellungen.class);
			startActivity(intent);
		}
		else if (arg0==btn15)
		{
			intent = new Intent(this, Statistik.class);
			startActivity(intent);
		}
	}
	
	private void bogenwahl()
	{
		in_bogenwahl = true;
		final ArrayList<Frageboegen> boegen = new ArrayList<Frageboegen>();
		if(anwaerter_active)
		{
			boegen.add(new Frageboegen(R.raw.anwaerter1, 11));
			boegen.add(new Frageboegen(R.raw.anwaerter2, 12));
		}
		if(normal_active)
		{
			boegen.add(new Frageboegen(R.raw.bogen1, 1));
			boegen.add(new Frageboegen(R.raw.bogen2, 2));
			boegen.add(new Frageboegen(R.raw.bogen3, 3));
			boegen.add(new Frageboegen(R.raw.bogen4, 4));
			boegen.add(new Frageboegen(R.raw.bogen5, 5));
			boegen.add(new Frageboegen(R.raw.bogen6, 6));
			boegen.add(new Frageboegen(R.raw.bogen7, 7));
			boegen.add(new Frageboegen(R.raw.bogen8, 8));
			boegen.add(new Frageboegen(R.raw.bogen9, 9));
			boegen.add(new Frageboegen(R.raw.bogen10, 10));
		}
		
		
		setContentView(R.layout.bogen_wahl);
        list = (ListView) findViewById(R.id.listView2);
        list.setAdapter(new FrageboegenListAdapter(this, boegen));	//ListView erstellen, und Fragen übergeben
		list.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// arg2 entspricht der nr, die angeklickt wurde
				startePruefung(boegen.get(arg2).getId(), boegen.get(arg2).getNr());
				setContentView(R.layout.activity_main_old);	//Menü wieder aufrufen, falls man von Prüfung zurück kehrt
			}
		});
	}
	private void starteRandomPruefung()
	{
		final int anzahl_anwaerter_boegen = 2;
		final int anzahl_normale_boegen = 10;
		
		
		/*
		 * Ein (finales) Integer Array machen, für die Resourcen mit den Bögen
		 * (Resourcen sind Integer Werte)
		 */
		
		int anzahl_boegen = 0;
		if (anwaerter_active) anzahl_boegen += anzahl_anwaerter_boegen;
		if (normal_active) anzahl_boegen += anzahl_normale_boegen;
		final int[] bogen_ids = new int[anzahl_boegen];
		int position = 0;
		if (normal_active)
		{
			bogen_ids[position + 0] = R.raw.bogen1;
			bogen_ids[position + 1] = R.raw.bogen2;
			bogen_ids[position + 2] = R.raw.bogen3;
			bogen_ids[position + 3] = R.raw.bogen4;
			bogen_ids[position + 4] = R.raw.bogen5;
			bogen_ids[position + 5] = R.raw.bogen6;
			bogen_ids[position + 6] = R.raw.bogen7;
			bogen_ids[position + 7] = R.raw.bogen8;
			bogen_ids[position + 8] = R.raw.bogen9;
			bogen_ids[position + 9] = R.raw.bogen10;
			position += anzahl_normale_boegen;
		}
		if (anwaerter_active)
		{
			bogen_ids[position + 0] = R.raw.anwaerter1;
			bogen_ids[position + 1] = R.raw.anwaerter2;
			
		}
		
		int nr = random.nextInt(bogen_ids.length);	//zufällig eine BogenNr wählen
		int id = bogen_ids[nr];			//Die Bogen-ID des zufälligen Bogens wählen
		
		startePruefung(id, (nr+1));
	}
	
	private void startePruefung(int id, int nr)
	{
		intent = new Intent(this, Pruefung.class);
		intent.putExtra("bogenid", id);
		intent.putExtra("bogennr", nr);
		startActivityForResult(intent, 0);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		initialisiere();
	}
	
	@Override
	public void onBackPressed() {
		if (in_bogenwahl)
		{
			setContentView(R.layout.activity_main_old);
			initialisiere();
		}
		else
		{
			finish();
		}
	}
}
