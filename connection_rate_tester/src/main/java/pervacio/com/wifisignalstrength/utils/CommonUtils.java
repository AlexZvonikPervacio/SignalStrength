package pervacio.com.wifisignalstrength.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;

public class CommonUtils {

    /**
     * Check is network available.
     *
     * @param context the context
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * Check is wifi enabled.
     *
     * @param context the context
     */
    public static boolean isWifiEnabled(Context context) {
        WifiManager mng = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return mng.isWifiEnabled();
    }

    /**
     * Check is mobile network enabled.
     *
     * @param context the context
     */
    public static boolean isMobileNetworkEnabled(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && ConnectivityManager.TYPE_MOBILE == networkInfo.getType() && networkInfo.isConnected();
    }

}