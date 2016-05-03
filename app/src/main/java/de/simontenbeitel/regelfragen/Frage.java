package de.simontenbeitel.regelfragen;

public class Frage{
	public String fragentext;
	protected double fehlerpunkte = 0;
	protected boolean richtig_beantwortet = false;
	private int typ = 0;
	
	public Frage(String fragentext, int typ)
	{
		this.fragentext = fragentext;
		this.typ = typ;
	}
	
	public boolean getRichtig()
	{
		return richtig_beantwortet;
	}
	public double getFehlerpunkte()
	{
		return fehlerpunkte;
	}
	public int getTyp(){
		return typ;
	}
}