package pervacio.com.signalstrength.utils;

import android.os.Handler;
import android.os.Looper;

public class ActivityUtils {

    public static void scheduleOnMainThread(Runnable r) {
        new Handler(Looper.getMainLooper()).post(r);
    }

}
