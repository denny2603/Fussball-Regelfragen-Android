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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.simontenbeitel.regelfragen.Frage;
import de.simontenbeitel.regelfragen.MultiplechoiceFrage;
import de.simontenbeitel.regelfragen.R;
import de.simontenbeitel.regelfragen.RegelfragenApplication;
import de.simontenbeitel.regelfragen.Spielsituationsfrage;

public class EinzelfragenSolutionFragment extends Fragment implements View.OnClickListener {

    private static final String KEY_QUESTION = "question";
    private Frage frage;
    private Unbinder unbinder;

    Button neueFrage, ende;
    TextView fragetext, fehlerpunkte;
    ImageView img2, img3, img4;
    boolean odf_active;

    //Spielsituationsfrage
    @Nullable
    @BindView(R.id.textView8)
    TextView sf1;
    @Nullable
    @BindView(R.id.textView9)
    TextView sf2;
    @Nullable
    @BindView(R.id.textView10)
    TextView odf1;
    @Nullable
    @BindView(R.id.textView11)
    TextView odf2;
    @Nullable
    @BindView(R.id.textView12)
    TextView ps1;
    @Nullable
    @BindView(R.id.textView13)
    TextView ps2;

    //Multiplechoice Frage
    @Nullable
    @BindView(R.id.textView21)
    TextView a1;
    @Nullable
    @BindView(R.id.textView22)
    TextView a2;
    @Nullable
    @BindView(R.id.textView23)
    TextView a3;

    public static EinzelfragenSolutionFragment getInstance(Frage frage) {
        EinzelfragenSolutionFragment fragment = new EinzelfragenSolutionFragment();
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
                layoutRes = R.layout.beantwortet_spielsituation;
                break;
            case 2:
                layoutRes = R.layout.beantwortet_multiple;
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
        showSolution();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View view) {
        if (view == neueFrage) {
            ((EinzelfragenRootFragment) getParentFragment()).showNextQuestion();
        } else if (view == ende) {
            getParentFragment().getActivity().finish();
        }

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
        ende.setOnClickListener(this);
        neueFrage.setOnClickListener(this);
    }

    private void initializeCommonViewsFromGamesituationLayout() {
        fragetext = ButterKnife.findById(getView(), R.id.textView6);
        ende = ButterKnife.findById(getView(), R.id.button3);
        neueFrage = ButterKnife.findById(getView(), R.id.button2);
        fehlerpunkte = ButterKnife.findById(getView(), R.id.textView15);
        img2 = ButterKnife.findById(getView(), R.id.img2);        // Richtig/Falsch Grafik Spielfortsetzung
        img3 = ButterKnife.findById(getView(), R.id.img3);        // Richtig/Falsch Grafik Ort der Fortsetzung
        img4 = ButterKnife.findById(getView(), R.id.img4);        // Richtig/Falsch Grafik persönliche Strafe
    }

    private void initializeCommonViewsFromMultiplechoiceLayout() {
        fragetext = ButterKnife.findById(getView(), R.id.textView20);
        ende = ButterKnife.findById(getView(), R.id.button10);
        neueFrage = ButterKnife.findById(getView(), R.id.button9);
        fehlerpunkte = ButterKnife.findById(getView(), R.id.textView24);
        img2 = ButterKnife.findById(getView(), R.id.img5);        // Richtig/Falsch Grafik Spielfortsetzung
        img3 = ButterKnife.findById(getView(), R.id.img6);        // Richtig/Falsch Grafik Ort der Fortsetzung
        img4 = ButterKnife.findById(getView(), R.id.img7);        // Richtig/Falsch Grafik persönliche Strafe

    }

    private void showSolution() {
        fragetext.setText(frage.fragentext);
        neueFrage.setText("Neue Frage");
        ende.setText("Ende");
        switch (frage.getTyp()) {
            case 1:
                showGamesituationSolution();
                break;
            case 2:
                showMultiplechoiceSolution();
                break;
        }
    }

    private void showGamesituationSolution() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(RegelfragenApplication.getAppContext());
        odf_active = sharedPrefs.getBoolean("pref_key_odf_active", true);

        sf1.setText(((Spielsituationsfrage) frage).sf_ausgewaehlt);
        if (odf_active)
            odf1.setText(((Spielsituationsfrage) frage).odf_ausgewaehlt);
        ps1.setText(((Spielsituationsfrage) frage).ps_ausgewaehlt);
        if (odf_active)
            ((Spielsituationsfrage) frage).aufloesen(sf2, odf2, ps2, img2, img3, img4, fehlerpunkte);
        else
            ((Spielsituationsfrage) frage).aufloesen(sf2, odf2, ps2, img2, img4, fehlerpunkte);
    }

    private void showMultiplechoiceSolution() {
        a1.setText(((MultiplechoiceFrage) frage).antwortmoeglichkeiten[0]);
        a2.setText(((MultiplechoiceFrage) frage).antwortmoeglichkeiten[1]);
        a3.setText(((MultiplechoiceFrage) frage).antwortmoeglichkeiten[2]);
        ((MultiplechoiceFrage) frage).aufloesen(img2, img3, img4, fehlerpunkte);
    }
}
