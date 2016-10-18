package pervacio.com.wifisignalstrength.speedMeasurer;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.List;

import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.model.UploadStorageType;
import pervacio.com.wifisignalstrength.speedMeasurer.speedListeners.AbstractSpeedListener;

public class Router implements ISpeedListenerFinishCallback {

    private List<TaskAndHandlerWrapper> mListenerAndHandlers;
    private SpeedTestSocket mSpeedTestSocket;
    private LastListenerFinished mLastListenerFinished;
    private int mSerialNumber;

    public Router(List<TaskAndHandlerWrapper> listenerAndHandlers, LastListenerFinished lastListenerFinished) {
        mListenerAndHandlers = listenerAndHandlers;
        mSpeedTestSocket = new SpeedTestSocket();
        mSpeedTestSocket.setUploadStorageType(UploadStorageType.FILE_STORAGE);
        mLastListenerFinished = lastListenerFinished;
    }

    /**
     * Calls when task finishes
     *
     * @param speedListener callback listener
     */
    @Override
    public void onSpeedListenerFinish(AbstractSpeedListener speedListener) {
        Log.w("Router", "onSpeedListenerFinish");
        if (mListenerAndHandlers.size() == mSerialNumber) {
            if (mLastListenerFinished != null) {
                Log.d("Router", "onSpeedListenerFinish() called  mListenerAndHandlers.size() == mSerialNumber " + (mListenerAndHandlers.size() == mSerialNumber));
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        mLastListenerFinished.onLastTaskCompleted();
                    }
                });
            }
        } else {
            startTask(mSerialNumber++);
        }
    }

    public void route() {
        startTask(mSerialNumber++);
    }

    /**
     * Create handlers from callbacks and start tasks one by one
     *
     * @param serialNumber number of the task
     */
    private void startTask(int serialNumber) {
        if (mListenerAndHandlers.size() > serialNumber) {
            TaskAndHandlerWrapper listenerAndHandler = mListenerAndHandlers.get(serialNumber);

            ConnectionRateTester.WorkerTask mWorkerTask = listenerAndHandler.mWorkerTask;
            Handler.Callback mCallback = listenerAndHandler.mCallback;
            SpeedListenerHandler handler = new SpeedListenerHandler(Looper.getMainLooper(), mCallback);

            mWorkerTask.execute(mSpeedTestSocket, handler, this);
        }
    }

    public interface LastListenerFinished {
        void onLastTaskCompleted();
    }

}
