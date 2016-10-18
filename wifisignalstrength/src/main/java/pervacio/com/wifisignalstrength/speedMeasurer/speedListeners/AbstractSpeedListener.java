package pervacio.com.wifisignalstrength.speedMeasurer.speedListeners;

import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;
import pervacio.com.wifisignalstrength.speedMeasurer.ISpeedListenerFinishCallback;
import pervacio.com.wifisignalstrength.speedMeasurer.SpeedListenerHandler;
import pervacio.com.wifisignalstrength.speedMeasurer.StopWaiter;
import pervacio.com.wifisignalstrength.utils.Constants;

import static pervacio.com.wifisignalstrength.utils.Constants.FINISH;

public abstract class AbstractSpeedListener implements
        ISpeedTestListener,
        StopWaiter.OnTimerStop {

    private static final String TAG = AbstractSpeedListener.class.getSimpleName();

    private List<Float> mList;
    private SpeedListenerHandler mHandler;
    private long realStartTime;
    private ISpeedListenerFinishCallback mOnFinish;
    private StopWaiter mStopWaiter;

    protected AbstractSpeedListener(SpeedListenerHandler handler, ISpeedListenerFinishCallback mOnFinish) {
        this.mHandler = handler;
        this.mOnFinish = mOnFinish;
        mList = new ArrayList<>();
        mStopWaiter = new StopWaiter(this);
    }

    @Override
    public void onDownloadFinished(SpeedTestReport report) {
    }

    @Override
    public void onDownloadProgress(float percent, SpeedTestReport report) {
    }

    @Override
    public void onDownloadError(SpeedTestError speedTestError, String errorMessage) {
    }

    @Override
    public void onUploadFinished(SpeedTestReport report) {
        Log.d(TAG, "onUploadFinished() called with: report = [" + report + "]");
    }

    @Override
    public void onUploadError(SpeedTestError speedTestError, String errorMessage) {
    }

    @Override
    public void onUploadProgress(float percent, SpeedTestReport report) {
    }

    @Override
    public void onInterruption() {
    }

    @Override
    public void onTimerStop() {
        Log.d(TAG, "onTimerStop onStop() called");
        onStop();
    }

    protected void onUpdate(SpeedTestReport report) {
        Log.d(TAG, "onUpdate() called with: report = [" + report.getTransferRateBit().floatValue() / 1024 / 1024 + "]" + report.getProgressPercent());
        if (mHandler == null) {
            return;
        }
        if (realStartTime == 0) {
            realStartTime = report.getReportTime();
            mHandler.publish(Constants.START);
            mStopWaiter.start();
        } else if (report.getProgressPercent() == 100) {
            Log.d(TAG, "report.getProgressPercent() == 100 onStop() called");
            onStop();
        } else {
            float rate = report.getTransferRateBit().floatValue() / 1024 / 1024;
            mStopWaiter.updateTimer();
            if (rate != 0) {
                mList.add(rate);
                int progress = (int) ((report.getReportTime() - realStartTime) / 100) + 1;
                mHandler.publish(Constants.PROGRESS, new PublishProgress(progress, rate));
            }
        }
    }

    protected void onStop() {
//        Log.d(TAG, "onStop() called");
        if (mHandler != null) {
            if (mList.size() != 0) {
                mHandler.publish(FINISH, mList.get(mList.size() - 1));
            } else {
                mHandler.publish(Constants.ERROR);
            }
        }
        if (mOnFinish != null) {
            mOnFinish.onSpeedListenerFinish(this);
        }
    }

    protected void publish(@Constants.LoadingStatus int loadingStatus, int value) {
        publish(loadingStatus, value, null);
    }

    protected void publish(@Constants.LoadingStatus int loadingStatus, int value, Object object) {
        Message message = new Message();
        message.arg1 = loadingStatus;
        message.arg2 = value;
        message.obj = object;
        mHandler.sendMessage(message);
    }

    public static class PublishProgress {

        public int progress;
        public float rate;

        public PublishProgress(int progress, float rate) {
            this.progress = progress;
            this.rate = rate;
        }
    }

}
