package com.mtechno.freerechargeswipe.services;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.mtechno.freerechargeswipe.activities.HomeScreenActivity;
import com.mtechno.freerechargeswipe.utils.PhoneCallStateListner;


/**
 * Created by DELL on 2/1/2015.
 */


public class ScreenListener extends Service {


    ScreenOnOffReceiver screenOnOffReceiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ((KeyguardManager) getSystemService(KEYGUARD_SERVICE)).newKeyguardLock("IN").disableKeyguard();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        screenOnOffReceiver = new ScreenOnOffReceiver();
        registerReceiver(screenOnOffReceiver, intentFilter);

        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        unregisterReceiver(screenOnOffReceiver);
        super.onDestroy();
    }


    public class ScreenOnOffReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
          	PhoneCallStateListner pscl = new PhoneCallStateListner(context);
			TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
			tm.listen(pscl, PhoneStateListener.LISTEN_CALL_STATE);
            }
            if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
            }
        }
    }


}
