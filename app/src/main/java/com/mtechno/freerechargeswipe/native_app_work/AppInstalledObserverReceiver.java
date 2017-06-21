package com.mtechno.freerechargeswipe.native_app_work;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class AppInstalledObserverReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
            try {
                String packageName = intent.getData().toString().replace("package:", "");
                System.out.println("kamalvermaAppinstallreceiver  "+packageName);
                Intent intent1 = new Intent(context, AppCheckingService.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.putExtra("packageName", packageName);
                context.startService(intent1);
            } catch (Exception e) {
            }
        }
    }
}