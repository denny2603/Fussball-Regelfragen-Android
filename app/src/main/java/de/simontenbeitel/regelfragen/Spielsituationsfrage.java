package de.simontenbeitel.regelfragen;

import android.widget.ImageView;
import android.widget.TextView;

public class Spielsituationsfrage extends Frage{
	private String sf;					//Spielfortsetzung
	private String odf;					//Ort der Fortsetzung
	private String ps;					//Persönliche Strafe
	public String sf_ausgewaehlt;		//Ausgewählte Spielfortsetzung
	public String odf_ausgewaehlt;		//Ausgewählter Ort der Fortsetzung
	public String ps_ausgewaehlt;		//Ausgewählte Persönliche Strafe
	private boolean sf_richtig = false;
	private boolean odf_richtig = false;
	private boolean ps_richtig = false;
	private boolean odf_active;			//Nur für DB Auswertung benötigt
	
	public Spielsituationsfrage(String fragentext, String sf, String odf, String ps) {
		super(fragentext,1);
		this.sf = sf;
		this.odf = odf;
		this.ps = ps;
		sf_ausgewaehlt = "weiterspielen";		//Oberste Werte setzen (und damit die Voreinstellung im Spinner übernehmen)
		odf_ausgewaehlt = "weiterspielen";
		ps_ausgewaehlt = "keine persönliche Strafe";
	}
	
	public void beantworte (String a1, String a2, String a3)
	{
		sf_ausgewaehlt = a1;
		odf_ausgewaehlt = a2;
		ps_ausgewaehlt = a3;
		if (sf_ausgewaehlt.equals(sf)) sf_richtig = true;	//Überprüfen ob ausgewählte Spielfortsetzung korrekt ist
		if (odf_ausgewaehlt.equals(odf)) odf_richtig = true;	//" bei Ort der Fortsetzung
		if (ps_ausgewaehlt.equals(ps)) ps_richtig = true;		//" bei Persönlicher Strafe
		if (sf_richtig==true && odf_richtig==true && ps_richtig==true) richtig_beantwortet = true;	//Überprüfen, ob alles korrekt beantwortet ist
		if (richtig_beantwortet==true) fehlerpunkte = 0;	//Wenn alles richtig ist, keine Fehlerpunkte für diese Frage
		else
		{
			if(sf_richtig==true && odf_richtig==true) fehlerpunkte = 0.5;	//Wenn nur persönliche Strafe falsch beantwortet ist, ein halber Fehlerpunkt
			else fehlerpunkte = 1;	//ansonsten 1 Fehlerpunkt
		}
	}

	//Für Modus ohne Ort der Fortsetzung
	public void beantworte (String a1, String a3)
	{
		sf_ausgewaehlt = a1;
		ps_ausgewaehlt = a3;
		if (sf_ausgewaehlt.equals(sf)) sf_richtig = true;	//Überprüfen ob ausgewählte Spielfortsetzung korrekt ist
		if (ps_ausgewaehlt.equals(ps)) ps_richtig = true;		//" bei Persönlicher Strafe
		if (sf_richtig==true  && ps_richtig==true) richtig_beantwortet = true;	//Überprüfen, ob alles korrekt beantwortet ist
		if (richtig_beantwortet==true) fehlerpunkte = 0;	//Wenn alles richtig ist, keine Fehlerpunkte für diese Frage
		else
		{
			if(sf_richtig==true) fehlerpunkte = 0.5;	//Wenn nur persönliche Strafe falsch beantwortet ist, ein halber Fehlerpunkt
			else fehlerpunkte = 1;	//ansonsten 1 Fehlerpunkt
		}
	}
	
	public void aufloesen(TextView tv9, TextView tv11, TextView tv13, ImageView img2, ImageView img3, ImageView img4, TextView tv15)
	{
		tv9.setText(sf);		//In der Auswertung die Lösungen ausgeben
		tv11.setText(odf);
		tv13.setText(ps);
		if(sf_richtig) img2.setImageResource(R.drawable.richtig);	//Jeweils richtig oder falsch Neben jede Antwort
		else img2.setImageResource(R.drawable.falsch);
		if(odf_richtig) img3.setImageResource(R.drawable.richtig);
		else img3.setImageResource(R.drawable.falsch);
		if(ps_richtig) img4.setImageResource(R.drawable.richtig);
		else img4.setImageResource(R.drawable.falsch);
		tv15.setText(new Double(fehlerpunkte).toString());		//Fehlerpunkte ausgeben
	}


	//Für Modus ohne Ort der Fortsetzung
	public void aufloesen(TextView tv9, TextView tv11, TextView tv13, ImageView img2, ImageView img4, TextView tv15)
	{
		tv9.setText(sf);		//In der Auswertung die Lösungen ausgeben
		tv11.setText(odf);
		tv13.setText(ps);
		if(sf_richtig) img2.setImageResource(R.drawable.richtig);	//Jeweils richtig oder falsch Neben jede Antwort
		else img2.setImageResource(R.drawable.falsch);
		if(ps_richtig) img4.setImageResource(R.drawable.richtig);
		else img4.setImageResource(R.drawable.falsch);
		tv15.setText(new Double(fehlerpunkte).toString());		//Fehlerpunkte ausgeben
	}
	
	public boolean get_sf_richtig()
	{
		return sf_richtig;
	}
	
	public boolean get_odf_richtig()
	{
		return odf_richtig;
	}
	
	public boolean get_ps_richtig()
	{
		return ps_richtig;
	}
	
	public boolean getOdfActive()
	{
		return odf_active;
	}
	
	public void setOdfActive(boolean odf_active)
	{
		this.odf_active = odf_active;
	}
}