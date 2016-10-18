package pervacio.com.wifisignalstrength;

import android.content.Context;
import android.net.wifi.WifiManager;

import java.util.Timer;
import java.util.TimerTask;

import pervacio.com.wifisignalstrength.utils.ActivityUtils;

public class SignalMeasurer {

    //in constr req
    private Context mContext;

    //in constr req
    private int wifiStrengthLevelsCount = 5;
    private int wifiUpdatePeriod = 5000;
    private WifiStrengthListener mStrengthListener;

    public SignalMeasurer(Context mContext, int wifiStrengthLevelsCount, int wifiUpdatePeriod, WifiStrengthListener mStrengthListener) {
        this.mContext = mContext;
        this.wifiStrengthLevelsCount = wifiStrengthLevelsCount;
        this.wifiUpdatePeriod = wifiUpdatePeriod;
        this.mStrengthListener = mStrengthListener;
        mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
    }

    //not in constr
    private WifiManager mWifiManager;

    public int getWifiStrengthLevel() {
        return WifiManager.calculateSignalLevel(getWifiStrengthRssi(), wifiStrengthLevelsCount);
    }

    public int getWifiStrengthRssi() {
        return mWifiManager.getConnectionInfo().getRssi();
    }

    public void startListenWifiStrength() {
        startListenWifiStrength(mStrengthListener, wifiUpdatePeriod);
    }

    public void startListenWifiStrength(final WifiStrengthListener listener, int updatePeriod) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                final int rssi = mWifiManager.getConnectionInfo().getRssi();
                final int level = WifiManager.calculateSignalLevel(rssi, wifiStrengthLevelsCount);
                ActivityUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        if (listener != null) {
                            listener.onStrengthUpdate(level, rssi);
                        }
                    }
                });
            }
        };
        if (wifiUpdatePeriod < 100){
            wifiUpdatePeriod = 100;
        }
        timer.schedule(task, 0, updatePeriod);
    }

    public interface WifiStrengthListener {
        void onStrengthUpdate(int level, int rssi);
    }

    public static class Builder {

        private Context mContext;
        private int mWifiStrengthLevelsCount;
        private int mWifiUpdatePeriod;
        private SignalMeasurer.WifiStrengthListener mStrengthListener;

        public Builder(Context mContext) {
            this.mContext = mContext;
        }

        public Builder setContext(Context mContext) {
            this.mContext = mContext;
            return this;
        }

        public Builder setWifiStrengthLevelsCount(int wifiStrengthLevelsCount) {
            mWifiStrengthLevelsCount = wifiStrengthLevelsCount;
            return this;
        }

        public Builder setWifiUpdatePeriod(int wifiUpdatePeriod) {
            this.mWifiUpdatePeriod = wifiUpdatePeriod;
            return this;
        }

        public Builder setStrengthListener(SignalMeasurer.WifiStrengthListener mStrengthListener) {
            this.mStrengthListener = mStrengthListener;
            return this;
        }

        public SignalMeasurer create() {
            return new SignalMeasurer(mContext, mWifiStrengthLevelsCount, mWifiUpdatePeriod, mStrengthListener);
        }
    }

}
