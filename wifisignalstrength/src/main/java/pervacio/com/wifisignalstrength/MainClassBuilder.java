package pervacio.com.wifisignalstrength;

import android.content.Context;

public class MainClassBuilder {
    private Context mContext;
    private int wifiStrengthLevelsCount;
    private int wifiUpdatePeriod;
    private SignalMeasurer.WifiStrengthListener mStrengthListener;

    public MainClassBuilder setmContext(Context mContext) {
        this.mContext = mContext;
        return this;
    }

    public MainClassBuilder setWifiStrengthLevelsCount(int wifiStrengthLevelsCount) {
        this.wifiStrengthLevelsCount = wifiStrengthLevelsCount;
        return this;
    }

    public MainClassBuilder setWifiUpdatePeriod(int wifiUpdatePeriod) {
        this.wifiUpdatePeriod = wifiUpdatePeriod;
        return this;
    }

    public MainClassBuilder setmStrengthListener(SignalMeasurer.WifiStrengthListener mStrengthListener) {
        this.mStrengthListener = mStrengthListener;
        return this;
    }

    public SignalMeasurer createMainClass() {
        return new SignalMeasurer(mContext, wifiStrengthLevelsCount, wifiUpdatePeriod, mStrengthListener);
    }
}