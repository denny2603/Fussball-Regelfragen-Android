package de.simontenbeitel.regelfragen;

import java.util.ArrayList;
import java.util.List;
 
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {
	// All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "Statistics";
 
    // Tabellennamen
    private static final String TABLE_BOEGEN = "boegen";
    private static final String TABLE_FRAGEN = "fragen";
 
    // Spaltennamen der Tabelle Bögen
    private static final String KEY_ID = "id";
    private static final String KEY_NR = "bogen";
    private static final String KEY_FEHLER = "fehler";
    private static final String KEY_DATUM = "datum";
    
    // Spaltennamen der Tabelle Fragen
    //private static final String KEY_ID = "id";	//existiert bereits in Tabelle Bögen
    private static final String KEY_BO_ID = "bogenid";
    private static final String KEY_BO_NR = "bogennr";
    private static final String KEY_FR_NR = "fragenr";
    private static final String KEY_A1 = "a1";
    private static final String KEY_A2 = "a2";
    private static final String KEY_A3 = "a3";
    private static final String KEY_FE_PK = "fehler"; //Fehlerpunkte
    private static final String KEY_TYP = "typ";
    private static final String KEY_RICHTIG = "richtig";	//Gesamte Frage richtig beantwortet
    private static final String KEY_RA1 = "ra1";		//A1 Richtig
    private static final String KEY_RA2 = "ra2";		//A2 Richtig
    private static final String KEY_RA3 = "ra3";		//A3 Richtig
    
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BOEGEN_TABLE = "CREATE TABLE " + TABLE_BOEGEN + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
        		+ KEY_NR + " INTEGER," 
                + KEY_FEHLER + " REAL,"
                + KEY_DATUM + " TEXT"        		
                + ")";
        db.execSQL(CREATE_BOEGEN_TABLE);
        
        String CREATE_FRAGEN_TABLE = "CREATE TABLE " + TABLE_FRAGEN + "("
        		+ KEY_ID + " INTEGER PRIMARY KEY,"
        		+ KEY_BO_ID + " INTEGER,"
        		+ KEY_BO_NR + " INTEGER,"
        		+ KEY_FR_NR + " INTEGER,"
        		+ KEY_A1 + " INTEGER,"
        		+ KEY_A2 + " INTEGER,"
        		+ KEY_A3 + " INTEGER,"
        		+ KEY_FE_PK + " REAL,"
        		+ KEY_DATUM + " TEXT,"
        		+ KEY_TYP  + " INTEGER,"
        		+ KEY_RICHTIG + " TEXT,"
        		+ KEY_RA1 + " TEXT,"
        		+ KEY_RA2 + " TEXT,"
        		+ KEY_RA3 + " TEXT"
        		+ ")";
        db.execSQL(CREATE_FRAGEN_TABLE);
    }
    
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	// mache nichts, da im moment nicht vorgesehen
    }
    
    
    
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
    public void addBogen(DBBogen bogen)
    {
    	SQLiteDatabase db = this.getWritableDatabase();
    	 
        ContentValues values = new ContentValues();
        values.put(KEY_NR, bogen.getBogen());
        values.put(KEY_FEHLER, bogen.getFehler());
        values.put(KEY_DATUM, bogen.getDatum());
        
        db.insert(TABLE_BOEGEN, null, values);		//Returns die Nummer der Zeile in der der Datensatz in die Tbl gespeichert wurde
        
        db.close(); // Closing database connection
    }
    
    public void addFrage(DBFrage frage)
    {
    	SQLiteDatabase db = this.getWritableDatabase();
   	 
        ContentValues values = new ContentValues();
        values.put(KEY_BO_ID, frage.getBogenID());
        values.put(KEY_BO_NR, frage.getBogenNR());
        values.put(KEY_FR_NR, frage.getFrageNR());
        values.put(KEY_A1, frage.getA1());
        values.put(KEY_A2, frage.getA2());
        values.put(KEY_A3, frage.getA3());
        values.put(KEY_FE_PK, frage.getFehler());
        values.put(KEY_DATUM, frage.getDatum());
        values.put(KEY_TYP, frage.getTyp());
        String richtig, ra1, ra3;
        if(frage.getRichtig()) richtig = "t";
        else richtig = "f";
        if(frage.getA1Richtig()) ra1 = "t";
        else ra1 = "f";
        if(frage.getA3Richtig()) ra3 = "t";
        else ra3 = "f";
        String ra2 = Character.toString(frage.getA2Richtig());
        values.put(KEY_RICHTIG, richtig);
        values.put(KEY_RA1, ra1);
        values.put(KEY_RA2, ra2);
        values.put(KEY_RA3, ra3);
        
        db.insert(TABLE_FRAGEN, null, values);
        db.close();
    }
    
    public List<DBBogen> getAllBoegen()
    {
    	List<DBBogen> boegen = new ArrayList<DBBogen>();
    	
    	String selectBefehl = "SELECT  * FROM " + TABLE_BOEGEN + " ORDER BY " + KEY_DATUM +  " DESC";
    	
    	SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectBefehl, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do
            {
            	DBBogen bogen = new DBBogen();
            	bogen.setID(Integer.parseInt(cursor.getString(0)));
            	bogen.setBogen(Integer.parseInt(cursor.getString(1)));
            	bogen.setFehler(Double.parseDouble(cursor.getString(2)));
            	char[] date = cursor.getString(3).toCharArray();
            	String datum = ("" + date[8] + date[9] + "." + date[5] + date[6] + "." + date[0] + date[1] + date[2] + date[3]);
            	bogen.setDatum(datum);
            	
            	boegen.add(bogen);
            } while (cursor.moveToNext());
        }
        
        return boegen;
    }
    
    public int getBogenID(String datum)
    {
    	String selectBefehl = "SELECT " + KEY_ID + " FROM " + TABLE_BOEGEN + " WHERE " + KEY_DATUM + " = '" + datum + "'";
    	int id = 0;
    	
    	SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectBefehl, null);
        
        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
        	id = Integer.parseInt(cursor.getString(0));
        }
        
        return id;
    }
    
    public List<DBFrage> getEinzelFragen(String datum)
    {
    	List<DBFrage> fragen = new ArrayList<DBFrage>();

    	String selectBefehl = "SELECT * FROM " + TABLE_FRAGEN + " WHERE " + KEY_BO_ID + " = 0 AND " + KEY_DATUM + " = '" + datum + "' ORDER BY " + KEY_ID + " ASC";
    	
    	SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectBefehl, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
        	do
        	{
        		DBFrage frage = new DBFrage();
        		frage.setID(Integer.parseInt(cursor.getString(0)));
        		frage.setBogenID(Integer.parseInt(cursor.getString(1)));
        		frage.setBogenNR(Integer.parseInt(cursor.getString(2)));
        		frage.setFrageNR(Integer.parseInt(cursor.getString(3)));
        		frage.setA1(Integer.parseInt(cursor.getString(4)));
        		frage.setA2(Integer.parseInt(cursor.getString(5)));
        		frage.setA3(Integer.parseInt(cursor.getString(6)));
        		frage.setFehler(Double.parseDouble(cursor.getString(7)));
        		frage.setDatum(cursor.getString(8));
        		frage.setTyp(Integer.parseInt(cursor.getString(9)));
        		

        		char r = cursor.getString(10).charAt(0);
        		char ra1 = cursor.getString(11).charAt(0);
        		char a2_richtig = cursor.getString(12).charAt(0);
        		char ra3 = cursor.getString(13).charAt(0);
        		
        		if (r == 't') frage.setRichtig(true);
        		else frage.setRichtig(false);
        		if (ra1 == 't') frage.setA1Richtig(true);
        		else frage.setA1Richtig(false);
        		frage.setA2Richtig(a2_richtig);
        		if(ra3 == 't') frage.setA3Richtig(true);
        		else frage.setA3Richtig(false);        		

        		
        		fragen.add(frage);
        	} while (cursor.moveToNext());
        }
    	    	
    	return fragen;
    }
    
    public List<DBFrage> getEinzelFragen()
    {
    	List<DBFrage> fragen = new ArrayList<DBFrage>();
    	
    	String selectBefehl = "SELECT * FROM " + TABLE_FRAGEN + " WHERE " + KEY_BO_ID + " = 0 ORDER BY " + KEY_DATUM + " DESC";
    	
    	SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectBefehl, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
        	do
        	{
        		DBFrage frage = new DBFrage();
        		frage.setID(Integer.parseInt(cursor.getString(0)));
        		frage.setBogenID(Integer.parseInt(cursor.getString(1)));
        		frage.setBogenNR(Integer.parseInt(cursor.getString(2)));
        		frage.setFrageNR(Integer.parseInt(cursor.getString(3)));
        		frage.setA1(Integer.parseInt(cursor.getString(4)));
        		frage.setA2(Integer.parseInt(cursor.getString(5)));
        		frage.setA3(Integer.parseInt(cursor.getString(6)));
        		frage.setFehler(Double.parseDouble(cursor.getString(7)));
        		frage.setTyp(Integer.parseInt(cursor.getString(8)));
        		

        		char r = cursor.getString(9).charAt(0);
        		char ra1 = cursor.getString(10).charAt(0);
        		char a2_richtig = cursor.getString(11).charAt(0);
        		char ra3 = cursor.getString(12).charAt(0);
        		
        		if (r == 't') frage.setRichtig(true);
        		else frage.setRichtig(false);
        		if (ra1 == 't') frage.setA1Richtig(true);
        		else frage.setA1Richtig(false);
        		frage.setA2Richtig(a2_richtig);
        		if(ra3 == 't') frage.setA3Richtig(true);
        		else frage.setA3Richtig(false);        		

        		
        		fragen.add(frage);
        	} while (cursor.moveToNext());
        }
    	    	
    	return fragen;
    }
    
    public List<DBEinzelfragenDateSummary> getEinzelFragenDateSummary()
    {
    	List<DBEinzelfragenDateSummary> zusammenfassung = new ArrayList<DBEinzelfragenDateSummary>();
    	
    	String selectBefehl = "SELECT " + KEY_DATUM + ", count(*), sum(" + KEY_FE_PK + ") FROM " + TABLE_FRAGEN + " WHERE " + KEY_BO_ID + " = 0 GROUP BY " + KEY_DATUM + " ORDER BY " + KEY_DATUM + " DESC";
    	
    	SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectBefehl, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
        	do
        	{
        		DBEinzelfragenDateSummary summary = new DBEinzelfragenDateSummary();
        		char[] date = cursor.getString(0).toCharArray();
            	String datum = ("" + date[8] + date[9] + "." + date[5] + date[6] + "." + date[0] + date[1] + date[2] + date[3]);
        		summary.setDatum(datum);
        		summary.setAnzahlFragen(Integer.parseInt(cursor.getString(1)));
        		summary.setFehlerpunkte(Double.parseDouble(cursor.getString(2)));
        		
        		zusammenfassung.add(summary);
        	} while (cursor.moveToNext());
        }
    	return zusammenfassung;
    }
    
    public ArrayList<DBFrage> getBoegenFragen(int id)
    {
    	ArrayList<DBFrage> fragen = new ArrayList<DBFrage>();
    	
    	String selectBefehl = "SELECT * FROM " + TABLE_FRAGEN + " WHERE " + KEY_BO_ID + " = " + id;
    	
    	SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectBefehl, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
        	do
        	{
        		DBFrage frage = new DBFrage();
        		frage.setID(Integer.parseInt(cursor.getString(0)));
        		frage.setBogenID(Integer.parseInt(cursor.getString(1)));
        		frage.setBogenNR(Integer.parseInt(cursor.getString(2)));
        		frage.setFrageNR(Integer.parseInt(cursor.getString(3)));
        		frage.setA1(Integer.parseInt(cursor.getString(4)));
        		frage.setA2(Integer.parseInt(cursor.getString(5)));
        		frage.setA3(Integer.parseInt(cursor.getString(6)));
        		frage.setFehler(Double.parseDouble(cursor.getString(7)));
        		frage.setDatum(cursor.getString(8));
        		frage.setTyp(Integer.parseInt(cursor.getString(9)));
        		

        		char r = cursor.getString(10).charAt(0);
        		char ra1 = cursor.getString(11).charAt(0);
        		char a2_richtig = cursor.getString(12).charAt(0);
        		char ra3 = cursor.getString(13).charAt(0);
        		
        		if (r == 't') frage.setRichtig(true);
        		else frage.setRichtig(false);
        		if (ra1 == 't') frage.setA1Richtig(true);
        		else frage.setA1Richtig(false);
        		frage.setA2Richtig(a2_richtig);
        		if(ra3 == 't') frage.setA3Richtig(true);
        		else frage.setA3Richtig(false);        		

        		
        		fragen.add(frage);
        	} while (cursor.moveToNext());
        }
    	
    	return fragen;
    }
    
    public StatistikUebersicht getStatistikUebersicht()
    {
    	StatistikUebersicht su = new StatistikUebersicht();
    	
    	String selectBefehl;
    	
    	SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        
        //Select number of correct answered questions
        selectBefehl = "SELECT COUNT(*) FROM " + TABLE_FRAGEN + " WHERE " + KEY_RICHTIG + " = 't'";
        cursor = db.rawQuery(selectBefehl, null);
        if (cursor.moveToFirst())
        {
        	su.setAnzahl_fragen_richtig(Integer.parseInt(cursor.getString(0)));
        }
        //Select number of wrong answered questions
        selectBefehl = "SELECT COUNT(*) FROM " + TABLE_FRAGEN + " WHERE " + KEY_RICHTIG + " = 'f'";
        cursor = db.rawQuery(selectBefehl, null);
        if (cursor.moveToFirst())
        {
        	su.setAnzahl_fragen_falsch(Integer.parseInt(cursor.getString(0)));
        }
        
        //Select number of wrong answered Spielsituationsfragen
        selectBefehl = "SELECT COUNT(*) FROM " + TABLE_FRAGEN + " WHERE " + KEY_TYP  + " = 1 AND " + KEY_RICHTIG + " = 'f'";
        cursor = db.rawQuery(selectBefehl, null);
        if (cursor.moveToFirst())
        {
        	su.setAnzahl_spielsituationsfragen_falsch(Integer.parseInt(cursor.getString(0)));
        }
        //Select number of wrong answered Multiplechoicefragen
        selectBefehl = "SELECT COUNT(*) FROM " + TABLE_FRAGEN + " WHERE " + KEY_TYP  + " = 2 AND " + KEY_RICHTIG + " = 'f'";
        cursor = db.rawQuery(selectBefehl, null);
        if (cursor.moveToFirst())
        {
        	su.setAnzahl_multiplechoicefragen_falsch(Integer.parseInt(cursor.getString(0)));
        }
        
        //Select number of wrong answered Spielfortsetzungen in Spielsituationsfragen
        selectBefehl = "SELECT COUNT(*) FROM " + TABLE_FRAGEN + " WHERE " + KEY_TYP  + " = 1 AND " + KEY_RA1 + " = 'f'";
        cursor = db.rawQuery(selectBefehl, null);
        if (cursor.moveToFirst())
        {
        	su.setAnzahl_spielfortsetzung_falsch(Integer.parseInt(cursor.getString(0)));
        }
        //Select number of wrong answered Ort der Fortsetzung in Spielsituationsfragen
        selectBefehl = "SELECT COUNT(*) FROM " + TABLE_FRAGEN + " WHERE " + KEY_TYP  + " = 1 AND " + KEY_RA2 + " = 'f'";
        cursor = db.rawQuery(selectBefehl, null);
        if (cursor.moveToFirst())
        {
        	su.setAnzahl_ortderfortsetzung_falsch(Integer.parseInt(cursor.getString(0)));
        }
        //Select number of wrong answered pers. Strafen in Spielsituationsfragen
        selectBefehl = "SELECT COUNT(*) FROM " + TABLE_FRAGEN + " WHERE " + KEY_TYP  + " = 1 AND " + KEY_RA3 + " = 'f'";
        cursor = db.rawQuery(selectBefehl, null);
        if (cursor.moveToFirst())
        {
        	su.setAnzahl_persoenlichestrafe_falsch(Integer.parseInt(cursor.getString(0)));
        }
        
        //Select number of passed Bögen
        selectBefehl = "SELECT COUNT(*) FROM " + TABLE_BOEGEN + " WHERE " + KEY_FEHLER + " <= 6";
        cursor = db.rawQuery(selectBefehl, null);
        if (cursor.moveToFirst())
        {
        	su.setAnzahl_boegen_bestanden(Integer.parseInt(cursor.getString(0)));
        }
        //Select number of not passed Bögen
        selectBefehl = "SELECT COUNT(*) FROM " + TABLE_BOEGEN + " WHERE " + KEY_FEHLER + " > 6";
        cursor = db.rawQuery(selectBefehl, null);
        if (cursor.moveToFirst())
        {
        	su.setAnzahl_boegen_nicht_bestanden(Integer.parseInt(cursor.getString(0)));
        }        
        //Select average Fehlerpunkte
        selectBefehl = "SELECT AVG(" + KEY_FEHLER + ") FROM " + TABLE_BOEGEN;
        cursor = db.rawQuery(selectBefehl, null);
        if (cursor.moveToFirst())
        {
        	if(cursor.getString(0) != null) su.setFehlerpunkteschnitt_boegen(Double.parseDouble(cursor.getString(0)));
        	else su.setFehlerpunkteschnitt_boegen(0);
        }
        
        Log.d("Fragen richtig", su.getAnzahl_fragen_richtig() + "");
        Log.d("Fragen falsch", su.getAnzahl_fragen_falsch() + "");
        Log.d("Spielsit falsch", su.getAnzahl_spielsituationsfragen_falsch() + "");
        Log.d("Multip falsch", su.getAnzahl_multiplechoicefragen_falsch() + "");
        Log.d("SF falsch", su.getAnzahl_spielfortsetzung_falsch() + "");
        Log.d("ODF falsch", su.getAnzahl_ortderfortsetzung_falsch() + "");
        Log.d("PS falsch", su.getAnzahl_persoenlichestrafe_falsch() + "");
        Log.d("Bestanden", su.getAnzahl_boegen_bestanden() + "");
        Log.d("nicht best", su.getAnzahl_boegen_nicht_bestanden() + "");
        Log.d("Schnitt", su.getFehlerpunkteschnitt_boegen() + "");       
        
    	return su;
    }

}
