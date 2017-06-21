package com.mtechno.freerechargeswipe.utils;

import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.mtechno.freerechargeswipe.activities.HomeScreenActivity;

/**
 * Created by DEVEN SINGH on 5/7/2015.
 */
public class PhoneCallStateListner extends PhoneStateListener {

    Context context;
    public PhoneCallStateListner(Context context) {
        this.context=context;
    }
    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        switch(state){
            case TelephonyManager.CALL_STATE_RINGING:
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                Intent homeIntent = new Intent(context, HomeScreenActivity.class);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                context.startActivity(homeIntent);
                break;
        }
    }
}
