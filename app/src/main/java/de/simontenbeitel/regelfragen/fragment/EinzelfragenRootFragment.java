package de.simontenbeitel.regelfragen.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Random;

import de.simontenbeitel.regelfragen.DBFrage;
import de.simontenbeitel.regelfragen.DatabaseHandler;
import de.simontenbeitel.regelfragen.Frage;
import de.simontenbeitel.regelfragen.Frageboegen;
import de.simontenbeitel.regelfragen.MultiplechoiceFrage;
import de.simontenbeitel.regelfragen.R;
import de.simontenbeitel.regelfragen.RegelfragenApplication;
import de.simontenbeitel.regelfragen.Spielsituationsfrage;

public class EinzelfragenRootFragment extends Fragment {
    final ArrayList<Frageboegen> boegen = new ArrayList<Frageboegen>();
    private int aktuellerBogen, aktuelleFrage;
    public static Frage[] fragen;
    private int[] originalposition;        //Die ursprüngliche Position der Frage (also die Nummer im Bogen)

    //Für Schwierigkeitsgrad
    boolean limit_difficulties, anwaerter_active, normal_active;

    boolean odf_active;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(RegelfragenApplication.getAppContext());
        limit_difficulties = sharedPrefs.getBoolean("pref_key_limit_difficulties", false);
        if (limit_difficulties) {
            anwaerter_active = sharedPrefs.getBoolean("pref_key_anwaerter_active", true);
            normal_active = sharedPrefs.getBoolean("pref_key_normal_active", true);
        } else {
            anwaerter_active = true;
            normal_active = true;
        }

        //Die IDs der Bögen in eine Liste setzen (wird evtl später benötigt, wenn man mehr als einen Bogen abgearbeitet hat)
        if (anwaerter_active) {
            boegen.add(new Frageboegen(R.raw.anwaerter1, 11));
            boegen.add(new Frageboegen(R.raw.anwaerter2, 12));
        }
        if (normal_active) {
            boegen.add(new Frageboegen(R.raw.bogen1, 1));
            boegen.add(new Frageboegen(R.raw.bogen2, 2));
            boegen.add(new Frageboegen(R.raw.bogen3, 3));
            boegen.add(new Frageboegen(R.raw.bogen4, 4));
            boegen.add(new Frageboegen(R.raw.bogen5, 5));
            boegen.add(new Frageboegen(R.raw.bogen6, 6));
            boegen.add(new Frageboegen(R.raw.bogen7, 7));
            boegen.add(new Frageboegen(R.raw.bogen8, 8));
            boegen.add(new Frageboegen(R.raw.bogen9, 9));
            boegen.add(new Frageboegen(R.raw.bogen10, 10));
        }

