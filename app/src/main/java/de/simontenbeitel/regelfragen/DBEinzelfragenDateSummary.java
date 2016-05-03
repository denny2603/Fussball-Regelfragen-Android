package de.simontenbeitel.regelfragen;

public class DBEinzelfragenDateSummary {
	private String datum;
	private int anzahlFragen;
	private double fehlerpunkte;
	
	public DBEinzelfragenDateSummary()
	{
		
	}
	
	public DBEinzelfragenDateSummary(String datum, int anzahlFragen, double fehlerpunkte)
	{
		this.datum = datum;
		this.anzahlFragen = anzahlFragen;
		this.fehlerpunkte = fehlerpunkte;
	}
	
	public String getDatum()
	{
		return datum;
	}
	
	public void setDatum(String datum)
	{
		this.datum = datum;
	}
	
	public int getAnzahlFragen()
	{
		return anzahlFragen;
	}
	
	public void setAnzahlFragen(int anzahlFragen)
	{
		this.anzahlFragen = anzahlFragen;
	}
	
	public double getFehlerpunkte()
	{
		return fehlerpunkte;
	}
	
	public void setFehlerpunkte(double fehlerpunkte)
	{
		this.fehlerpunkte = fehlerpunkte;
	}

}
