package de.simontenbeitel.regelfragen;

import android.app.Application;
import android.content.Context;

public class RegelfragenApplication extends Application {

    private static Context sAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sAppContext = this.getApplicationContext();
    }

    public static Context getAppContext() {
        return sAppContext;
    }

}
