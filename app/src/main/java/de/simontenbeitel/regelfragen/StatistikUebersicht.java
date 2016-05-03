package de.simontenbeitel.regelfragen;

public class StatistikUebersicht {
	
	private int anzahl_fragen_richtig;
	private int anzahl_fragen_falsch;
	private int anzahl_spielsituationsfragen_falsch;
	private int anzahl_multiplechoicefragen_falsch;
	private int anzahl_spielfortsetzung_falsch;
	private int anzahl_ortderfortsetzung_falsch;
	private int anzahl_persoenlichestrafe_falsch;
	private int anzahl_boegen_bestanden;
	private int anzahl_boegen_nicht_bestanden;
	private double fehlerpunkteschnitt_boegen;
	
	public StatistikUebersicht()
	{ }
	
	public StatistikUebersicht(int anzahl_fragen_richtig,
			int anzahl_fragen_falsch, int anzahl_spielsituationsfragen_falsch,
			int anzahl_multiplechoicefragen_falsch,
			int anzahl_spielfortsetzung_falsch,
			int anzahl_ortderfortsetzung_falsch,
			int anzahl_persoenlichestrafe_falsch, int anzahl_boegen_bestanden,
			int anzahl_boegen_nicht_bestanden, double fehlerpunkteschnitt_boegen) {
		this.anzahl_fragen_richtig = anzahl_fragen_richtig;
		this.anzahl_fragen_falsch = anzahl_fragen_falsch;
		this.anzahl_spielsituationsfragen_falsch = anzahl_spielsituationsfragen_falsch;
		this.anzahl_multiplechoicefragen_falsch = anzahl_multiplechoicefragen_falsch;
		this.anzahl_spielfortsetzung_falsch = anzahl_spielfortsetzung_falsch;
		this.anzahl_ortderfortsetzung_falsch = anzahl_ortderfortsetzung_falsch;
		this.anzahl_persoenlichestrafe_falsch = anzahl_persoenlichestrafe_falsch;
		this.anzahl_boegen_bestanden = anzahl_boegen_bestanden;
		this.anzahl_boegen_nicht_bestanden = anzahl_boegen_nicht_bestanden;
		this.fehlerpunkteschnitt_boegen = fehlerpunkteschnitt_boegen;
	}
	
	public int getAnzahl_fragen_richtig() {
		return anzahl_fragen_richtig;
	}
	public void setAnzahl_fragen_richtig(int anzahl_fragen_richtig) {
		this.anzahl_fragen_richtig = anzahl_fragen_richtig;
	}
	public int getAnzahl_fragen_falsch() {
		return anzahl_fragen_falsch;
	}
	public void setAnzahl_fragen_falsch(int anzahl_fragen_falsch) {
		this.anzahl_fragen_falsch = anzahl_fragen_falsch;
	}
	public int getAnzahl_spielsituationsfragen_falsch() {
		return anzahl_spielsituationsfragen_falsch;
	}
	public void setAnzahl_spielsituationsfragen_falsch(
			int anzahl_spielsituationsfragen_falsch) {
		this.anzahl_spielsituationsfragen_falsch = anzahl_spielsituationsfragen_falsch;
	}
	public int getAnzahl_multiplechoicefragen_falsch() {
		return anzahl_multiplechoicefragen_falsch;
	}
	public void setAnzahl_multiplechoicefragen_falsch(
			int anzahl_multiplechoicefragen_falsch) {
		this.anzahl_multiplechoicefragen_falsch = anzahl_multiplechoicefragen_falsch;
	}
	public int getAnzahl_spielfortsetzung_falsch() {
		return anzahl_spielfortsetzung_falsch;
	}
	public void setAnzahl_spielfortsetzung_falsch(int anzahl_spielfortsetzung_falsch) {
		this.anzahl_spielfortsetzung_falsch = anzahl_spielfortsetzung_falsch;
	}
	public int getAnzahl_ortderfortsetzung_falsch() {
		return anzahl_ortderfortsetzung_falsch;
	}
	public void setAnzahl_ortderfortsetzung_falsch(
			int anzahl_ortderfortsetzung_falsch) {
		this.anzahl_ortderfortsetzung_falsch = anzahl_ortderfortsetzung_falsch;
	}
	public int getAnzahl_persoenlichestrafe_falsch() {
		return anzahl_persoenlichestrafe_falsch;
	}
	public void setAnzahl_persoenlichestrafe_falsch(
			int anzahl_persoenlichestrafe_falsch) {
		this.anzahl_persoenlichestrafe_falsch = anzahl_persoenlichestrafe_falsch;
	}
	public int getAnzahl_boegen_bestanden() {
		return anzahl_boegen_bestanden;
	}
	public void setAnzahl_boegen_bestanden(int anzahl_boegen_bestanden) {
		this.anzahl_boegen_bestanden = anzahl_boegen_bestanden;
	}
	public int getAnzahl_boegen_nicht_bestanden() {
		return anzahl_boegen_nicht_bestanden;
	}
	public void setAnzahl_boegen_nicht_bestanden(int anzahl_boegen_nicht_bestanden) {
		this.anzahl_boegen_nicht_bestanden = anzahl_boegen_nicht_bestanden;
	}
	public double getFehlerpunkteschnitt_boegen() {
		return fehlerpunkteschnitt_boegen;
	}
	public void setFehlerpunkteschnitt_boegen(double fehlerpunkteschnitt_boegen) {
		this.fehlerpunkteschnitt_boegen = fehlerpunkteschnitt_boegen;
	}
}
