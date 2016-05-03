package de.simontenbeitel.regelfragen;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

public class Pruefung_Auswertung extends Activity implements OnClickListener, OnItemClickListener{
	private ArrayList<Frage> fragen;
	ListView list;
	Button vor, zurueck;
	int position;
	TextView ueberschrift, fragetext, fehlerpunkte;
	ImageView img2, img3, img4;
	ScrollView sv1;
	Boolean aufloesung_einzelfrage;
	Boolean statistics_active;
	
	//Spielsituationsfrage:
	TextView sf1, sf2, odf1, odf2, ps1, ps2;
	boolean odf_active;
	
	//Multiplechoice Frage:
	TextView a1,a2,a3;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);				//Keine Titelleiste
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);	//Hochkant Format
		setContentView(R.layout.auswertung);
		aufloesung_einzelfrage = false;
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		odf_active = sharedPrefs.getBoolean("pref_key_odf_active", true);
		statistics_active = sharedPrefs.getBoolean("pref_key_statistics_active", true);
		
		fragen = new ArrayList<Frage>(Arrays.asList(Pruefung.fragen));	//Die Frage-Objekte aus der Prüfung laden
		
		if(statistics_active)
		{
			insertIntoDB();
		}        
		
		list = (ListView) findViewById(R.id.listView1);		//ListView aus Resourcen laden
		list.setAdapter(new MyListAdapter(this, fragen));	//ListView erstellen, und Fragen übergeben
		list.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if(arg2<fragen.size())		//Wenn eine Frage angeklickt wurde, die Frage laden, und die Auswertung der einzelnen Frage anzeigen
		{
			aufloesung_einzelfrage = true;
			position = arg2;
			print();
		}
		else if (arg2 >= fragen.size())
	    {
	        //Wenn die Fehlerpunkte angeklickt wurden, soll nichts passieren
			//TODO: evtl Bestanden/nicht
	    }
		
	}

	@Override
	public void onClick(View arg0) {
		if(arg0==vor)
		{
			position++;
			print();
		}
		else if(arg0==zurueck)
		{
			position--;
			print();
		}
		
	}
	
	private void print()
	{
		switch(fragen.get(position).getTyp())
		{
			//In der switch Abfrage wird ermittelt, welcher Fragentyp vorliegt
			//Typ 1: Spielsituationsfrage
			//Typ 2: Multiplechoice Frage
			case 1:	setContentView(R.layout.beantwortet_spielsituation);
					ueberschrift = (TextView) findViewById(R.id.textView7);
					fragetext = (TextView) findViewById(R.id.textView6);
					vor = (Button) findViewById(R.id.button3);
					vor.setOnClickListener(this);
					zurueck = (Button) findViewById(R.id.button2);
					zurueck.setOnClickListener(this);
					fehlerpunkte = (TextView) findViewById(R.id.textView15);	//Ausgabe der Anzahl der Fehlerpunke für diese Frage
					sv1 = (ScrollView) findViewById(R.id.scrollView2);
					
					sf1 = (TextView) findViewById(R.id.textView8);	//gewählte Spielfortsetzung
					sf2 = (TextView) findViewById(R.id.textView9);	//korrekte Spielfortsetzung
					odf1 = (TextView) findViewById(R.id.textView10);//gewählter Ort der Fortsetzung
					odf2 = (TextView) findViewById(R.id.textView11);//korrekter Ort der Fortsetzung
					ps1 = (TextView) findViewById(R.id.textView12);	//gewählte persönliche Strafe
					ps2 = (TextView) findViewById(R.id.textView13);	//korrekte persönliche Strafe
					img2 = (ImageView) findViewById(R.id.img2);		// Richtig/Falsch Grafik Spielfortsetzung
					img3 = (ImageView) findViewById(R.id.img3);		// Richtig/Falsch Grafik Ort der Fortsetzung
					img4 = (ImageView) findViewById(R.id.img4);		// Richtig/Falsch Grafik persönliche Strafe
					
					sf1.setText(((Spielsituationsfrage)fragen.get(position)).sf_ausgewaehlt);
					if(odf_active) odf1.setText(((Spielsituationsfrage)fragen.get(position)).odf_ausgewaehlt);
					ps1.setText(((Spielsituationsfrage)fragen.get(position)).ps_ausgewaehlt);
					if(odf_active) ((Spielsituationsfrage)fragen.get(position)).aufloesen(sf2, odf2, ps2, img2, img3, img4, fehlerpunkte);
					else ((Spielsituationsfrage)fragen.get(position)).aufloesen(sf2, odf2, ps2, img2, img4, fehlerpunkte);
					break;
			case 2:	setContentView(R.layout.beantwortet_multiple);
					ueberschrift = (TextView) findViewById(R.id.textView19);
					fragetext = (TextView) findViewById(R.id.textView20);
					vor = (Button) findViewById(R.id.button10);
					vor.setOnClickListener(this);
					zurueck = (Button) findViewById(R.id.button9);
					zurueck.setOnClickListener(this);
					fehlerpunkte = (TextView) findViewById(R.id.textView24);	//Ausgabe der Anzahl der Fehlerpunke für diese Frage
					sv1 = (ScrollView) findViewById(R.id.scrollView3);
					
					img2 = (ImageView) findViewById(R.id.img5);		// Richtig/Falsch Grafik 1. Antwortmöglichkeit
					img3 = (ImageView) findViewById(R.id.img6);		// Richtig/Falsch Grafik 2. Antwortmöglichkeit
					img4 = (ImageView) findViewById(R.id.img7);		// Richtig/Falsch Grafik 3. Antwortmöglichkeit
					a1 = (TextView) findViewById(R.id.textView21);	// Antworttext der 1. Antwortmöglichkeit
					a2 = (TextView) findViewById(R.id.textView22);	// Antworttext der 2. Antwortmöglichkeit
					a3 = (TextView) findViewById(R.id.textView23);	// Antworttext der 3. Antwortmöglichkeit
					
					a1.setText(((MultiplechoiceFrage)fragen.get(position)).antwortmoeglichkeiten[0]);
					a2.setText(((MultiplechoiceFrage)fragen.get(position)).antwortmoeglichkeiten[1]);
					a3.setText(((MultiplechoiceFrage)fragen.get(position)).antwortmoeglichkeiten[2]);
					((MultiplechoiceFrage)fragen.get(position)).aufloesen(img2, img3, img4, fehlerpunkte);
					break;
			default: break;
		}
		
		ueberschrift.setText("Frage " + (position+1));
		fragetext.setText(fragen.get(position).fragentext);
		sv1.fullScroll(sv1.FOCUS_UP);		//Wieder nach oben Scrollen, damit Fragetext sofort lesbar ist, ohne manuelles scrollen des Benutzers
		
		if(position==0)
		{
			zurueck.setClickable(false);
		}
		else
		{
			zurueck.setClickable(true);
		}
		if(position==(fragen.size()-1))
		{
			vor.setClickable(false);
		}
		else
		{
			vor.setClickable(true);
		}
	}
	
	//Falls die Zurück-Taste gedrückt wird, soll der Benutzer erst gefragt werden, ob er zurück ins Hauptmenü möchte
		@Override
		public void onBackPressed() {
			if(aufloesung_einzelfrage)
			{
				setContentView(R.layout.auswertung);
				list = (ListView) findViewById(R.id.listView1);		//ListView aus Resourcen laden
				list.setAdapter(new MyListAdapter(this, fragen));	//ListView erstellen, und Fragen übergeben
				list.setOnItemClickListener(this);
				aufloesung_einzelfrage = false;
			}
			
			else
			{
				new AlertDialog.Builder(this)
				.setTitle("Beenden")
				.setMessage("Zurück ins Hauptmenü?")
				.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) { 
						//Wenn man wirklich zurück will, wird die Klasse zerstört, daher landet man wieder in der Hauptklasse
						//Pruefung.beenden();
						setResult(RESULT_OK);
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
		}
		
		private void insertIntoDB()
		{
			DatabaseHandler db = new DatabaseHandler(this);
			
			Date dateNow = new Date ();
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    StringBuilder datumBuilder = new StringBuilder( sdf.format(dateNow));
		    String datum = new String(datumBuilder);
		    
		    int bogenNR = Pruefung.bogenNR;		//BogenNR in Resourcen
		    
		    double fehlerpkt = 0;
			for (int x = 0; x<fragen.size(); x++)
			{
				fehlerpkt += fragen.get(x).getFehlerpunkte();	//Fehlerpunkte von allen Fragen addieren
			}
		    
		    DBBogen dbbogen = new DBBogen(bogenNR, fehlerpkt, datum);
		    
		    db.addBogen(dbbogen);
		    
		    
		    //Starte mit befüllen der Fragentabelle
		    int bogenID = db.getBogenID(datum);		//ID des Bogens in der Tabelle holen
		    
		    sdf = new SimpleDateFormat("yyyy-MM-dd");
		    datumBuilder = new StringBuilder( sdf.format(dateNow));
		    datum = new String(datumBuilder);
		    
		    final String[] sf_kat = getResources().getStringArray(R.array.sfkat);
		    final String[] odf_kat = getResources().getStringArray(R.array.odfkat);
		    final String[] ps_kat = getResources().getStringArray(R.array.pskat);

		    int a1 = 0;
		    int a2 = 0;
		    int a3 = 0;
		    int typ = 0;
		    
		    boolean richtig = true;;
		    boolean a1_richtig = true, a3_richtig = true;
		    char a2_richtig = 't';
		    
		    for (int i = 0; i<fragen.size(); i++)
		    {
		    	fehlerpkt = fragen.get(i).getFehlerpunkte();
		    	richtig = fragen.get(i).getRichtig();
		    	
			    if (fragen.get(i).getTyp() == 1)
			    {
			    	//Spielsituationsfrage
			    	typ = 1;
			    	a1_richtig = ((Spielsituationsfrage)fragen.get(i)).get_sf_richtig();
			    	a3_richtig = ((Spielsituationsfrage)fragen.get(i)).get_ps_richtig();
			    	if (((Spielsituationsfrage)fragen.get(i)).get_odf_richtig()) a2_richtig = 't';
			    	else a2_richtig = 'f';
			    	
			    	//Die ausgewählten Antworten ermitteln
			    	for (int x = 0; x<sf_kat.length; x++)
			    	{
			    		String sf_ausgewaehlt = ((Spielsituationsfrage)fragen.get(i)).sf_ausgewaehlt;
			    		if (sf_kat[x].equals(sf_ausgewaehlt))
			    		{
			    			a1 = x;
			    		}
			    	}
			    	if (odf_active)
			    	{
			    		for (int x = 0; x<odf_kat.length; x++)
				    	{
				    		String odf_ausgewaehlt = ((Spielsituationsfrage)fragen.get(i)).odf_ausgewaehlt;
				    		if (odf_kat[x].equals(odf_ausgewaehlt))
				    		{
				    			a2 = x;
				    		}
				    	}
			    	}
			    	for (int x = 0; x<ps_kat.length; x++)
			    	{
			    		String ps_ausgewaehlt = ((Spielsituationsfrage)fragen.get(i)).ps_ausgewaehlt;
			    		if (ps_kat[x].equals(ps_ausgewaehlt))
			    		{
			    			a3 = x;
			    		}
			    	}
			    	
			    }
			    else if (fragen.get(i).getTyp() == 2)
			    {
			    	//Multiplechoicefrage
			    	typ = 2;
			    	
			    	a1 = a2 = a3 = ((MultiplechoiceFrage)fragen.get(i)).ausgewaehlt;
			    }
			    
			    
			    DBFrage db_frage;
			    if (odf_active)
			    {
			    	db_frage = new DBFrage(bogenID, bogenNR, i, a1, a2, a3, fehlerpkt, datum, typ, richtig, a1_richtig, a2_richtig, a3_richtig);
			    }
			    else
			    {
			    	db_frage = new DBFrage(bogenID, bogenNR, i, a1, 99, a3, fehlerpkt, datum, typ, richtig, a1_richtig, a2_richtig, a3_richtig);
			    }
			    db.addFrage(db_frage);
		    }
		}
}