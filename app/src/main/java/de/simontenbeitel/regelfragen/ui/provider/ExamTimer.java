package de.simontenbeitel.regelfragen.ui.provider;

import android.os.Handler;

/**
 * @author Simon Tenbeitel
 */
public class ExamTimer implements Runnable{

    private static ExamTimer sInstance;
    private Handler mHandler;
    private int secondsSinceStart;
    private TimerListener mListener;

    private ExamTimer() {
        secondsSinceStart = -1;
        mHandler = new Handler();
        mHandler.post(this);
    }

    public static synchronized ExamTimer getTimer(TimerListener mListener) {
        if(sInstance ==null)
            sInstance = new ExamTimer();
        sInstance.mListener = mListener;
        return sInstance;
    }

    public static synchronized void destroy() {
        sInstance = null;
    }

    @Override
    public void run() {
        secondsSinceStart++;
        mListener.OnTimerTick(secondsSinceStart);
        mHandler.postDelayed(this, 1000);
    }

    public interface TimerListener {
        void OnTimerTick(int seconds);
    }

}
