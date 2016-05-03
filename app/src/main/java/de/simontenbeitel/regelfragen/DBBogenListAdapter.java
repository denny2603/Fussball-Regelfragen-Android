package de.simontenbeitel.regelfragen;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DBBogenListAdapter extends ArrayAdapter<DBBogen>{
	private final Context context;
	private List<DBBogen> boegen;

	public DBBogenListAdapter(Context context, List<DBBogen> boegen)
	{
		super(context, R.layout.reihe_statistik_bogen);
		this.context = context;
		this.boegen = boegen;
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
		
		TextView tv41 = (TextView) rowView.findViewById(R.id.textView41);	//BogenNR
		TextView tv42 = (TextView) rowView.findViewById(R.id.textView42);	//Datum
		TextView tv43 = (TextView) rowView.findViewById(R.id.textView43);	//Fehlerpunkte
		
		Storage.initialisiere();
		tv41.setText(Storage.bogennamen[boegen.get(position).getBogen()-1]);	//BogenNR ausgeben
		tv42.setText(boegen.get(position).getDatum());
		double fehlerpunkte = boegen.get(position).getFehler();
		tv43.setText((new Double(fehlerpunkte).toString()) + " Fehler");
		if (fehlerpunkte <= 6)
		{
			tv43.setTextColor(Color.rgb(0, 142, 89));
		}
		else
		{
			tv43.setTextColor(Color.RED);
		}
		
		return rowView;
	}
	
	@Override
	public int getCount() {		//Länge der ListView festlegen
		return (boegen == null) ? 0 : boegen.size();
		}
}
