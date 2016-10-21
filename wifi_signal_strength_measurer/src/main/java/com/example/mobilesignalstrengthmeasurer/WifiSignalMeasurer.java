package com.example.mobilesignalstrengthmeasurer;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.support.annotation.IntRange;

import java.util.Timer;
import java.util.TimerTask;

public class WifiSignalMeasurer {

    private static final int MIN_LEVELS = 2;
    private static final int MAX_LEVELS = 7;
    private static final int MIN_UPDATE_TIME = 1000;
    private static final int DEFAULT_UPDATE_TIME = 5000;
    private static final int DEFAULT_LEVELS_COUNT = 5;

    private Context mContext;
    @IntRange(from = MIN_LEVELS, to = MAX_LEVELS)
    private int mWifiStrengthLevelsCount = DEFAULT_LEVELS_COUNT;
    @IntRange(from = MIN_UPDATE_TIME)
    private int mWifiUpdatePeriod = DEFAULT_UPDATE_TIME;
    private Timer mTimer;
    private WifiStrengthListener mStrengthListener;

    private WifiSignalMeasurer(Context context, int wifiStrengthLevelsCount, int wifiUpdatePeriod, WifiStrengthListener strengthListener) {
        this.mContext = context;
        this.mWifiStrengthLevelsCount = wifiStrengthLevelsCount;
        this.mWifiUpdatePeriod = wifiUpdatePeriod;
        this.mStrengthListener = strengthListener;
        mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
    }

    private WifiManager mWifiManager;

    public int getWifiStrengthLevel() {
        return WifiManager.calculateSignalLevel(getWifiStrengthRssi(), mWifiStrengthLevelsCount);
    }

    public int getWifiStrengthRssi() {
        return mWifiManager.getConnectionInfo().getRssi();
    }

    public void startListenWifiStrength(int updatePeriod, WifiStrengthListener listener) {
        this.mWifiUpdatePeriod = updatePeriod;
        this.mStrengthListener = listener;
        startListenWifiStrength();
    }

    public void startListenWifiStrength() {
        if (mTimer == null) {
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    final int rssi = mWifiManager.getConnectionInfo().getRssi();
                    final int level = WifiManager.calculateSignalLevel(rssi, mWifiStrengthLevelsCount);
                    final boolean hasInternetAccess = CommonUtils.hasInternetAccess(mContext);
                    ActivityUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mStrengthListener != null) {
                                if (level == 0 || !hasInternetAccess) {
                                    mStrengthListener.onStrengthFailure(mContext.getString(R.string.no_internet_connection));
                                } else if (CommonUtils.typeConnection(mContext) != CommonUtils.WIFI) {
                                    mStrengthListener.onStrengthFailure(mContext.getString(R.string.wifi_not_connected));
                                } else {
                                    mStrengthListener.onStrengthUpdate(level, rssi);
                                }
                            }
                        }
                    });
                }
            };
            if (mWifiUpdatePeriod < 1000) {
                mWifiUpdatePeriod = 1000;
            }
            timer.schedule(task, 0, mWifiUpdatePeriod);
        }
    }

    public void stopListenWifiStrength() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    public interface WifiStrengthListener {
        void onStrengthUpdate(int level, int rssi);

        void onStrengthFailure(String message);
    }

    public static class Builder {

        private Context mContext;
        private int mWifiStrengthLevelsCount;
        private int mWifiUpdatePeriod;
        private WifiSignalMeasurer.WifiStrengthListener mStrengthListener;

        public Builder(Context mContext) {
            this.mContext = mContext;
        }

        public Builder setContext(Context mContext) {
            this.mContext = mContext;
            return this;
        }

        public Builder setWifiStrengthLevelsCount(@IntRange(from = MIN_LEVELS, to = MAX_LEVELS) int wifiStrengthLevelsCount) {
            mWifiStrengthLevelsCount = wifiStrengthLevelsCount;
            return this;
        }

        public Builder setWifiUpdatePeriod(@IntRange(from = MIN_UPDATE_TIME) int wifiUpdatePeriod) {
            this.mWifiUpdatePeriod = wifiUpdatePeriod;
            return this;
        }

        public Builder setStrengthListener(WifiSignalMeasurer.WifiStrengthListener mStrengthListener) {
            this.mStrengthListener = mStrengthListener;
            return this;
        }

        public WifiSignalMeasurer create() {
            return new WifiSignalMeasurer(mContext, mWifiStrengthLevelsCount, mWifiUpdatePeriod, mStrengthListener);
        }
    }

}