package pervacio.com.signalstrength.utils;

import android.os.Handler;
import android.os.Looper;

public class ActivityUtils {

    public static void scheduleOnMainThread(Runnable r) {
        new Handler(Looper.getMainLooper()).post(r);
    }

    public static void scheduleOnMainThread(Runnable r, long delay) {
        new Handler(Looper.getMainLooper()).postDelayed(r, delay);
    }

    public static void runOnMainThread(Runnable r) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            r.run();
        } else {
            scheduleOnMainThread(r);
        }
    }

}
