package de.simontenbeitel.regelfragen;

import android.app.Application;
import android.content.Context;

import java.util.Random;

/**
 * @author Simon Tenbeitel
 */
public class RegelfragenApplication extends Application {

    private static Context sContext;
    private static Random sRandom;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        sRandom = new Random();
    }

    public static Context getContext() {
        return sContext;
    }

    public static Random getRandom() {
        return sRandom;
    }

}
