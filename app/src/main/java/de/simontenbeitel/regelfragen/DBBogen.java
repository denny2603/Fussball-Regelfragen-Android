package de.simontenbeitel.regelfragen;

public class DBBogen {
	
	private int id;
	private int bogen;
	private double fehler;
	private String datum;
	
	public DBBogen()
	{
		
	}
	
	public DBBogen(int id, int bogen, double fehler, String datum)
	{
		this.id = id;
		this.bogen = bogen;
		this.fehler = fehler;
		this.datum = datum;
	}
	
	public DBBogen(int bogen, double fehler, String datum)
	{
		this.bogen = bogen;
		this.fehler = fehler;
		this.datum = datum;
	}

	public int getID()
	{
		return id;
	}
	
	public void setID(int id)
	{
		this.id = id;
	}
	
	public int getBogen()
	{
		return bogen;
	}
	
	public void setBogen(int bogen)
	{
		this.bogen = bogen;
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
}
