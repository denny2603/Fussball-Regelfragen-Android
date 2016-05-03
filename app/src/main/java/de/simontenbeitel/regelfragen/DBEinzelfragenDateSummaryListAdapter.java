package de.simontenbeitel.regelfragen;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DBEinzelfragenDateSummaryListAdapter extends ArrayAdapter<DBEinzelfragenDateSummary>{
	private final Context context;
	private List<DBEinzelfragenDateSummary> zusammenfassung;
	
	public DBEinzelfragenDateSummaryListAdapter(Context context, List<DBEinzelfragenDateSummary> zusammenfassung)
	{
		super(context, R.layout.reihe_statistik_bogen);
		this.context = context;
		this.zusammenfassung = zusammenfassung;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View rowView = convertView;
		if(rowView == null)
		{
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.reihe_statistik_bogen, parent, false);
		}
		
		TextView tv41 = (TextView) rowView.findViewById(R.id.textView41);	//Datum
		TextView tv42 = (TextView) rowView.findViewById(R.id.textView42);	//Anzahl Fragen
		TextView tv43 = (TextView) rowView.findViewById(R.id.textView43);	//Fehlerpunkte
		
		tv41.setText(zusammenfassung.get(position).getDatum());	//BogenNR ausgeben
		int anzahlFragen = zusammenfassung.get(position).getAnzahlFragen();
		if (anzahlFragen != 1) tv42.setText(anzahlFragen + " Fragen");
		else tv42.setText(anzahlFragen + " Frage");
		double fehlerpunkte = zusammenfassung.get(position).getFehlerpunkte();
		tv43.setText((new Double(fehlerpunkte).toString()) + " Fehler");
		
		return rowView;
	}
	
	@Override
	public int getCount() {		//Länge der ListView festlegen
		return (zusammenfassung == null) ? 0 : zusammenfassung.size();
		}
}