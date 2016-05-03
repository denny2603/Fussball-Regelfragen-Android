package de.simontenbeitel.regelfragen;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

public class Einzelfrage extends Activity implements OnClickListener, OnItemSelectedListener, OnCheckedChangeListener{
	final ArrayList<Frageboegen> boegen = new ArrayList<Frageboegen>();
	private int aktuellerBogen, aktuelleFrage;
	public static Frage[] fragen;
	private int[] originalposition;		//Die ursprünliche Position der Frage (also die Nummer im Bogen)
	
	//Für Spielsituationsfrage
	Spinner s4,s5,s6;
	String sf_antwort, odf_antwort, ps_antwort;		//die ausgewählten Antworten
	boolean odf_active;
	
	//Für Multiplechoicefrage
	RadioGroup rg2;
	RadioButton rb4,rb5,rb6;
	int multipleantwort;	//Nr der ausgewählten Antwort (0,1,2)
	
	Button aufloesen, neueFrage, ende;
	TextView fragetext, ueberschrift, fehlerpunkte;
	ScrollView sv;	
	ImageView img2, img3, img4;
	
	//Spielsituationsfrage (auflösung):
	TextView sf1, sf2, odf1, odf2, ps1, ps2;
	
	//Multiplechoice Frage (auflösung):
	TextView a1,a2,a3;
	
