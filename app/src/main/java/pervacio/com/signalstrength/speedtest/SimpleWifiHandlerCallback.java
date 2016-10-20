package pervacio.com.signalstrength.speedtest;

import pervacio.com.wifisignalstrength.speedMeasurer.DefaultHandlerCallback;

public class SimpleWifiHandlerCallback extends DefaultHandlerCallback{

    public SimpleWifiHandlerCallback(ViewSet viewSet, String taskName) {
        super(viewSet, taskName, WIFI);
    }

}