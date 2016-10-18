package pervacio.com.signalstrength.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import pervacio.com.signalstrength.R;
import pervacio.com.signalstrength.speedtest.DefaultHandlerCallback;
import pervacio.com.wifisignalstrength.WifiSignalMeasurer;
import pervacio.com.wifisignalstrength.speedMeasurer.ConnectionRateTester;
import pervacio.com.wifisignalstrength.speedMeasurer.Router;
import pervacio.com.wifisignalstrength.speedMeasurer.TaskAndHandlerWrapper;
import pervacio.com.wifisignalstrength.speedMeasurer.speedListeners.DownloadSpeedListener;
import pervacio.com.wifisignalstrength.speedMeasurer.speedListeners.UploadSpeedListener;

import static pervacio.com.wifisignalstrength.utils.Constants.FILE_SIZE;
import static pervacio.com.wifisignalstrength.utils.Constants.SPEED_TEST_MAX_DURATION;
import static pervacio.com.wifisignalstrength.utils.Constants.SPEED_TEST_REPORT_INTERVAL;
import static pervacio.com.wifisignalstrength.utils.Constants.SPEED_TEST_SERVER_HOST;
import static pervacio.com.wifisignalstrength.utils.Constants.SPEED_TEST_SERVER_PORT;
import static pervacio.com.wifisignalstrength.utils.Constants.SPEED_TEST_SERVER_URI_DL;

