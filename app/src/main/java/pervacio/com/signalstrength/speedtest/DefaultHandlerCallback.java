package pervacio.com.signalstrength.speedtest;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import pervacio.com.signalstrength.R;
import pervacio.com.wifisignalstrength.speedMeasurer.speedListeners.AbstractSpeedListener;

import static pervacio.com.wifisignalstrength.utils.Constants.ERROR;
import static pervacio.com.wifisignalstrength.utils.Constants.FINISH;
import static pervacio.com.wifisignalstrength.utils.Constants.PROGRESS;
import static pervacio.com.wifisignalstrength.utils.Constants.START;

public class DefaultHandlerCallback implements Handler.Callback {

    private ProgressBar mProgressBar;
    private TextView mRateText;
    private ImageView mRestartButton;

    private String mTaskName;
    private Context mContext;

    public DefaultHandlerCallback(ViewSet viewSet, String taskName) {
        mContext = viewSet.mProgressBar.getContext();
        mProgressBar = viewSet.mProgressBar;
        mRateText = viewSet.mRateText;
        mRestartButton = viewSet.mRestartButton;
        mTaskName = taskName;
    }

    @Override
    public boolean handleMessage(Message message) {
        switch (message.arg1) {
            case START:
                mProgressBar.setVisibility(View.VISIBLE);
                mRestartButton.setVisibility(View.GONE);
                mRateText.setText(mContext.getString(R.string.measurement_started, mTaskName));
                break;
            case PROGRESS:
                AbstractSpeedListener.PublishProgress publishProgress = (AbstractSpeedListener.PublishProgress) message.obj;
                mRateText.setText(mContext.getString(R.string.rate_message, mTaskName, publishProgress.rate));
                break;
            case FINISH:
                final Float rate = (Float) message.obj;
                mRateText.setText(mContext.getString(R.string.rate_message, mTaskName, rate));
                mProgressBar.setVisibility(View.INVISIBLE);
                mProgressBar.setIndeterminate(false);
                mRestartButton.setVisibility(View.VISIBLE);
                break;
            case ERROR:
                mRateText.setText("Cannot connect to server. Pleace, retry");
                mProgressBar.setVisibility(View.INVISIBLE);
                mProgressBar.setIndeterminate(false);
                mRestartButton.setVisibility(View.VISIBLE);
                break;
        }
        return true;
    }

    public static class ViewSet {

        private ProgressBar mProgressBar;
        private TextView mRateText;
        private ImageView mRestartButton;

        public ViewSet(@NonNull ProgressBar progressBar, @NonNull TextView rateText, @NonNull ImageView restartButton) {
            this.mProgressBar = progressBar;
            this.mRateText = rateText;
            this.mRestartButton = restartButton;
        }
    }

}