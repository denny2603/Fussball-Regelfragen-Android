package de.simontenbeitel.regelfragen;

public class Frageboegen {
	private int id;	//ID der .txt Datei des Bogens in R.raw
	private int nr;	//BogenNr
	
	public Frageboegen(int id, int nr)
	{
		this.id = id;
		this.nr = nr;
	}
	
	public int getId()
	{
		return id;
	}
	
	public int getNr()
	{
		return nr;
	}
}
