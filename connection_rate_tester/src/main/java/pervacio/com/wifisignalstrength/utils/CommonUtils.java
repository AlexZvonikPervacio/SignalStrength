package pervacio.com.wifisignalstrength.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.IntDef;
import android.support.annotation.WorkerThread;
import android.util.Log;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.HttpURLConnection;
import java.net.URL;

public class CommonUtils {

    private static final String TAG = CommonUtils.class.getSimpleName();

    public static final int NONE = 0;
    public static final int WIFI = 1;
    public static final int MOBILE = 2;

    /**
     * Get network connection type. Select one of the {@link ConnectionType} .
     *
     * @param context is the context
     */
    @ConnectionType
    public static int typeConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected()) {
                    return WIFI;
                }
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected()) {
                    return MOBILE;
                }
        }
        return NONE;
    }

    /**
     * Check is network connection valid.
     *
     * @param context is the context
     */
    @WorkerThread
    public static boolean hasInternetAccess(Context context) {
        if (isNetworkAvailable(context)) {
            try {
                HttpURLConnection url = (HttpURLConnection) new URL("http://clients3.google.com/generate_204")
                        .openConnection();
                url.setRequestProperty("User-Agent", "Android");
                url.setRequestProperty("Connection", "close");
                url.setConnectTimeout(1500);
                url.connect();
                return (url.getResponseCode() == 204 && url.getContentLength() == 0);
            } catch (IOException e) {
                Log.e(TAG, "Error checking internet connection", e);
            }
        }
        return false;
    }

    /**
     * Check is network available.
     *
     * @param context is the context
     */
    private static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @IntDef({NONE, WIFI, MOBILE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ConnectionType {
    }

}