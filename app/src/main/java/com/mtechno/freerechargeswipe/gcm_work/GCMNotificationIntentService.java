package com.mtechno.freerechargeswipe.gcm_work;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mtechno.freerechargeswipe.R;
import com.mtechno.freerechargeswipe.activities.MainActivity;
import com.mtechno.freerechargeswipe.utils.FrsDatabase;


public class GCMNotificationIntentService extends IntentService {
    // Sets an ID for the notification, so it can be updated
    public static final int notifyID = 9001;
    private FrsDatabase frsDatabase;

    public GCMNotificationIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        frsDatabase=new FrsDatabase(GCMNotificationIntentService.this);
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
                    .equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
                    .equals(messageType)) {
                sendNotification("Deleted messages on server: "
                        + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
                    .equals(messageType)) {
                sendNotification(extras.getString("message"));
                if(frsDatabase.getMessageCount()<5){
                    frsDatabase.insertMessage(extras.getString("message"),System.currentTimeMillis(),"unread");
                }else{
                    frsDatabase.updateMessage(extras.getString("message"),System.currentTimeMillis(),"unread");
                }
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String msg) {
        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.putExtra("msg", msg);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
                resultIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mNotifyBuilder;
        NotificationManager mNotificationManager;

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mNotifyBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle("Free Recharge Swipe")
                    .setContentText(msg)
                    .setSmallIcon(R.mipmap.ic_launcher);
        } else {
            mNotifyBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle("Free Recharge Swipe")
                    .setContentText(msg)
                    .setSmallIcon(R.drawable.notification_icon)
                    .setColor(R.color.my_accent_color);
        }
//        mNotifyBuilder = new NotificationCompat.Builder(this)
//                .setContentTitle("Free Recharge Swipe")
//                .setContentText(msg)
//
//                .setSmallIcon(R.mipmap.ic_launcher);
        // Set pending intent
        mNotifyBuilder.setContentIntent(resultPendingIntent);

        // Set Vibrate, Sound and Light
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;

        mNotifyBuilder.setDefaults(defaults);
        // Set the content for Notification
        mNotifyBuilder.setContentText(msg);
        // Set autocancel
        mNotifyBuilder.setAutoCancel(true);
        // Post a notification
        mNotificationManager.notify(notifyID, mNotifyBuilder.build());
    }
}
