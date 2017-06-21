package com.mtechno.freerechargeswipe.extras;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.mtechno.freerechargeswipe.utils.SharedPrefs;

import java.util.Random;

/**
 * Created by DEVEN SINGH on 4/27/2015.
 */
public class Constants {


    public static final String GOOGLE_PROJ_ID = "1056009251932";

//    public static final class FRS_URLs {
//        public static final String BaseUrl = "http://fundoapp.com/userregistration.asmx/";
//        public static final String LOGIN = BaseUrl + "registration";
//        public static final String BALANCE = BaseUrl + "totalbalance";
//        public static final String OFFERS = BaseUrl + "offers";
//        public static final String PROFILE = BaseUrl + "getprofile";
//        public static final String UPDATE_PROFILE = BaseUrl + "updateprofile";
//        public static final String INSERT_MONEY = BaseUrl + "insertmoney";
//        public static final String REDEEM_MONEY = BaseUrl + "redeemmoney";
//        public static final String ACCOUNT_SUMMARY = BaseUrl + "accountsummary";
//        public static final String APP_SERVER_URL = "http://fundoapp.com/AndroidGCMPushNotification.asmx/registration";
//
//    }

    public static String getImeiNum(Context context) {
        String identifier = null;
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null)
            identifier = tm.getDeviceId();
        if (identifier == null || identifier.length() == 0)
            identifier = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return identifier;
    }

    public String getUserKey(Context context){
        String imei=Constants.getImeiNum(context);
        String key=imei.substring(0,3)+new SharedPrefs(context).getUserDev()+imei.substring(imei.length()-3);
        return key;
    }
    public static int getRandomnumForAd(int[] numbers){
        int rnd = new Random().nextInt(numbers.length);
        return numbers[rnd];
    }
}
