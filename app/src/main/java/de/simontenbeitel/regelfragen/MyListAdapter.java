package de.simontenbeitel.regelfragen;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyListAdapter extends ArrayAdapter<Frage>{
	private final Context context;
	private ArrayList<Frage> fragen;
 
	public MyListAdapter(Context context, ArrayList<Frage> f) {
		super(context, R.layout.reihe);
		this.context = context;
		fragen = f;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		
		if(rowView == null)
		{
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.reihe, parent, false);
		}
		
		
		TextView textView = (TextView) rowView.findViewById(R.id.textView4);	//Fragenummer in Auswertung angeben
		ImageView imageView = (ImageView) rowView.findViewById(R.id.img1);		//Grafik, ob Frage korrekt oder falsch beantwortet ist
		
		if(position<fragen.size())
		{
			textView.setText("Frage " + (position+1));		//Fragenummer ausgeben
		
			if (fragen.get(position).getRichtig()==true)
			{
				imageView.setImageResource(R.drawable.richtig);		//Wenn Frage richtig beantwortet ist, grüner Haken
			}
			else
			{
				imageView.setImageResource(R.drawable.falsch);		//Ansonsten (falsch beantwortet), rotes X
			}
		}
		else
		{
			double fehlerpunkte = 0;
			for (int x = 0; x<fragen.size(); x++)
			{
				fehlerpunkte += fragen.get(x).getFehlerpunkte();	//Fehlerpunkte von allen Fragen addieren
			}
			textView.setText("Fehlerpunkte: " + (new Double(fehlerpunkte).toString()));		//Gesamtzahl der Fehlerpunkte in dem Bogen ausgeben
			imageView.setVisibility(4);		//Keine Grafik neben Anzahl der Fehlerpunkte
		}
		return rowView;
	}
	
	@Override
	public int getCount() {		//Länge der ListView festlegen (durch Anzahl der Fragen)
		return (fragen == null) ? 0 : fragen.size()+1;
		}

}
