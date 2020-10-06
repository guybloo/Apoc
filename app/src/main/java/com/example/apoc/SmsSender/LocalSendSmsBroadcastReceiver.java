package com.example.apoc.SmsSender;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class LocalSendSmsBroadcastReceiver extends BroadcastReceiver {
    public static final String PHONE = "PHONE";
    public static final String CONTENT = "CONTENT";
    private static final String CHANNEL_ID = "channel_sendsms_02";
    private static final int NOTIFY_ID = 10;

    private String phoneNumber;
    private String smsMessage;


    @Override
    public void onReceive(Context context, Intent intent) {
        createNotificationChannel(context);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            Log.d("Error", "Can't get sms permissions");
            return;
        }
        phoneNumber = intent.getStringExtra(PHONE);
        smsMessage = intent.getStringExtra(CONTENT);
        if (phoneNumber == null || phoneNumber.equals("") || smsMessage == null || smsMessage.equals("")) {
            Log.d("Error", "Can't get phone or content");
        }
//        try {
        // Get the default instance of the SmsManager
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber,
                null,
                smsMessage,
                null,
                null);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setContentTitle("Sending SMS")
                .setContentText(String.format("Sending SMS to %s: %s", phoneNumber, smsMessage))
                .setContentInfo("Information");
        notificationManager.notify(NOTIFY_ID, notificationBuilder.build());
    }

    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = CHANNEL_ID;
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
