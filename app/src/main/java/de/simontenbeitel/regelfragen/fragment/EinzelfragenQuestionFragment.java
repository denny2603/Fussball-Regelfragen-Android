package de.simontenbeitel.regelfragen.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.simontenbeitel.regelfragen.Frage;
import de.simontenbeitel.regelfragen.MultiplechoiceFrage;
import de.simontenbeitel.regelfragen.R;
import de.simontenbeitel.regelfragen.RegelfragenApplication;
import de.simontenbeitel.regelfragen.Spielsituationsfrage;

public class EinzelfragenQuestionFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, RadioGroup.OnCheckedChangeListener {

    private static final String KEY_QUESTION = "question";
    private Frage frage;
    private Unbinder unbinder;

    //Für Spielsituationsfrage
    @Nullable
    @BindView(R.id.spinner4)
    Spinner s4;
    @Nullable
    @BindView(R.id.spinner5)
    Spinner s5;
    @Nullable
    @BindView(R.id.spinner6)
    Spinner s6;
    String sf_antwort, odf_antwort, ps_antwort;        //die ausgewählten Antworten
    boolean odf_active;

    //Für Multiplechoicefrage
    @Nullable
    @BindView(R.id.radioGroup2)
    RadioGroup rg2;
    @Nullable
    @BindView(R.id.radio4)
    RadioButton rb4;
    @Nullable
    @BindView(R.id.radio5)
    RadioButton rb5;
    @Nullable
    @BindView(R.id.radio6)
    RadioButton rb6;
    int multipleantwort;    //Nr der ausgewählten Antwort (0,1,2)

    Button aufloesen;
    TextView fragetext;
    ImageView img2, img3, img4;

    public static EinzelfragenQuestionFragment getInstance(Frage frage) {
        EinzelfragenQuestionFragment fragment = new EinzelfragenQuestionFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_QUESTION, frage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        frage = (Frage) getArguments().getSerializable(KEY_QUESTION);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @LayoutRes int layoutRes;
        switch (frage.getTyp()) {
            case 1:
                layoutRes = R.layout.spielsituation_einzelfrage;
                break;
            case 2:
                layoutRes = R.layout.multiplechoice_einzelfrage;
                break;
            default:
                throw new RuntimeException("Cannot find layout resource for question type " + frage.getTyp());
        }
        View view = inflater.inflate(layoutRes, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeCommonViews();
        showQuestion();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View view) {
        // Show solution
        if (frage.getTyp() == 1) {
            if (odf_active)
                ((Spielsituationsfrage) frage).beantworte(sf_antwort, odf_antwort, ps_antwort);
            else ((Spielsituationsfrage) frage).beantworte(sf_antwort, ps_antwort);
        }
        ((EinzelfragenRootFragment) getParentFragment()).showSolution();
    }

    private void initializeCommonViews() {
        switch (frage.getTyp()) {
            case 1:
                initializeCommonViewsFromGamesituationLayout();
                break;
            case 2:
                initializeCommonViewsFromMultiplechoiceLayout();
                break;
        }
        aufloesen.setOnClickListener(this);
    }

    private void initializeCommonViewsFromGamesituationLayout() {
        aufloesen = ButterKnife.findById(getView(), R.id.button13);
        fragetext = ButterKnife.findById(getView(), R.id.textView31);
    }

    private void initializeCommonViewsFromMultiplechoiceLayout() {
        aufloesen = ButterKnife.findById(getView(), R.id.button4);
        fragetext = ButterKnife.findById(getView(), R.id.textView29);
    }

    private void showQuestion() {
        fragetext.setText(frage.fragentext);
        switch (frage.getTyp()) {
            case 1:
                showGamesituationQuestion();
                break;
            case 2:
                showMultiplechoiceQuestion();
                break;
        }
    }

    private void showGamesituationQuestion() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(RegelfragenApplication.getAppContext());
        odf_active = sharedPrefs.getBoolean("pref_key_odf_active", true);

        //Die Spinner befüllen
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getContext(), R.array.sfkat, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s4 = ButterKnife.findById(getView(), R.id.spinner4);
        s4.setAdapter(adapter1);
        s4.setOnItemSelectedListener(this);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(), R.array.odfkat, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s5.setAdapter(adapter2);
        s5.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(getContext(), R.array.pskat, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s6.setAdapter(adapter3);
        s6.setOnItemSelectedListener(this);

        if (!odf_active) {
            s5.setVisibility(View.INVISIBLE);
            TextView ueberschrift_odf = ButterKnife.findById(getView(), R.id.textView33);
            ueberschrift_odf.setVisibility(View.INVISIBLE);
        }
    }


    private void showMultiplechoiceQuestion() {
        rg2.setOnCheckedChangeListener(this);    //Wenn ein Item ausgewählt wird, sofort notieren!!!
        rb4.setChecked(true);
        rb4.setText(((MultiplechoiceFrage) frage).antwortmoeglichkeiten[0]);
        rb5.setText(((MultiplechoiceFrage) frage).antwortmoeglichkeiten[1]);
        rb6.setText(((MultiplechoiceFrage) frage).antwortmoeglichkeiten[2]);
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        //Ein Spinner wurde betätigt
        //arg0 entspricht dem Spinner, der das onItemSelected ausgelöst hat
        if (arg0 == s4) {
            sf_antwort = (String) s4.getSelectedItem();    //ausgewählte Antwort aktualisieren
        } else if (arg0 == s5) {
            odf_antwort = (String) s5.getSelectedItem();
        } else if (arg0 == s6) {
            ps_antwort = (String) s6.getSelectedItem();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCheckedChanged(RadioGroup arg0, int checkedID) {
        /*
         * Wenn in der RadioGroup ein Item ausgewählt wurde, muss rausgefunden werden,
		 * welcher Button angeklickt wurde
		 *
		 * Danach die Antwort speichern
		 */
        if (checkedID == R.id.radio4) multipleantwort = 0;
        else if (checkedID == R.id.radio5) multipleantwort = 1;
        else if (checkedID == R.id.radio6) multipleantwort = 2;

    }
}
