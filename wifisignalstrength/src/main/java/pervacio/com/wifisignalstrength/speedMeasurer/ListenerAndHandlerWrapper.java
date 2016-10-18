package pervacio.com.wifisignalstrength.speedMeasurer;

import android.os.Handler;

public class ListenerAndHandlerWrapper {

    public WorkerThread.WorkerTask mWorkerTask;
    public Handler.Callback mCallback;

    public ListenerAndHandlerWrapper(WorkerThread.WorkerTask workerTask, Handler.Callback callback) {
        this.mWorkerTask = workerTask;
        this.mCallback = callback;
    }

}
