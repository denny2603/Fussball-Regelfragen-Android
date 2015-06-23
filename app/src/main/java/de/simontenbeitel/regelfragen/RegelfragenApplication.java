package de.simontenbeitel.regelfragen;

import android.app.Application;
import android.content.Context;

/**
 * @author Simon Tenbeitel
 */
public class RegelfragenApplication extends Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
    }

    public static Context getContext() {
        return sContext;
    }
}
