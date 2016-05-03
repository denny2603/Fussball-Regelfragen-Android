package de.simontenbeitel.regelfragen;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class Statistik extends FragmentActivity {
	
	public static ArrayList<Frage> fragen;
 
        
        /** 
         * The {@link android.support.v4.view.PagerAdapter} that will provide 
         * fragments for each of the sections. We use a 
         * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which 
         * will keep every loaded fragment in memory. If this becomes too memory 
         * intensive, it may be best to switch to a 
         * {@link android.support.v4.app.FragmentStatePagerAdapter}. 
         */  
        public static SectionsPagerAdapter mSectionsPagerAdapter;  
      
        /** 
         * The {@link ViewPager} that will host the section contents. 
         */  
        public static ViewPager mViewPager;  
      
        @Override  
        protected void onCreate(Bundle savedInstanceState) {  
            super.onCreate(savedInstanceState);  
            requestWindowFeature(Window.FEATURE_NO_TITLE);				//Keine Titelleiste
            setContentView(R.layout.statistik);            
    		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);	//Hochkant Format
      
            // Create the adapter that will return a fragment for each of the three  
            // primary sections of the app.  
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());  
      
            // Set up the ViewPager with the sections adapter.  
            mViewPager = (ViewPager) findViewById(R.id.pager);  
            mViewPager.setAdapter(mSectionsPagerAdapter);  
      
        }
      
        /** 
         * A {@link FragmentPagerAdapter} that returns a fragment corresponding to 
         * one of the sections/tabs/pages. 
         */  
        public class SectionsPagerAdapter extends FragmentPagerAdapter {  
      
            public SectionsPagerAdapter(FragmentManager fm) {  
                super(fm);  
            }  
      
            @Override  
            public Fragment getItem(int position) {  
                // getItem is called to instantiate the fragment for the given page.  
                // Return a DummySectionFragment (defined as a static inner class  
                // below) with the page number as its lone argument.  
            	Fragment fragment;
            	switch(position){
            	case 0: fragment = new GeneralFragment();
            			break;
            	case 1:	fragment = new BogenFragment();
            			break;
            	case 2: fragment = new EinzelfragenFragment();
            			break;
            	default: fragment = new DummySectionFragment();  
                Bundle args = new Bundle();  
                args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);  
                fragment.setArguments(args);
            	}
            	
                 
                return fragment;  
            }  
      
            @Override  
            public int getCount() {    
                return 3;  
            }  
      
            @Override  
            public CharSequence getPageTitle(int position) {  
                switch (position) {
                case 0:
                	return "Übersicht";
                case 1:  
                    return "Bögen";  
                case 2:  
                    return "Einzelfragen";  
                }  
                return null;  
            }  
            
          
        }	//Endclass SectionsPagerAdapter
        
        public static class GeneralFragment extends Fragment
        {
        	/*
        	 * Shows basic statistic data
        	 * 
        	 * Correct and Wrong Answers (incl. Percentage) Pie chart would be great
        	 * 
        	 * Where were the mistakes (summed up in categories)
        	 * - Spielsituationsfragen or Multiplechoicefragen
        	 * - Spielsituationsfrage:
        	 *   - Spielfortsetzung, Ort der Fortsetzung, pers. Strafe
        	 */
        	DatabaseHandler db;
        	View rootView;
        	StatistikUebersicht su;
        	
        	ImageView img8, img9, img10, img11, img12, img13, img14, img15, img16;	//Color field next to TextView
        	TextView tv45, tv46, tv48, tv49, tv51, tv52, tv53, tv55, tv56, tv57;
        	LinearLayout linear1, linear2, linear3, linear4;
        	
        	int[] colors_fehlerquote = { Color.rgb(0, 142, 89), Color.RED };
        	int[] colors_fehlerverteilung = { Color.rgb(42, 171, 224), Color.rgb(251, 197, 64) };
        	int[] colors_spielsituationsfrage = { Color.rgb(42, 171, 224), Color.rgb(251, 197, 64), Color.rgb(0, 142, 89) };
        	int[] colors_boegen = { Color.rgb(0, 142, 89), Color.RED };
        	
        	public GeneralFragment()
        	{
        	}
        	@Override
        	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        	{        		
        		rootView = inflater.inflate(R.layout.statistik_general, container, false);
        		db = new DatabaseHandler(getActivity());
        		su = db.getStatistikUebersicht();
        		DecimalFormat df = new DecimalFormat("#.##"); 
        		
        		float[] fehlerquote = { su.getAnzahl_fragen_richtig(), su.getAnzahl_fragen_falsch() };
        		float[] fehlerverteilung = { su.getAnzahl_spielsituationsfragen_falsch(), su.getAnzahl_multiplechoicefragen_falsch() };
        		float[] spielsituationsfrage = { su.getAnzahl_spielfortsetzung_falsch(), su.getAnzahl_ortderfortsetzung_falsch(), su.getAnzahl_persoenlichestrafe_falsch() };
        		float[] boegen = { su.getAnzahl_boegen_bestanden(), su.getAnzahl_boegen_nicht_bestanden() };
        		
        		int[] fehlerquote_absolut = { su.getAnzahl_fragen_richtig(), su.getAnzahl_fragen_falsch() };
        		int[] fehlerverteilung_absolut = { su.getAnzahl_spielsituationsfragen_falsch(), su.getAnzahl_multiplechoicefragen_falsch() };
        		int[] spielsituationsfrage_absolut = { su.getAnzahl_spielfortsetzung_falsch(), su.getAnzahl_ortderfortsetzung_falsch(), su.getAnzahl_persoenlichestrafe_falsch() };
        		int[] boegen_absolut = { su.getAnzahl_boegen_bestanden(), su.getAnzahl_boegen_nicht_bestanden() };
        		
        		MyPieChart pieChart_Fehlerquote = new MyPieChart(getActivity(), fehlerquote, colors_fehlerquote);        		
        		MyPieChart pieChart_Fehlerverteilung = new MyPieChart(getActivity(), fehlerverteilung, colors_fehlerverteilung);
        		MyPieChart pieChart_Spielsituationsfrage = new MyPieChart(getActivity(), spielsituationsfrage, colors_spielsituationsfrage);
        		MyPieChart pieChart_Boegen = new MyPieChart(getActivity(), boegen, colors_boegen);
        		
        		linear1 = (LinearLayout) rootView.findViewById(R.id.linear1);
        		linear2 = (LinearLayout) rootView.findViewById(R.id.linear2);
        		linear3 = (LinearLayout) rootView.findViewById(R.id.linear3);
        		linear4 = (LinearLayout) rootView.findViewById(R.id.linear4);
        		img8 = (ImageView) rootView.findViewById(R.id.img8);
        		img9 = (ImageView) rootView.findViewById(R.id.img9);
        		img10 = (ImageView) rootView.findViewById(R.id.img10);
        		img11 = (ImageView) rootView.findViewById(R.id.img11);
        		img12 = (ImageView) rootView.findViewById(R.id.img12);
        		img13 = (ImageView) rootView.findViewById(R.id.img13);
        		img14 = (ImageView) rootView.findViewById(R.id.img14);
        		img15 = (ImageView) rootView.findViewById(R.id.img15);
        		img16 = (ImageView) rootView.findViewById(R.id.img16);
        		tv45 = (TextView) rootView.findViewById(R.id.textView45);
        		tv46 = (TextView) rootView.findViewById(R.id.textView46);
        		tv48 = (TextView) rootView.findViewById(R.id.textView48);
        		tv49 = (TextView) rootView.findViewById(R.id.textView49);
        		tv51 = (TextView) rootView.findViewById(R.id.textView51);
        		tv52 = (TextView) rootView.findViewById(R.id.textView52);
        		tv53 = (TextView) rootView.findViewById(R.id.textView53);
        		tv55 = (TextView) rootView.findViewById(R.id.textView55);
        		tv56 = (TextView) rootView.findViewById(R.id.textView56);
        		tv57 = (TextView) rootView.findViewById(R.id.textView57);
        		
        		img8.setBackgroundColor(colors_fehlerquote[0]);
        		img9.setBackgroundColor(colors_fehlerquote[1]);
        		img10.setBackgroundColor(colors_fehlerverteilung[0]);
        		img11.setBackgroundColor(colors_fehlerverteilung[1]);
        		img12.setBackgroundColor(colors_spielsituationsfrage[0]);
        		img13.setBackgroundColor(colors_spielsituationsfrage[1]);
        		img14.setBackgroundColor(colors_spielsituationsfrage[2]);
        		img15.setBackgroundColor(colors_boegen[0]);
        		img16.setBackgroundColor(colors_boegen[1]);
        		
        		double richtigBeantwortet_relativ = 0, falschBeantwortet_relativ = 0;
        		if ((fehlerquote_absolut[0] + fehlerquote_absolut[1]) > 0 )
        		{
        			richtigBeantwortet_relativ = (fehlerquote_absolut[0] / (double) (fehlerquote_absolut[0] + fehlerquote_absolut[1]) *100);
            		falschBeantwortet_relativ = (fehlerquote_absolut[1] / (double) (fehlerquote_absolut[0] + fehlerquote_absolut[1]) *100);
        		}
        		
        		double spielsituationFalsch_relativ = 0, multiplechoiceFalsch_relativ = 0;
        		if ((fehlerverteilung_absolut[0] + fehlerverteilung_absolut[1]) > 0)
        		{
        			spielsituationFalsch_relativ = (fehlerverteilung_absolut[0] / (double) (fehlerverteilung_absolut[0] + fehlerverteilung_absolut[1]) *100);
            		multiplechoiceFalsch_relativ = (fehlerverteilung_absolut[1] / (double) (fehlerverteilung_absolut[0] + fehlerverteilung_absolut[1]) *100);
        		}
        		
        		double spielfortsetzungFalsch_relativ = 0, ortderfortsetzungFalsch_relativ = 0, persoenlichestrafeFalsch_relativ = 0;
        		if ((spielsituationsfrage_absolut[0] + spielsituationsfrage_absolut[1] + spielsituationsfrage_absolut[2]) > 0)
        		{
        			spielfortsetzungFalsch_relativ = (spielsituationsfrage_absolut[0] / (double) (spielsituationsfrage_absolut[0] + spielsituationsfrage_absolut[1] + spielsituationsfrage_absolut[2]) *100);
            		ortderfortsetzungFalsch_relativ = (spielsituationsfrage_absolut[1] / (double) (spielsituationsfrage_absolut[0] + spielsituationsfrage_absolut[1] + spielsituationsfrage_absolut[2]) *100);
            		persoenlichestrafeFalsch_relativ = (spielsituationsfrage_absolut[2] / (double) (spielsituationsfrage_absolut[0] + spielsituationsfrage_absolut[1] + spielsituationsfrage_absolut[2]) *100);
        		}
        		
        		double boegenRichtig_relativ = 0, boegenFalsch_relativ = 0;
        		if ((boegen_absolut[0] + boegen_absolut[1]) > 0)
        		{
        			boegenRichtig_relativ = (boegen_absolut[0] / (double) (boegen_absolut[0] + boegen_absolut[1]) *100);
        			boegenFalsch_relativ = (boegen_absolut[1] / (double) (boegen_absolut[0] + boegen_absolut[1]) *100);
        		}
        		
        		tv45.setText(fehlerquote_absolut[0] + " " + getResources().getQuantityString(R.plurals.frage, fehlerquote_absolut[0]) + " richtig (" + df.format(richtigBeantwortet_relativ) + "%)");
        		tv46.setText(fehlerquote_absolut[1] + " " + getResources().getQuantityString(R.plurals.frage, fehlerquote_absolut[1]) + " falsch (" + df.format(falschBeantwortet_relativ) + "%)");
        		tv48.setText(fehlerverteilung_absolut[0] + " " + getResources().getQuantityString(R.plurals.spielsituationsfrage, fehlerverteilung_absolut[0]) + " (" + df.format(spielsituationFalsch_relativ) + "%)");
        		tv49.setText(fehlerverteilung_absolut[1] + " " + getResources().getQuantityString(R.plurals.multiplechoicefrage, fehlerverteilung_absolut[1]) + " (" + df.format(multiplechoiceFalsch_relativ) + "%)");
        		tv51.setText(spielsituationsfrage_absolut[0] + " Spielfortsetzung (" + df.format(spielfortsetzungFalsch_relativ) + "%)");
        		tv52.setText(spielsituationsfrage_absolut[1] + " Ort der Fortsetzung (" + df.format(ortderfortsetzungFalsch_relativ) + "%)");
        		tv53.setText(spielsituationsfrage_absolut[2] + " persönliche Strafe (" + df.format(persoenlichestrafeFalsch_relativ) + "%)");
        		tv55.setText(boegen_absolut[0] + " " + getResources().getQuantityString(R.plurals.bogen, boegen_absolut[0]) + " bestanden (" + df.format(boegenRichtig_relativ) + "%)");
        		tv56.setText(boegen_absolut[1] + " " + getResources().getQuantityString(R.plurals.bogen, boegen_absolut[1]) + " nicht bestanden (" + df.format(boegenFalsch_relativ) + "%)");
        		tv57.setText("Fehlerpunkteschnitt: " + df.format(su.getFehlerpunkteschnitt_boegen()));


        		linear1.addView(pieChart_Fehlerquote);
        		linear2.addView(pieChart_Fehlerverteilung);
        		linear3.addView(pieChart_Spielsituationsfrage);
        		linear4.addView(pieChart_Boegen);
        		
        		return rootView;
        	}       	
        	
        	
        }	//Endclass GeneralFragment
        
        
        public static class BogenFragment extends Fragment implements OnItemClickListener
        {
        	DatabaseHandler db;
        	private List<DBBogen> boegen;
        	ListView list;
        	View rootView;

        	
        	
        	public BogenFragment()
        	{ 
        	}
        	 @Override  
             public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        	 {
        		 
        		 rootView = inflater.inflate(R.layout.statistik_bogen, container, false);
        		 db = new DatabaseHandler(getActivity());
        		 
        		 boegen = db.getAllBoegen();
        		 
        		 list = (ListView) rootView.findViewById(R.id.listView3);
        		 list.setAdapter(new DBBogenListAdapter(getActivity(), boegen));
        		 list.setOnItemClickListener(this);
        		 
        		 
        		 return rootView;
        	 }
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					//Man ist noch in allgemeiner Übersicht, und möchte einen Bogen detailiert anschauen
					int id = boegen.get(arg2).getID();
					int bogenNR = boegen.get(arg2).getBogen();
					fragen = ObjectFactory.createBogen(id, bogenNR, getActivity());
					Intent i = new Intent(getActivity(), DBBogenAuswertung.class);
					startActivity(i);
			}	//Endmethod onItemClick


        }
        
        public static class EinzelfragenFragment extends Fragment implements OnItemClickListener
        {
        	DatabaseHandler db;
        	private List<DBEinzelfragenDateSummary> zusammenfassung;
        	ListView list;
        	
        	public EinzelfragenFragment()
        	{
        	}
        	
        	 @Override  
             public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        	 {
        		 View rootView = inflater.inflate(R.layout.statistik_bogen, container, false);
        		 db = new DatabaseHandler(getActivity());
        		 
        		 zusammenfassung = db.getEinzelFragenDateSummary();
        		 
        		 list = (ListView) rootView.findViewById(R.id.listView3);
        		 list.setAdapter(new DBEinzelfragenDateSummaryListAdapter(getActivity(), zusammenfassung));
        		 list.setOnItemClickListener(this);
        		 
        		 return rootView;
        	 }

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				String datum = zusammenfassung.get(arg2).getDatum();
				fragen = ObjectFactory.createEinzelfragenBogen(datum, getActivity());
				Intent i = new Intent(getActivity(), DBBogenAuswertung.class);
				startActivity(i);
			}
        }
  
        /** 
         * A dummy fragment representing a section of the app, but that simply 
         * displays dummy text. 
         */  
        public static class DummySectionFragment extends Fragment {  
            /** 
             * The fragment argument representing the section number for this 
             * fragment. 
             */  
            public static final String ARG_SECTION_NUMBER = "section_number";  
      
            public DummySectionFragment() {  
            }  
      
            @Override  
            public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {  
                View rootView = inflater.inflate(R.layout.fragment_main_dummy, container, false);  
                TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);  
                dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));  
                return rootView;  
            }  
        }
      
    }  