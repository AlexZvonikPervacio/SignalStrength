package pervacio.com.wifisignalstrength.speedMeasurer;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import pervacio.com.wifisignalstrength.utils.Constants;

public class SpeedListenerHandler extends Handler {

    public SpeedListenerHandler(Looper looper, Callback callback) {
        super(looper, callback);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
    }


    public void publish(@Constants.LoadingStatus int loadingStatus) {
        publish(loadingStatus, null);
    }

    public void publish(@Constants.LoadingStatus int loadingStatus, Object object) {
        Message message = new Message();
        message.arg1 = loadingStatus;
        message.obj = object;
        sendMessage(message);
    }

}