public class MainContentFragment extends Fragment implements
        Router.LastListenerFinished,
        View.OnClickListener,
        WifiSignalMeasurer.WifiStrengthListener {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private ProgressBar mWifiDownloadSpeedProgress;
    private ProgressBar mWifiUploadSpeedProgress;
    private TextView downloadRate;
    private TextView uploadRate;
    private ImageView restartDownload;
    private ImageView restartUpload;
    private Button mWifiStrengthTimer;
    private Button mWifiStrengthOnRequest;

    private WifiSignalMeasurer mSignalMeasurer;

    private Handler.Callback mDownloadCallback;
    private Handler.Callback mUploadCallback;

    private ConnectionRateTester.WorkerTask mDownLoadTask = (speedTestSocket, handler, onFinish) -> {
        speedTestSocket.addSpeedTestListener(new DownloadSpeedListener(handler, onFinish));
        speedTestSocket.startFixedDownload(
                SPEED_TEST_SERVER_HOST,
                SPEED_TEST_SERVER_PORT,
                SPEED_TEST_SERVER_URI_DL,
                SPEED_TEST_MAX_DURATION,
                SPEED_TEST_REPORT_INTERVAL);
    };
    private ConnectionRateTester.WorkerTask mUploadTask = (speedTestSocket, handler, onFinish) -> {
        speedTestSocket.addSpeedTestListener(new UploadSpeedListener(handler, onFinish));
        speedTestSocket.startFixedUpload(
                SPEED_TEST_SERVER_HOST,
                SPEED_TEST_SERVER_PORT,
                SPEED_TEST_SERVER_URI_DL,
                FILE_SIZE,
                SPEED_TEST_MAX_DURATION,
                SPEED_TEST_REPORT_INTERVAL);
    };

    public MainContentFragment() {
    }

    public static MainContentFragment newInstance(int sectionNumber) {
        MainContentFragment fragment = new MainContentFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_pager2, container, false);
        mWifiDownloadSpeedProgress = (ProgressBar) rootView.findViewById(R.id.wifiDownloadSpeedProgress);
        mWifiUploadSpeedProgress = (ProgressBar) rootView.findViewById(R.id.wifiUploadSpeedProgress);
        downloadRate = (TextView) rootView.findViewById(R.id.download_rate);
        uploadRate = (TextView) rootView.findViewById(R.id.upload_rate);
        restartDownload = (ImageView) rootView.findViewById(R.id.restart_download);
        restartUpload = (ImageView) rootView.findViewById(R.id.restart_upload);
        mWifiStrengthTimer = (Button) rootView.findViewById(R.id.signal_strength_timer);
        mWifiStrengthOnRequest = (Button) rootView.findViewById(R.id.signal_strength_on_request);
        restartDownload.setOnClickListener(this);
        restartUpload.setOnClickListener(this);
        mWifiStrengthOnRequest.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDownloadCallback = new DefaultHandlerCallback(new DefaultHandlerCallback.ViewSet(mWifiDownloadSpeedProgress, downloadRate, restartDownload), "Download");
        mUploadCallback = new DefaultHandlerCallback(new DefaultHandlerCallback.ViewSet(mWifiUploadSpeedProgress, uploadRate, restartUpload), "Update");
        ArrayList<TaskAndHandlerWrapper> listenerAndHandlers = new ArrayList<>();
        listenerAndHandlers.add(new TaskAndHandlerWrapper(mDownLoadTask, mDownloadCallback));
        listenerAndHandlers.add(new TaskAndHandlerWrapper(mUploadTask, mUploadCallback));
        ConnectionRateTester rateTester = new ConnectionRateTester(listenerAndHandlers, this);
        rateTester.start();
        mSignalMeasurer = new WifiSignalMeasurer.Builder(getActivity())
                .setWifiStrengthLevelsCount(5)
                .setWifiUpdatePeriod(3000)
                .setStrengthListener(this)
                .create();
        mSignalMeasurer.startListenWifiStrength();
    }

    @Override
    public void onLastTaskCompleted() {
        Log.d("onLastTaskCompleted", "onLastTaskCompleted() called");
        restartDownload.setEnabled(true);
        restartUpload.setEnabled(true);
        restartDownload.setClickable(true);
        restartUpload.setClickable(true);

        restartDownload.setImageResource(R.drawable.ic_replay_accent_48dp);
        restartUpload.setImageResource(R.drawable.ic_replay_accent_48dp);
    }

    @Override
    public void onStrengthUpdate(int level, int rssi) {
        if(getActivity() != null && isAdded()){
            mWifiStrengthTimer.setText(getString(R.string.wifi_signal_strength, level, rssi));
        }
    }

    @Override
    public void onStrengthFailure(String message) {
        if(getActivity() != null && isAdded()){
            mWifiStrengthTimer.setText(message);
        }
    }

    @Override
    public void onClick(View view) {
        Log.d("onClick", "onClick() called " + restartDownload.getVisibility() + " " + restartDownload.isClickable() +
                " " + restartDownload.isEnabled() + "\n" +
                " ==  " + (view.getId() == R.id.restart_download));
        switch (view.getId()) {
            case R.id.restart_download:
                restartDownload.setEnabled(false);
                restartUpload.setEnabled(false);
                restartDownload.setClickable(false);
                restartUpload.setClickable(false);
                new ConnectionRateTester(Collections.singletonList(new TaskAndHandlerWrapper(mDownLoadTask, mDownloadCallback)), this).start();
                break;
            case R.id.restart_upload:
                Log.d("onClick", "onClick() called with: restart_upload");
                restartDownload.setEnabled(false);
                restartUpload.setEnabled(false);
                restartDownload.setClickable(false);
                restartUpload.setClickable(false);
                new ConnectionRateTester(Collections.singletonList(new TaskAndHandlerWrapper(mUploadTask, mUploadCallback)), this).start();
                break;
            case R.id.signal_strength_on_request:
                final int wifiStrengthLevel = mSignalMeasurer.getWifiStrengthLevel();
                final int wifiStrengthRssi = mSignalMeasurer.getWifiStrengthRssi();
                if (wifiStrengthLevel != 0){
                    mWifiStrengthOnRequest.setText(getString(R.string.wifi_signal_strength, wifiStrengthLevel, wifiStrengthRssi));
                }
                else {
                    mWifiStrengthOnRequest.setText(R.string.wifi_not_connected);
                }
                break;
        }
    }

}