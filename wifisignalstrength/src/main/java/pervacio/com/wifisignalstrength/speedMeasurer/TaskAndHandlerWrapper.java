package pervacio.com.wifisignalstrength.speedMeasurer;

import android.os.Handler;

/**
 * Class contains @link{{@link ConnectionRateTester.WorkerTask}} to determine connection rate test action
 * and @link{{@link android.os.Handler.Callback}} to determine result callback
 */
public class TaskAndHandlerWrapper {

    public ConnectionRateTester.WorkerTask mWorkerTask;
    public Handler.Callback mCallback;

    public TaskAndHandlerWrapper(ConnectionRateTester.WorkerTask workerTask, Handler.Callback callback) {
        this.mWorkerTask = workerTask;
        this.mCallback = callback;
    }

}
