package pervacio.com.wifisignalstrength.utils;

import android.os.Handler;
import android.os.Looper;

public class ActivityUtils {

    private static void scheduleOnMainThread(Runnable r) {
        new Handler(Looper.getMainLooper()).post(r);
    }

    /**
     * Method uses to schedule tasks on main thread.
     *
     * @param r     the runnable scheduled to execute in the main thread
     * @param delay the delay
     */
    public static void scheduleOnMainThread(Runnable r, long delay) {
        new Handler(Looper.getMainLooper()).postDelayed(r, delay);
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