        aktuellerBogen = 0;
        aktuelleFrage = -1;
        Collections.shuffle(boegen);    //Die Bögen durchmischen
        createBogen(boegen.get(aktuellerBogen).getId());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_container, container, false);
        showNextQuestion();
        return view;
    }

    public void showNextQuestion() {
        inkrementiereFrage();
        Fragment questionFragment = EinzelfragenQuestionFragment.getInstance(fragen[aktuelleFrage]);
        getChildFragmentManager().beginTransaction().replace(R.id.container, questionFragment).commit();
    }

    public void showSolution() {
        insertIntoDB();
        Fragment solutionFragment = EinzelfragenSolutionFragment.getInstance(fragen[aktuelleFrage]);
        getChildFragmentManager().beginTransaction().replace(R.id.container, solutionFragment).commit();
    }

    private void inkrementiereFrage() {
        aktuelleFrage++;
        if (aktuelleFrage == fragen.length) {
            //Das Ende des aktuellen Bogens wurde erreicht, daher zum nächsten Bogen
            aktuellerBogen++;
            if (aktuellerBogen != boegen.size()) {
                createBogen(boegen.get(aktuellerBogen).getId());
                aktuelleFrage = 0;
            } else {
                new AlertDialog.Builder(getContext())
                        .setTitle("Ende")
                        .setMessage("Sie haben alle Fragen bearbeitet")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO st: handle this unlikely situation
                            }
                        })
                        .show();
            }
        }
    }

    private void createBogen(int id) {
        JSONObject json_data;    //json objekt
        try {
            JSONArray jArray = new JSONArray(readBogen(id));    //den String aus der .txt auslesen
            fragen = new Frage[jArray.length()];    //Die Frage-Objekte als Array erstellen (ist gleich der Länge des json-Array)
            for (int i = 0; i < jArray.length(); i++) {
                json_data = jArray.getJSONObject(i);
                int typ = json_data.getInt("typ");
                String frage = (String) json_data.get("frage");
                String a1 = (String) json_data.get("a1");
                String a2 = (String) json_data.get("a2");
                String a3 = (String) json_data.get("a3");
                int loesung = json_data.getInt("loesung");

                switch (typ) {
                    case 1:
                        fragen[i] = new Spielsituationsfrage(frage, a1, a2, a3);
                        break;
                    case 2:
                        fragen[i] = new MultiplechoiceFrage(frage, a1, a2, a3, loesung);
                        break;
                    default:
                        break;
                }

            }
        } catch (Exception e) {
            Log.e("log-tag", "Error2" + e.toString());
        }

        fragenMischen();
    }

    private void fragenMischen() {
        Frage tmp;
        int rand;
        Random r = new Random();
        originalposition = new int[fragen.length];
        int tempposition;
        for (int i = 0; i < originalposition.length; i++) {
            originalposition[i] = i;
        }
        for (int i = 0; i < fragen.length; i++) {
            rand = r.nextInt(fragen.length);

            tmp = fragen[i];
            tempposition = originalposition[i];

            fragen[i] = fragen[rand];
            originalposition[i] = originalposition[rand];

            fragen[rand] = tmp;
            originalposition[rand] = tempposition;
        }
    }

    private String readBogen(int id) {
        /*
         * Die txt öfnnen
		 * die Resourcen-ID wird übergeben
		 */
        String mResponse = "";
        try {
            InputStream is = getResources().openRawResource(id);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            mResponse = new String(buffer, "UTF-8");    //Jede .txt muss als utf-8 gespeichert werden
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mResponse;
    }

    private void insertIntoDB() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(RegelfragenApplication.getAppContext());
        odf_active = sharedPrefs.getBoolean("pref_key_odf_active", true);
        DatabaseHandler db = new DatabaseHandler(RegelfragenApplication.getAppContext());

        Date dateNow = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder datumBuilder = new StringBuilder(sdf.format(dateNow));
        String datum = new String(datumBuilder);

        final String[] sf_kat = getResources().getStringArray(R.array.sfkat);
        final String[] odf_kat = getResources().getStringArray(R.array.odfkat);
        final String[] ps_kat = getResources().getStringArray(R.array.pskat);

        int a1 = 0;
        int a2 = 0;
        int a3 = 0;
        int typ = 0;

        double fehlerpkt = fragen[aktuelleFrage].getFehlerpunkte();

        int bogenNR = boegen.get(aktuellerBogen).getNr();

        boolean richtig = fragen[aktuelleFrage].getRichtig();
        boolean a1_richtig = true, a3_richtig = true;
        char a2_richtig = 't';

        if (fragen[aktuelleFrage].getTyp() == 1) {
            //Spielsituationsfrage
            typ = 1;
            a1_richtig = ((Spielsituationsfrage) fragen[aktuelleFrage]).get_sf_richtig();
            a3_richtig = ((Spielsituationsfrage) fragen[aktuelleFrage]).get_ps_richtig();
            a2_richtig = 'p';

            //Die ausgewählten Antworten ermitteln
            for (int x = 0; x < sf_kat.length; x++) {
                String sf_ausgewaehlt = ((Spielsituationsfrage) fragen[aktuelleFrage]).sf_ausgewaehlt;
                if (sf_kat[x].equals(sf_ausgewaehlt)) {
                    a1 = x;
                }
            }
            if (odf_active) {
                if (((Spielsituationsfrage) fragen[aktuelleFrage]).get_odf_richtig())
                    a2_richtig = 't';
                else a2_richtig = 'f';
                for (int x = 0; x < odf_kat.length; x++) {
                    String odf_ausgewaehlt = ((Spielsituationsfrage) fragen[aktuelleFrage]).odf_ausgewaehlt;
                    if (odf_kat[x].equals(odf_ausgewaehlt)) {
                        a2 = x;
                    }
                }
            }

            for (int x = 0; x < ps_kat.length; x++) {
                String ps_ausgewaehlt = ((Spielsituationsfrage) fragen[aktuelleFrage]).ps_ausgewaehlt;
                if (ps_kat[x].equals(ps_ausgewaehlt)) {
                    a3 = x;
                }
            }

        } else if (fragen[aktuelleFrage].getTyp() == 2) {
            //Multiplechoicefrage
            typ = 2;

            a1 = a2 = a3 = ((MultiplechoiceFrage) fragen[aktuelleFrage]).ausgewaehlt;
            a1_richtig = true;
            a3_richtig = true;
            a2_richtig = 't';
        }


        DBFrage db_frage;

        if (odf_active) {
            db_frage = new DBFrage(0, bogenNR, originalposition[aktuelleFrage], a1, a2, a3, fehlerpkt, datum, typ, richtig, a1_richtig, a2_richtig, a3_richtig);
        } else {
            db_frage = new DBFrage(0, bogenNR, originalposition[aktuelleFrage], a1, 99, a3, fehlerpkt, datum, typ, richtig, a1_richtig, a2_richtig, a3_richtig);
        }
        db.addFrage(db_frage);
    }
}
