package com.example.apoc.SmsSender;

import android.app.Application;
import android.content.IntentFilter;
import android.net.nsd.NsdManager;
import android.os.Bundle;

public class SendSMSBroadcastApplication extends Application {
    public static String SMS_ACTION = "APOC.ACTION_SEND_SMS";

    @Override
    public void onCreate() {
        super.onCreate();

        IntentFilter filter = new IntentFilter();
        filter.addAction(SMS_ACTION);
        registerReceiver(new LocalSendSmsBroadcastReceiver(), filter);

    }
}
