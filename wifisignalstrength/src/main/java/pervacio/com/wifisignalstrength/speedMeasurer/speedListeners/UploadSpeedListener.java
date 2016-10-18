package pervacio.com.wifisignalstrength.speedMeasurer.speedListeners;

import android.util.Log;

import fr.bmartel.speedtest.SpeedTestReport;
import pervacio.com.wifisignalstrength.speedMeasurer.ISpeedListenerFinishCallback;
import pervacio.com.wifisignalstrength.speedMeasurer.SpeedListenerHandler;

public class UploadSpeedListener extends AbstractSpeedListener {

    protected static final String TAG = "[" + UploadSpeedListener.class.getSimpleName() + "]";

    public UploadSpeedListener(SpeedListenerHandler handler, ISpeedListenerFinishCallback mOnFinish) {
        super(handler, mOnFinish);
    }

    @Override
    public void onUploadProgress(float percent, SpeedTestReport report) {
        onUpdate(report);
    }

    @Override
    public void onUploadFinished(SpeedTestReport report) {
        Log.d(TAG, "onUploadFinished onStop() called");
        onStop();
    }



}
