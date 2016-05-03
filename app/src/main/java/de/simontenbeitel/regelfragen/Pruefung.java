package de.simontenbeitel.regelfragen;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

public class Pruefung extends Activity implements OnClickListener, OnItemSelectedListener, OnCheckedChangeListener{
	public static Frage[] fragen;
	int position;	//Bei welcher Frage(nr) man im aktuellen Bogen ist
	Button btn5, btn6;	//Vor und Zurück Buttons
	ScrollView scrollView1;
	ProgressBar progbar1;	//Fortschrittsbalken
	TextView fragetext, tv14;	//tv14 ist für Darstellung des Fortschritts ("Frage x von y")
	Intent intent;
	int id;			//Bogen-ID Speichern
	public static int bogenNR;		//BogenNR (für DB Eintrag später)
	
	//Für Spielsituationsfrage:
	Spinner s1, s2, s3;		//s1: Spielfortsetzung s2: Ort der Fortstzung s3: Persönliche Strafe
	String sf_antwort, odf_antwort, ps_antwort;		//die ausgewählten Antworten
	boolean odf_active;

	
	//Für Multiplechoicefrage:
	RadioGroup rg1;		//Die Gruppe, die die 3 Antwortmöglichkeiten darstellt
	RadioButton rb1,rb2,rb3;	//Die 3 Radiobuttons für die Antwortmöglichkeiten
	int multipleantwort;	//Nr der ausgewählten Antwort (0,1,2)
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);				//Keine Titelleiste
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);	//Hochkant Format
		setContentView(R.layout.leer);
		TextView tv_ueberschrift = (TextView) findViewById(R.id.textView25);	//Überschrift auf leerer Seite
		tv_ueberschrift.setText("Prüfung"); //Einfach "Prüfung" als Überschrift wählen
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		odf_active = sharedPrefs.getBoolean("pref_key_odf_active", true);

		id = getIntent().getIntExtra("bogenid", 0);
		bogenNR = getIntent().getIntExtra("bogennr", 0);
		createBogen(id);
		start();
	}
	
	@Override
	public void onClick(View v) {
		/*
		 * Prüfe zunächst, ob auf vorherige, oder nächste Frage geklickt wurde
		 * Dann führe die Beantworte-Methode aus, damit die Antwort gespeichert wurde
		 */
		if(v==btn5 || v==btn6)
		{
			switch(fragen[position].getTyp()){
			case 1:	if(odf_active) ((Spielsituationsfrage)fragen[position]).beantworte(sf_antwort, odf_antwort, ps_antwort);
					else ((Spielsituationsfrage)fragen[position]).beantworte(sf_antwort, ps_antwort);
					break;
			case 2: ((MultiplechoiceFrage)fragen[position]).beantworte(multipleantwort);
					break;
			default: break;		
			}
		}
		
		/*
		 * Wenn der zurück Button gedrückt wurde, dekrementiere die Position
		 * und zeige die vorherige Frage an
		 */
		if(v==btn5)
		{
			position--;
			print();
		}
		/*
		 * Wenn der vor-Button gedrückt wurde, prüfe zunächst, an welcher Position man ist
		 */
		else if(v==btn6)
		{
			/*
			 * Wenn man noch nicht bei der letzten Frage ist, inkrementiere die Position
			 * und zeige die nächste Frage an
			 */
			if(position<(fragen.length-1))
			{
				position++;
				print();
			}
			/*
			 * Wenn man bei der letzten Frage ist, soll die Auswertung aufgerufen werden
			 */
			else
			{

				intent = new Intent(this, Pruefung_Auswertung.class);
				startActivityForResult(intent, 0);	//Rufe die Auswertung auf
			}
		}
		
		
	}
	
	private void start()
	{
		position = 0;		
		print();
	}
	
	private void createBogen(int id){
		JSONObject json_data;	//json objekt
		try{
			JSONArray jArray = new JSONArray(readBogen(id));	//den String aus der .txt auslesen
			fragen = new Frage[jArray.length()];	//Die Frage-Objekte als Array erstellen (ist gleich der Länge des json-Array)
			for(int i=0;i<jArray.length();i++)
			{
				json_data = jArray.getJSONObject(i);
				int typ = json_data.getInt("typ");
				String frage = (String) json_data.get("frage");
				String a1 = (String) json_data.get("a1");
				String a2 = (String) json_data.get("a2");
				String a3 = (String) json_data.get("a3");
				int loesung = json_data.getInt("loesung");
				
				switch(typ){
				case 1:	fragen[i] = new Spielsituationsfrage(frage, a1, a2, a3);
						break;
				case 2: fragen[i] = new MultiplechoiceFrage(frage, a1,a2,a3,loesung);
						break;
				default: break;
				}

			}
		}catch(Exception e)
		{
			Log.e("log-tag","Error2"+e.toString());
		}
	}
	
	private String readBogen(int id){
		/*
		 * Die txt öfnnen
		 * die Resourcen-ID wird übergeben
		 */
		String mResponse="";
		try {
	        InputStream is = getResources().openRawResource(id);
	        int size = is.available();
	        byte[] buffer = new byte[size];
	        is.read(buffer);
	        is.close();
	        mResponse = new String(buffer, "UTF-8");	//Jede .txt muss als utf-8 gespeichert werden
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
		return mResponse;
	}
	
	private void print(){
		switch(fragen[position].getTyp()){
			case 1:	setContentView(R.layout.pruefung);        
	        		s1 = (Spinner) findViewById(R.id.spinner1);			//Spielfortsetzung
	        		s2 = (Spinner) findViewById(R.id.spinner2);			//Ort der Fortsetzung
	        		s3 = (Spinner) findViewById(R.id.spinner3);			//Persönliche Strafe
	        		btn5 = (Button) findViewById(R.id.btn5);			//Vorherige Frage
	        		btn5.setOnClickListener(this);
	        		btn6 = (Button) findViewById(R.id.btn6);			//Nächste Frage
	        		btn6.setOnClickListener(this);
	        		fragetext = (TextView) findViewById(R.id.textView5);
	        		tv14 = (TextView) findViewById(R.id.textView14);	//Anzeige der Fragenummer
	        		scrollView1 = (ScrollView) findViewById(R.id.scrollView1);
	        		progbar1 = (ProgressBar) findViewById(R.id.progressBar1);
	                
	        		//Die Spinner befüllen
	                ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.sfkat, android.R.layout.simple_spinner_item);
	                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	                s1.setAdapter(adapter1);
	                s1.setOnItemSelectedListener(this);
	                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	                ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.odfkat, android.R.layout.simple_spinner_item);
	                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	                s2.setAdapter(adapter2);
	                s2.setOnItemSelectedListener(this);
	                ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.pskat, android.R.layout.simple_spinner_item);
	                adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	                s3.setAdapter(adapter3);
	                s3.setOnItemSelectedListener(this);
	                
	                //Die bereits ausgewählten Antworten laden (bei zurück gehen, oder erneutem vorwärts gehen)
	                String sf_ausgewaehlt = ((Spielsituationsfrage)fragen[position]).sf_ausgewaehlt;
	        		String odf_ausgewaehlt = ((Spielsituationsfrage)fragen[position]).odf_ausgewaehlt;
	        		String ps_ausgewaehlt = ((Spielsituationsfrage)fragen[position]).ps_ausgewaehlt;		
	        		ArrayAdapter sf_adapter = (ArrayAdapter) s1.getAdapter();
	        		int sf_position = sf_adapter.getPosition(sf_ausgewaehlt);	//Position der ausgewählten Antwort im Spinner ermitteln
	        		s1.setSelection(sf_position);		//Beim Spinner die ausgewählte Antwort markieren
	        		ArrayAdapter odf_adapter = (ArrayAdapter) s2.getAdapter();
	        		int odf_position = odf_adapter.getPosition(odf_ausgewaehlt);
	        		s2.setSelection(odf_position);
	        		ArrayAdapter ps_adapter = (ArrayAdapter) s3.getAdapter();
	        		int ps_position = ps_adapter.getPosition(ps_ausgewaehlt);
	        		s3.setSelection(ps_position);
	                
	        		if(odf_active==false)
	        		{
	        			s2.setVisibility(View.INVISIBLE);
	        			TextView ueberschrift_odf = (TextView) findViewById(R.id.textView32);
	        			ueberschrift_odf.setVisibility(View.INVISIBLE);
	        		}
	        		break;
			case 2:	setContentView(R.layout.multiplechoice);
					btn5 = (Button) findViewById(R.id.button7);			//Vorherige Frage
					btn5.setOnClickListener(this);
					btn6 = (Button) findViewById(R.id.button8);			//Nächste Frage
					btn6.setOnClickListener(this);
					fragetext = (TextView) findViewById(R.id.textView17);
					rg1 = (RadioGroup) findViewById(R.id.radioGroup1);
					rg1.setOnCheckedChangeListener(this);	//Wenn ein Item ausgewählt wird, sofort notieren!!!
					
					/*
					 * Die RadioButtons initialisieren
					 * keiner ist vorausgewählt
					 * die 3 Antwortmöglichkeiten werden als Text dargestellt
					 */
					rb1 = (RadioButton) findViewById(R.id.radio1);
					rb1.setChecked(false);
					rb2 = (RadioButton) findViewById(R.id.radio2);
					rb2.setChecked(false);
					rb3 = (RadioButton) findViewById(R.id.radio3);
					rb3.setChecked(false);
					rb1.setText(((MultiplechoiceFrage)fragen[position]).antwortmoeglichkeiten[0]);
					rb2.setText(((MultiplechoiceFrage)fragen[position]).antwortmoeglichkeiten[1]);
					rb3.setText(((MultiplechoiceFrage)fragen[position]).antwortmoeglichkeiten[2]);
					
					
					tv14 = (TextView) findViewById(R.id.textView18);	//Anzeige der Fragenummer
					scrollView1 = (ScrollView) findViewById(R.id.scrollView4);
					progbar1 = (ProgressBar) findViewById(R.id.progressBar2);	//Fortschrittsbalken
			        
					/*
					 * Die ausgewählte Antwort auswählen
					 * (beim ersten Aufruf ist erste Antwort ausgewählt)
					 */
					int ausgewaehlt = ((MultiplechoiceFrage)fragen[position]).ausgewaehlt;
					switch (ausgewaehlt){
						case 0: rb1.setChecked(true);
								break;
						case 1: rb2.setChecked(true);
								break;
						case 2: rb3.setChecked(true);
								break;
						default: break;
					}
					break;
			default: break;			
		}
		if(position==0)
		{
			btn5.setClickable(false);	//Wenn man bei der ersten Frage ist, kann man natürlich nicht zurück
		}
		else
		{
			btn5.setClickable(true);	//Wenn man nicht bei der ersten Frage ist, kann man zurück
		}
		if(position==(fragen.length-1))
		{
			btn6.setText("Abgabe");		//Wenn man bei der letzten Frage ist, gibt man den Bogen ab
		}
		else
		{
			btn6.setText("Nächste");	//Wenn man nicht bei der letzten Frage ist, kommt man per Knopfdruck zur nächsten Frage
		}
		fragetext.setText(fragen[position].fragentext);			//Fragetext wird angezeigt
		progbar1.setMax(fragen.length);							//Der Maximalwert des Fortschrittsbalkens soll der Länge des Fragenobjekts-Arrays entsprechen
		progbar1.setProgress(position+1);						//Visueller Fortschritt anzeigen
        tv14.setText("Frage " + (position+1) + " von " + fragen.length);	//Texttueller Fortschritt anzeigen
        scrollView1.fullScroll(scrollView1.FOCUS_UP);		//Wieder nach oben Scrollen, damit Fragetext sofort lesbar ist, ohne manuelles scrollen des Benutzers
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		//Ein Spinner wurde betätigt		
		//arg0 entspricht dem Spinner, der das onItemSelected ausgelöst hat
				if(arg0==s1)
				{
					sf_antwort = (String)s1.getSelectedItem();	//ausgewählte Antwort aktualisieren
				}
				else if(arg0==s2)
				{
					odf_antwort = (String)s2.getSelectedItem();
				}
				else if(arg0==s3)
				{
					ps_antwort = (String)s3.getSelectedItem();
				}
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// Wenn kein Spinner ausgewählt wurde (ist unmöglich!!!), soll natürlich nichts passieren
		// diese Methode muss aber in Klassen überschrieben werden, die onItemSelectedListener implementieren
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int checkedID) {
		/*
		 * Wenn in der RadioGroup ein Item ausgewählt wurde, muss rausgefunden werden,
		 * welcher Button angeklickt wurde
		 * 
		 * Danach die Antwort speichern
		 */
		if(checkedID==R.id.radio1) multipleantwort = 0;
		else if (checkedID==R.id.radio2) multipleantwort = 1;
		else if (checkedID==R.id.radio3) multipleantwort = 2;
	}

	//Falls die Zurück-Taste gedrückt wird, soll der Benutzer erst gefragt werden, ob er die Prüfung wirklich abbrechen möchte
	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
	    .setTitle("Beenden")
	    .setMessage("Sind Sie sicher, die Prüfung beenden zu wollen?")
	    .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	//Wenn man wirklich beenden will, wird die Klasse zerstört, daher landet man wieder in der Hauptklasse
	            finish();
	        }
	     })
	    .setNegativeButton("Nein", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // Wenn man doch nich abbrechen möchte, passiert nichts
	        }
	     })
	     .show();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		finish();
	}

}