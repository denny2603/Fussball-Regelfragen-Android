package de.simontenbeitel.regelfragen;

import android.app.Application;

import de.simontenbeitel.regelfragen.injection.component.DaggerApplicationComponent;
import de.simontenbeitel.regelfragen.injection.component.ApplicationComponent;

public class RegelfragenApplication extends Application {

    ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mApplicationComponent = DaggerApplicationComponent.builder()
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

}
