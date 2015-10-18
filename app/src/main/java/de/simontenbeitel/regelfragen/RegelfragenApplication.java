package de.simontenbeitel.regelfragen;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.util.Random;

/**
 * This is a subclass of {@link Application} used to provide shared objects for this app, such as
 * the {@link Context} and {@link Tracker}.
 * @author Simon Tenbeitel
 */
public class RegelfragenApplication extends Application {

    private static Context sContext;
    private static Random sRandom;
    private Tracker mTracker;

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

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (null == mTracker) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }

}