	//Für Schwierigkeitsgrad
	boolean limit_difficulties, anwaerter_active, normal_active;
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);				//Keine Titelleiste
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);	//Hochkant Format
		setContentView(R.layout.leer);
		TextView tv_ueberschrift = (TextView) findViewById(R.id.textView25);	//Überschrift auf leerer Seite
		tv_ueberschrift.setText("Einzelfragenmodus"); //Einfach "Prüfung" als Überschrift wählen
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		odf_active = sharedPrefs.getBoolean("pref_key_odf_active", true);

		initialisiere();
	}
	
	private void initialisiere()
	{
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
        
		//Die IDs der Bögen in eine Liste setzen (wird evtl später benötigt, wenn man mehr als einen Bogen abgearbeitet hat)
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
		
		aktuellerBogen = 0;
		aktuelleFrage = -1;
		Collections.shuffle(boegen);	//Die Bögen durchmischen
		createBogen(boegen.get(aktuellerBogen).getId());
		
		print();
	}
	
	private void print()
	{
		inkrementiereFrage();
		
		switch(fragen[aktuelleFrage].getTyp()){
		case 1:	setContentView(R.layout.spielsituation_einzelfrage);        
        		s4 = (Spinner) findViewById(R.id.spinner4);			//Spielfortsetzung
        		s5 = (Spinner) findViewById(R.id.spinner5);			//Ort der Fortsetzung
        		s6 = (Spinner) findViewById(R.id.spinner6);			//Persönliche Strafe
        		aufloesen = (Button) findViewById(R.id.button13);	//Auflösung anzeigen
        		aufloesen.setOnClickListener(this);
        		fragetext = (TextView) findViewById(R.id.textView31);
        		sv = (ScrollView) findViewById(R.id.scrollView6);
                
        		//Die Spinner befüllen
                ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.sfkat, android.R.layout.simple_spinner_item);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                s4.setAdapter(adapter1);
                s4.setOnItemSelectedListener(this);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.odfkat, android.R.layout.simple_spinner_item);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                s5.setAdapter(adapter2);
                s5.setOnItemSelectedListener(this);
                ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.pskat, android.R.layout.simple_spinner_item);
                adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                s6.setAdapter(adapter3);
                s6.setOnItemSelectedListener(this);
        		
                if(odf_active==false)
        		{
        			s5.setVisibility(View.INVISIBLE);
        			TextView ueberschrift_odf = (TextView) findViewById(R.id.textView33);
        			ueberschrift_odf.setVisibility(View.INVISIBLE);
        		}
        		break;
		case 2:	setContentView(R.layout.multiplechoice_einzelfrage);
				aufloesen = (Button) findViewById(R.id.button4);
				aufloesen.setOnClickListener(this);
				fragetext = (TextView) findViewById(R.id.textView29);
				rg2 = (RadioGroup) findViewById(R.id.radioGroup2);
				rg2.setOnCheckedChangeListener(this);	//Wenn ein Item ausgewählt wird, sofort notieren!!!
				sv = (ScrollView) findViewById(R.id.scrollView5);
				
				/*
				 * Die RadioButtons initialisieren
				 * keiner ist vorausgewählt
				 * die 3 Antwortmöglichkeiten werden als Text dargestellt
				 */
				rb4 = (RadioButton) findViewById(R.id.radio4);
				rb5 = (RadioButton) findViewById(R.id.radio5);
				rb6 = (RadioButton) findViewById(R.id.radio6);
				rb4.setChecked(true);
				rb4.setText(((MultiplechoiceFrage)fragen[aktuelleFrage]).antwortmoeglichkeiten[0]);
				rb5.setText(((MultiplechoiceFrage)fragen[aktuelleFrage]).antwortmoeglichkeiten[1]);
				rb6.setText(((MultiplechoiceFrage)fragen[aktuelleFrage]).antwortmoeglichkeiten[2]);
				
				
				break;
		default: break;			
		}
		
		fragetext.setText(fragen[aktuelleFrage].fragentext);			//Fragetext wird angezeigt
        sv.fullScroll(sv.FOCUS_UP);		//Wieder nach oben Scrollen, damit Fragetext sofort lesbar ist, ohne manuelles scrollen des Benutzers
	}
	
	private void inkrementiereFrage()
	{
		aktuelleFrage++;
		if(aktuelleFrage==fragen.length)
		{
			//Das Ende des aktuellen Bogens wurde erreicht, daher zum nächsten Bogen
			aktuellerBogen++;
			if(aktuellerBogen!=boegen.size())
			{
				createBogen(boegen.get(aktuellerBogen).getId());
				aktuelleFrage = 0;
			}
			else
			{
				new AlertDialog.Builder(this)
			    .setTitle("Ende")
			    .setMessage("Sie haben alle Fragen bearbeitet")
			    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
			            finish();
			        }
			     })
			     .show();
			}
		}
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
		
		fragenMischen();
	}
	
	private void fragenMischen()
	{
		Frage tmp;
        int rand;
        Random r = new Random();
        originalposition = new int[fragen.length];
        int tempposition;
        for (int i = 0; i < originalposition.length; i++)
        {
        	originalposition[i] = i;
        }
        for (int i = 0; i < fragen.length; i++) {
            rand = r.nextInt(fragen.length);
            
            tmp = fragen[i];
            tempposition = originalposition[i];
            
            fragen[i] = fragen[rand];
            originalposition[i] = originalposition[rand];
            
            fragen[rand] = tmp;
            originalposition[rand] = tempposition;
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
	

	@Override
	public void onClick(View arg0) {
		if(arg0==aufloesen)
		{
			//In der switch Abfrage wird ermittelt, welcher Fragentyp vorliegt
			//Typ 1: Spielsituationsfrage
			//Typ 2: Multiplechoice Frage
			switch(fragen[aktuelleFrage].getTyp()){
			case 1:	if(odf_active) ((Spielsituationsfrage)fragen[aktuelleFrage]).beantworte(sf_antwort, odf_antwort, ps_antwort);
					else ((Spielsituationsfrage)fragen[aktuelleFrage]).beantworte(sf_antwort, ps_antwort);
					break;
			case 2: ((MultiplechoiceFrage)fragen[aktuelleFrage]).beantworte(multipleantwort);
					break;
			default: break;		
			}
			
			zeigeLoesung();
		}
		else if (arg0==neueFrage)
		{
			print();
		}
		else if (arg0==ende)
		{
			finish();
		}
		
	}
	
	private void zeigeLoesung()
	{
		switch(fragen[aktuelleFrage].getTyp())
		{
			//In der switch Abfrage wird ermittelt, welcher Fragentyp vorliegt
			//Typ 1: Spielsituationsfrage
			//Typ 2: Multiplechoice Frage
			case 1:	setContentView(R.layout.beantwortet_spielsituation);
					ueberschrift = (TextView) findViewById(R.id.textView7);
					fragetext = (TextView) findViewById(R.id.textView6);
					ende = (Button) findViewById(R.id.button3);
					ende.setOnClickListener(this);
					neueFrage = (Button) findViewById(R.id.button2);
					neueFrage.setOnClickListener(this);
					fehlerpunkte = (TextView) findViewById(R.id.textView15);	//Ausgabe der Anzahl der Fehlerpunke für diese Frage
					sv = (ScrollView) findViewById(R.id.scrollView2);
					
					sf1 = (TextView) findViewById(R.id.textView8);	//gewählte Spielfortsetzung
					sf2 = (TextView) findViewById(R.id.textView9);	//korrekte Spielfortsetzung
					odf1 = (TextView) findViewById(R.id.textView10);//gewählter Ort der Fortsetzung
					odf2 = (TextView) findViewById(R.id.textView11);//korrekter Ort der Fortsetzung
					ps1 = (TextView) findViewById(R.id.textView12);	//gewählte persönliche Strafe
					ps2 = (TextView) findViewById(R.id.textView13);	//korrekte persönliche Strafe
					img2 = (ImageView) findViewById(R.id.img2);		// Richtig/Falsch Grafik Spielfortsetzung
					img3 = (ImageView) findViewById(R.id.img3);		// Richtig/Falsch Grafik Ort der Fortsetzung
					img4 = (ImageView) findViewById(R.id.img4);		// Richtig/Falsch Grafik persönliche Strafe
					
					sf1.setText(((Spielsituationsfrage)fragen[aktuelleFrage]).sf_ausgewaehlt);
					if(odf_active) odf1.setText(((Spielsituationsfrage)fragen[aktuelleFrage]).odf_ausgewaehlt);
					ps1.setText(((Spielsituationsfrage)fragen[aktuelleFrage]).ps_ausgewaehlt);
					if(odf_active) ((Spielsituationsfrage)fragen[aktuelleFrage]).aufloesen(sf2, odf2, ps2, img2, img3, img4, fehlerpunkte);
					else ((Spielsituationsfrage)fragen[aktuelleFrage]).aufloesen(sf2, odf2, ps2, img2, img4, fehlerpunkte);
					break;
			case 2:	setContentView(R.layout.beantwortet_multiple);
					ueberschrift = (TextView) findViewById(R.id.textView19);
					fragetext = (TextView) findViewById(R.id.textView20);
					ende = (Button) findViewById(R.id.button10);
					ende.setOnClickListener(this);
					neueFrage = (Button) findViewById(R.id.button9);
					neueFrage.setOnClickListener(this);
					fehlerpunkte = (TextView) findViewById(R.id.textView24);	//Ausgabe der Anzahl der Fehlerpunke für diese Frage
					sv = (ScrollView) findViewById(R.id.scrollView3);
					
					img2 = (ImageView) findViewById(R.id.img5);		// Richtig/Falsch Grafik 1. Antwortmöglichkeit
					img3 = (ImageView) findViewById(R.id.img6);		// Richtig/Falsch Grafik 2. Antwortmöglichkeit
					img4 = (ImageView) findViewById(R.id.img7);		// Richtig/Falsch Grafik 3. Antwortmöglichkeit
					a1 = (TextView) findViewById(R.id.textView21);	// Antworttext der 1. Antwortmöglichkeit
					a2 = (TextView) findViewById(R.id.textView22);	// Antworttext der 2. Antwortmöglichkeit
					a3 = (TextView) findViewById(R.id.textView23);	// Antworttext der 3. Antwortmöglichkeit
					
					a1.setText(((MultiplechoiceFrage)fragen[aktuelleFrage]).antwortmoeglichkeiten[0]);
					a2.setText(((MultiplechoiceFrage)fragen[aktuelleFrage]).antwortmoeglichkeiten[1]);
					a3.setText(((MultiplechoiceFrage)fragen[aktuelleFrage]).antwortmoeglichkeiten[2]);
					((MultiplechoiceFrage)fragen[aktuelleFrage]).aufloesen(img2, img3, img4, fehlerpunkte);
					break;
			default: break;
		}
		
		ueberschrift.setText("Einzelfragenmodus");
		fragetext.setText(fragen[aktuelleFrage].fragentext);
		neueFrage.setText("Neue Frage");
		ende.setText("Ende");
		sv.fullScroll(sv.FOCUS_UP);		//Wieder nach oben Scrollen, damit Fragetext sofort lesbar ist, ohne manuelles scrollen des Benutzers
		
		insertIntoDB();
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		//Ein Spinner wurde betätigt		
		//arg0 entspricht dem Spinner, der das onItemSelected ausgelöst hat
						if(arg0==s4)
						{
							sf_antwort = (String)s4.getSelectedItem();	//ausgewählte Antwort aktualisieren
						}
						else if(arg0==s5)
						{
							odf_antwort = (String)s5.getSelectedItem();
						}
						else if(arg0==s6)
						{
							ps_antwort = (String)s6.getSelectedItem();
						}
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int checkedID) {
		/*
		 * Wenn in der RadioGroup ein Item ausgewählt wurde, muss rausgefunden werden,
		 * welcher Button angeklickt wurde
		 * 
		 * Danach die Antwort speichern
		 */
		if(checkedID==R.id.radio4) multipleantwort = 0;
		else if (checkedID==R.id.radio5) multipleantwort = 1;
		else if (checkedID==R.id.radio6) multipleantwort = 2;
		
	}
	
	//Falls die Zurück-Taste gedrückt wird, soll der Benutzer erst gefragt werden, ob er zurück ins Hauptmenü möchte
	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
	    .setTitle("Beenden")
	    .setMessage("Zurück ins Hauptmenü?")
	    .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	finish();
	        }
	     })
	    .setNegativeButton("Nein", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // Wenn man doch nich zurück möchte, passiert nichts
	        }
	     })
	     .show();
	}
	
	private void insertIntoDB()
	{
		DatabaseHandler db = new DatabaseHandler(this);
		
		Date dateNow = new Date ();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    StringBuilder datumBuilder = new StringBuilder( sdf.format(dateNow));
	    String datum = new String(datumBuilder);

	    final String[] sf_kat = getResources().getStringArray(R.array.sfkat);
	    final String[] odf_kat = getResources().getStringArray(R.array.odfkat);
	    final String[] ps_kat = getResources().getStringArray(R.array.pskat);

	    int a1 = 0;
	    int a2 = 0;
	    int a3 = 0;
	    int typ = 0;
	    
	    double fehlerpkt = fragen[aktuelleFrage].getFehlerpunkte();
	    
	    int bogenNR = boegen.get(aktuellerBogen).getNr();
	    
	    boolean richtig = fragen[aktuelleFrage].getRichtig();
	    boolean a1_richtig = true, a3_richtig = true;
	    char a2_richtig = 't';
	    
	    if (fragen[aktuelleFrage].getTyp() == 1)
	    {
	    	//Spielsituationsfrage
	    	typ = 1;
	    	a1_richtig = ((Spielsituationsfrage)fragen[aktuelleFrage]).get_sf_richtig();
	    	a3_richtig = ((Spielsituationsfrage)fragen[aktuelleFrage]).get_ps_richtig();
	    	a2_richtig = 'p';
	    	
	    	//Die ausgewählten Antworten ermitteln
	    	for (int x = 0; x<sf_kat.length; x++)
	    	{
	    		String sf_ausgewaehlt = ((Spielsituationsfrage)fragen[aktuelleFrage]).sf_ausgewaehlt;
	    		if (sf_kat[x].equals(sf_ausgewaehlt))
	    		{
	    			a1 = x;
	    		}
	    	}
	    	if(odf_active)
	    	{
	    		if (((Spielsituationsfrage)fragen[aktuelleFrage]).get_odf_richtig()) a2_richtig = 't';
	    		else a2_richtig = 'f';
	    		for (int x = 0; x<odf_kat.length; x++)
		    	{
		    		String odf_ausgewaehlt = ((Spielsituationsfrage)fragen[aktuelleFrage]).odf_ausgewaehlt;
		    		if (odf_kat[x].equals(odf_ausgewaehlt))
		    		{
		    			a2 = x;
		    		}
		    	}
	    	}
	    	
	    	for (int x = 0; x<ps_kat.length; x++)
	    	{
	    		String ps_ausgewaehlt = ((Spielsituationsfrage)fragen[aktuelleFrage]).ps_ausgewaehlt;
	    		if (ps_kat[x].equals(ps_ausgewaehlt))
	    		{
	    			a3 = x;
	    		}
	    	}
	    	
	    }
	    else if (fragen[aktuelleFrage].getTyp() == 2)
	    {
	    	//Multiplechoicefrage
	    	typ = 2;
	    	
	    	a1 = a2 = a3 = ((MultiplechoiceFrage)fragen[aktuelleFrage]).ausgewaehlt;
	    	a1_richtig = true;
	    	a3_richtig = true;
	    	a2_richtig = 't';
	    }
	    
	    
	    
	    DBFrage db_frage;
	    
	    if (odf_active)
	    {
	    	db_frage = new DBFrage(0, bogenNR, originalposition[aktuelleFrage], a1, a2, a3, fehlerpkt, datum, typ, richtig, a1_richtig, a2_richtig, a3_richtig);
	    }
	    else
	    {
	    	db_frage = new DBFrage(0, bogenNR, originalposition[aktuelleFrage], a1, 99, a3, fehlerpkt, datum, typ, richtig, a1_richtig, a2_richtig, a3_richtig);
	    }
	    db.addFrage(db_frage);
	}
}
