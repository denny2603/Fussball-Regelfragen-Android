package de.simontenbeitel.regelfragen;

public class Storage {
	public static final String[] bogennamen = new String[12];
	public static final int[] bogenadressen = new int[12];
	
	public static void initialisiere()
	{
		bogennamen[0] = "Bogen 1";
		bogennamen[1] = "Bogen 2";
		bogennamen[2] = "Bogen 3";
		bogennamen[3] = "Bogen 4";
		bogennamen[4] = "Bogen 5";
		bogennamen[5] = "Bogen 6";
		bogennamen[6] = "Bogen 7";
		bogennamen[7] = "Bogen 8";
		bogennamen[8] = "Bogen 9";
		bogennamen[9] = "Bogen 10";
		bogennamen[10] = "Anwärter 1";
		bogennamen[11] = "Anwärter 2";
		
		bogenadressen[0] = R.raw.bogen1;
		bogenadressen[1] = R.raw.bogen2;
		bogenadressen[2] = R.raw.bogen3;
		bogenadressen[3] = R.raw.bogen4;
		bogenadressen[4] = R.raw.bogen5;
		bogenadressen[5] = R.raw.bogen6;
		bogenadressen[6] = R.raw.bogen7;
		bogenadressen[7] = R.raw.bogen8;
		bogenadressen[8] = R.raw.bogen9;
		bogenadressen[9] = R.raw.bogen10;
		bogenadressen[10] = R.raw.anwaerter1;
		bogenadressen[11] = R.raw.anwaerter2;
	}

}
