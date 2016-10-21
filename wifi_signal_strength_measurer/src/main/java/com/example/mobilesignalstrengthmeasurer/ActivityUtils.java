package com.example.mobilesignalstrengthmeasurer;

import android.os.Handler;
import android.os.Looper;

public class ActivityUtils {

    private static void scheduleOnMainThread(Runnable r) {
        new Handler(Looper.getMainLooper()).post(r);
    }

    /**
     * Method uses to run tasks on main thread.
     *
     * @param r     the runnable runs on the main thread
     */
    public static void runOnMainThread(Runnable r) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            r.run();
        } else {
            scheduleOnMainThread(r);
        }
    }

}