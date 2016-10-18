package pervacio.com.wifisignalstrength.speedMeasurer.speedListeners;

import android.util.Log;

import fr.bmartel.speedtest.SpeedTestReport;
import pervacio.com.wifisignalstrength.speedMeasurer.ISpeedListenerFinishCallback;
import pervacio.com.wifisignalstrength.speedMeasurer.SpeedListenerHandler;

public class DownloadSpeedListener extends AbstractSpeedListener {

    protected static final String TAG = "[" + DownloadSpeedListener.class.getSimpleName() + "]";

    public DownloadSpeedListener(SpeedListenerHandler handler, ISpeedListenerFinishCallback mOnFinish) {
        super(handler, mOnFinish);
    }

    @Override
    public void onDownloadProgress(float percent, SpeedTestReport report) {
        onUpdate(report);
    }

    @Override
    public void onDownloadFinished(SpeedTestReport report) {
        Log.d(TAG, "onDownloadFinished onStop() called");
        onStop();
    }

}
