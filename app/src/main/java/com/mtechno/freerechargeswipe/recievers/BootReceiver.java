package com.mtechno.freerechargeswipe.recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mtechno.freerechargeswipe.services.ScreenListener;
import com.mtechno.freerechargeswipe.utils.SharedPrefs;

/**
 * Created by DEVEN SINGH on 5/7/2015.
 */
public class BootReceiver extends BroadcastReceiver {

    SharedPrefs sharedPrefs;

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
             sharedPrefs=new SharedPrefs(context);
            if (sharedPrefs.isServiceRunning()) {
                Intent iService = new Intent(context, ScreenListener.class);
                context.startService(iService);
            }
        }

    }
}
