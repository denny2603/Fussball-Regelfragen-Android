package de.simontenbeitel.regelfragen;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FrageboegenListAdapter extends ArrayAdapter<Frageboegen>{
	private final Context context;
	private ArrayList<Frageboegen> frageboegen;
 
	public FrageboegenListAdapter(Context context, ArrayList<Frageboegen> f) {
		super(context, R.layout.reihe_bogenwahl);
		this.context = context;
		frageboegen = f;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		
		if(rowView == null)
		{
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.reihe_bogenwahl, parent, false);
		}
		
		
		TextView textView = (TextView) rowView.findViewById(R.id.textView27);	//BogenNr anzeigen
		Storage.initialisiere();
		textView.setText(Storage.bogennamen[frageboegen.get(position).getNr()-1]);
	
		return rowView;
	}
	
	@Override
	public int getCount() {		//Länge der ListView festlegen (durch Anzahl der Fragebögen)
		return (frageboegen == null) ? 0 : frageboegen.size();
		}
}
