package de.simontenbeitel.regelfragen;

import android.app.Application;

import de.simontenbeitel.regelfragen.injection.component.DaggerApplicationComponent;
import de.simontenbeitel.regelfragen.injection.component.ApplicationComponent;

public class RegelfragenApplication extends Application {

    ApplicationComponent mApplicationComponent;
    static RegelfragenApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        mApplicationComponent = DaggerApplicationComponent.builder()
                .build();
    }

    public static RegelfragenApplication getInstance() {
        return sInstance;
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

}
