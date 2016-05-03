package de.simontenbeitel.regelfragen;

public class DBFrage {
	
	private int id;
	private int bogenID;
	private int bogenNR;
	private int frageNR;
	private int a1;
	private int a2;
	private int a3;
	private double fehler;
	private String datum;
	private int typ;
	private boolean richtig;
	private boolean a1_richtig;
	private char a2_richtig;
	private boolean a3_richtig;
	
	public DBFrage()
	{
		
	}
	
	public DBFrage(int id, int bogenID, int bogenNR, int frageNR, int a1, int a2, int a3, double fehler, String datum, int typ, boolean richtig, boolean a1_richtig, char a2_richtig, boolean a3_richtig)
	{
		this.id = id;
		this.bogenID = bogenID;
		this.bogenNR = bogenNR;
		this.frageNR = frageNR;
		this.a1 = a1;
		this.a2 = a2;
		this.a3 = a3;
		this.fehler = fehler;
		this.datum = datum;
		this.typ = typ;
		this.richtig = richtig;
		this.a1_richtig = a1_richtig;
		this.a2_richtig = a2_richtig;
		this.a3_richtig = a3_richtig;
	}
	
	public DBFrage(int bogenID, int bogenNR, int frageNR, int a1, int a2, int a3, double fehler, String datum, int typ, boolean richtig, boolean a1_richtig, char a2_richtig, boolean a3_richtig)
	{
		this.bogenID = bogenID;
		this.bogenNR = bogenNR;
		this.frageNR = frageNR;
		this.a1 = a1;
		this.a2 = a2;
		this.a3 = a3;
		this.fehler = fehler;
		this.datum = datum;
		this.typ = typ;
		this.richtig = richtig;
		this.a1_richtig = a1_richtig;
		this.a2_richtig = a2_richtig;
		this.a3_richtig = a3_richtig;
	}
	
	public int getID()
	{
		return id;
	}
	
	public void setID(int id)
	{
		this.id = id;
	}
	
	public int getBogenID()
	{
		return bogenID;
	}
	
	public void setBogenID(int bogenID)
	{
		this.bogenID = bogenID;
	}
	
	public int getBogenNR()
	{
		return bogenNR;
	}
	
	public void setBogenNR(int bogenNR)
	{
		this.bogenNR = bogenNR;
	}
	
	public int getFrageNR()
	{
		return frageNR;
	}
	
	public void setFrageNR(int frageNR)
	{
		this.frageNR = frageNR;
	}
	
	public int getA1()
	{
		return a1;
	}
	
	public void setA1(int a1)
	{
		this.a1 = a1;
	}
	
	public int getA2()
	{
		return a2;
	}
	
	public void setA2(int a2)
	{
		this.a2 = a2;
	}
	
	public int getA3()
	{
		return a3;
	}
	
	public void setA3(int a3)
	{
		this.a3 = a3;
	}
	
	public double getFehler()
	{
		return fehler;
	}
	
	public void setFehler(double fehler)
	{
		this.fehler = fehler;
	}
	
	public String getDatum()
	{
		return datum;
	}
	
	public void setDatum(String datum)
	{
		this.datum = datum;
	}
	
	public int getTyp()
	{
		return typ;
	}
	
	public void setTyp(int typ)
	{
		this.typ = typ;
	}
	
	public boolean getRichtig()
	{
		return richtig;
	}
	
	public void setRichtig(boolean richtig)
	{
		this.richtig = richtig;
	}
	
	public boolean getA1Richtig()
	{
		return a1_richtig;
	}
	
	public void setA1Richtig(boolean a1_richtig)
	{
		this.a1_richtig = a1_richtig;
	}
	
	public char getA2Richtig()
	{
		return a2_richtig;
	}
	
	public void setA2Richtig(char a2_richtig)
	{
		this.a2_richtig = a2_richtig;
	}
	
	public boolean getA3Richtig()
	{
		return a3_richtig;
	}
	
	public void setA3Richtig(boolean a3_richtig)
	{
		this.a3_richtig = a3_richtig;
	}

}
