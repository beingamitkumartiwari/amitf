package com.mtechno.freerechargeswipe.gcm_work;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.mtechno.freerechargeswipe.activities.MainActivity;
import com.mtechno.freerechargeswipe.utils.GcmNotificationObserver;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ComponentName comp = new ComponentName(context.getPackageName(),
                GCMNotificationIntentService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
//        new MainActivity().onRecievedGcmNotification();
    }
}
