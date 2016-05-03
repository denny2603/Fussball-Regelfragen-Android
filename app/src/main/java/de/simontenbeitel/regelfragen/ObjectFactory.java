package de.simontenbeitel.regelfragen;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class ObjectFactory {
	
	private static Context context;
	
	public static ArrayList<Frage> createBogen(int id, int bogenNR, Context c)
	{
		context = c;
		DatabaseHandler db = new DatabaseHandler(context);
		
		ArrayList<DBFrage> dbfragen = db.getBoegenFragen(id);
		
		Storage.initialisiere();
		
		ArrayList<Frage> fragen = createBogen(Storage.bogenadressen[bogenNR-1]);
		
		// Die Antworten der Frage zuordnen
		for (int i = 0; i<fragen.size(); i++)
		{
			for (int x = 0; x<dbfragen.size(); x++)
			{
				if(i == dbfragen.get(x).getFrageNR())
				{
					if(fragen.get(i).getTyp() == 1)
					{
						//Spielsituationsfrage
						String a1, a2, a3;
						a2 = "";
						
						final String[] sf_kat = context.getResources().getStringArray(R.array.sfkat);
					    final String[] odf_kat = context.getResources().getStringArray(R.array.odfkat);
					    final String[] ps_kat = context.getResources().getStringArray(R.array.pskat);
					    
					    a1 = sf_kat[dbfragen.get(x).getA1()];
					    int tempa2 = dbfragen.get(x).getA2();	// ist 99, wenn odf inaktiv war
					    if (tempa2!=99)
					    {
					    	((Spielsituationsfrage)fragen.get(i)).setOdfActive(true);
					    	a2 = odf_kat[tempa2];
					    }
					    else
					    {
					    	((Spielsituationsfrage)fragen.get(i)).setOdfActive(false);
					    }
					    a3 = ps_kat[dbfragen.get(x).getA3()];
					    
					    if (tempa2!=99)
					    {
					    	((Spielsituationsfrage)fragen.get(i)).beantworte(a1, a2, a3);
					    }
					    else
					    {
					    	((Spielsituationsfrage)fragen.get(i)).beantworte(a1, a3);
					    }
					}
					else if(fragen.get(i).getTyp() == 2)
					{
						//Multiplechoicefrage
						int multipleantwort = dbfragen.get(x).getA1();
						((MultiplechoiceFrage)fragen.get(i)).beantworte(multipleantwort);
					}
				}
			}
		}
		
		return fragen;
	}
	
	public static ArrayList<Frage> createEinzelfragenBogen(String d, Context c)
	{
		context = c;
		char[] date = d.toCharArray();
		String datum = "" + date[6] + date[7] + date[8] + date[9] + "-" + date[3] + date[4] + "-" + date[0] + date[1];
		DatabaseHandler db = new DatabaseHandler(context);
		ArrayList<Frage> fragen = new ArrayList<Frage>();
		List<DBFrage> dbfragen = db.getEinzelFragen(datum);
		
		for (int i = 0; i<dbfragen.size(); i++)
		{
			fragen.add(addFrage(dbfragen.get(i)));
		}
		
		
		return fragen;
	}
	
	private static Frage addFrage(DBFrage dbfrage)
	{
		Storage.initialisiere();
		
		Frage f = createBogen(Storage.bogenadressen[dbfrage.getBogenNR()-1]).get(dbfrage.getFrageNR());
		
		if(f.getTyp() == 1)
		{
			//Spielsituationsfrage
			String a1, a2, a3;
			a2 = "";
			
			final String[] sf_kat = context.getResources().getStringArray(R.array.sfkat);
		    final String[] odf_kat = context.getResources().getStringArray(R.array.odfkat);
		    final String[] ps_kat = context.getResources().getStringArray(R.array.pskat);
		    
		    a1 = sf_kat[dbfrage.getA1()];
		    int tempa2 = dbfrage.getA2();	// ist 99, wenn odf inaktiv war
		    if (tempa2!=99)
		    {
		    	((Spielsituationsfrage)f).setOdfActive(true);
		    	a2 = odf_kat[tempa2];
		    }
		    else
		    {
		    	((Spielsituationsfrage)f).setOdfActive(false);
		    }
		    a3 = ps_kat[dbfrage.getA3()];
		    
		    if (tempa2!=99)
		    {
		    	((Spielsituationsfrage)f).beantworte(a1, a2, a3);
		    }
		    else
		    {
		    	((Spielsituationsfrage)f).beantworte(a1, a3);
		    }
		}
		else if(f.getTyp() == 2)
		{
			//Multiplechoicefrage
			int multipleantwort = dbfrage.getA1();
			((MultiplechoiceFrage)f).beantworte(multipleantwort);
		}
		
		return f;
	}
	
	private static ArrayList<Frage> createBogen(int id){
		JSONObject json_data;	//json objekt
		try{
			JSONArray jArray = new JSONArray(readBogen(id));	//den String aus der .txt auslesen
			Frage[] fragen = new Frage[jArray.length()];	//Die Frage-Objekte als Array erstellen (ist gleich der Länge des json-Array)
			for(int i=0;i<jArray.length();i++)
			{
				Log.d("try", "Versuche Frage " + i + " zu erstellen");
				json_data = jArray.getJSONObject(i);
				int typ = json_data.getInt("typ");
				String frage = (String) json_data.get("frage");
				String a1 = (String) json_data.get("a1");
				String a2 = (String) json_data.get("a2");
				String a3 = (String) json_data.get("a3");
				int loesung = json_data.getInt("loesung");
				
				switch(typ){
				case 1:	fragen[i] = new Spielsituationsfrage(frage, a1, a2, a3);
						Log.d("Create", "Frage " + i + " erstellt");
						break;
				case 2: fragen[i] = new MultiplechoiceFrage(frage, a1,a2,a3,loesung);
				Log.d("Create", "Frage " + i + " erstellt");
						break;
				default: break;
				}
			}
			return (new ArrayList<Frage>(Arrays.asList(fragen)));
		}catch(Exception e)
		{
			Log.e("log-tag","Error2"+e.toString());
		}
		return null;
	}
	
	private static String readBogen(int id){
		/*
		 * Die txt öfnnen
		 * die Resourcen-ID wird übergeben
		 */
		String mResponse="";
		try {
	        InputStream is = context.getResources().openRawResource(id);
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

}