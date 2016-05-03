package de.simontenbeitel.regelfragen;

import android.widget.ImageView;
import android.widget.TextView;

public class MultiplechoiceFrage extends Frage{
	public String[] antwortmoeglichkeiten = new String[3];
	private int loesung;
	public int ausgewaehlt;
	public MultiplechoiceFrage(String fragentext, String a1, String a2, String a3, int loesung) {
		super(fragentext,2);
		antwortmoeglichkeiten[0] = a1;
		antwortmoeglichkeiten[1] = a2;
		antwortmoeglichkeiten[2] = a3;
		this.loesung = loesung;
		ausgewaehlt = 0;
	}
	
	public void beantworte(int pos){
		ausgewaehlt = pos;
		if (ausgewaehlt==loesung){
			richtig_beantwortet = true;
			fehlerpunkte = 0;
		}
		else{
			richtig_beantwortet = false;
			fehlerpunkte = 1;
		}
	}
	
	public void aufloesen(ImageView img2, ImageView img3, ImageView img4, TextView tv15)
	{
		tv15.setText(new Double(fehlerpunkte).toString());	//Fehlerpunkte ausgeben
		//Die Lösung markieren
		switch(loesung)
		{
			case 0: img2.setImageResource(R.drawable.richtig);
					break;
			case 1: img3.setImageResource(R.drawable.richtig);
					break;
			case 2: img4.setImageResource(R.drawable.richtig);
					break;
			default:break;
		}
		
		//Wenn die falsche Antwort gewählt wurde, markiere diese
		if(!richtig_beantwortet)
		{
			switch(ausgewaehlt)
			{
				case 0: img2.setImageResource(R.drawable.falsch);
						break;
				case 1: img3.setImageResource(R.drawable.falsch);
						break;
				case 2: img4.setImageResource(R.drawable.falsch);
						break;
				default:break;
			}
		}
	}
}