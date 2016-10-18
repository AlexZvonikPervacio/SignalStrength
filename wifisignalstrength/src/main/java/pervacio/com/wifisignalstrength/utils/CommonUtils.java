package pervacio.com.wifisignalstrength.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

public class CommonUtils {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isWifiEnabled(Context context) {
        WifiManager mng = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return mng.isWifiEnabled();
    }

}