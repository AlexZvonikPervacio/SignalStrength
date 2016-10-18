package pervacio.com.wifisignalstrength.speedMeasurer;

import android.os.HandlerThread;

import java.util.List;

import fr.bmartel.speedtest.SpeedTestSocket;

public class WorkerThread extends HandlerThread {

    public static final String TAG = WorkerThread.class.getSimpleName();

    private List<ListenerAndHandlerWrapper> mListenerAndHandlers;
    private Router.LastListenerFinished mLastListenerFinished;

    public WorkerThread(List<ListenerAndHandlerWrapper> listenerAndHandlers, Router.LastListenerFinished lastListenerFinished) {
        super(TAG);
        mListenerAndHandlers = listenerAndHandlers;
        mLastListenerFinished = lastListenerFinished;
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        Router router = new Router(mListenerAndHandlers, mLastListenerFinished);
        router.route();
    }

    public interface WorkerTask {
        void execute(SpeedTestSocket speedTestSocket, SpeedListenerHandler handler, ISpeedListenerFinishCallback onFinish);
    }

}
