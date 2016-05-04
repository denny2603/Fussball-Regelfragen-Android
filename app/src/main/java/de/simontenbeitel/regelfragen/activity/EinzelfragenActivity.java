package de.simontenbeitel.regelfragen.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import de.simontenbeitel.regelfragen.R;
import de.simontenbeitel.regelfragen.fragment.EinzelfragenRootFragment;

@Deprecated
public class EinzelfragenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        if (null == savedInstanceState) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new EinzelfragenRootFragment()).commit();
        }
    }

}
