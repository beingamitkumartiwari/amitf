package com.mtechno.freerechargeswipe.native_app_work;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mtechno.freerechargeswipe.extras.Constants;
import com.mtechno.freerechargeswipe.utils.SharedPrefs;
import com.mtechno.freerechargeswipe.volley_works.AppController;
import com.mtechno.freerechargeswipe.volley_works.CustomJsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DEVEN SINGH on 8/12/2015.
 */
public class AppCheckingService extends IntentService {

    private SharedPrefs myPrefs;
    String TAG = "AppCheckingService";

    public AppCheckingService(){
        super("AppCheckingService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        myPrefs = new SharedPrefs(getApplicationContext());
        String packageName = intent.getStringExtra("packageName");

        if (myPrefs.getCampaignPackagename().contains(packageName)) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("deviceId", Constants.getImeiNum(getApplicationContext()));
            params.put("uniqueId", myPrefs.getUserKey());
//            params.put("uniqueId", "35385337");
            params.put("Payout",myPrefs.getCampaignPayout().replaceAll("\\D+",""));
            params.put("packageName", packageName);
            params.put("appName","frsCampaigntype"+myPrefs.getCampaignType());
            AppController.getInstance().addToRequestQueue(requestWithPackageName("http://www.mpaisa.info/MtechAppsPromotion.asmx/installAppsFrsCampaign", params), TAG);
        }
        String[] packageNamePayout=myPrefs.getExpletusOfferNativePackage().split(",");
        String payout=null;
        String appName=null;
        for(String appPackage:packageNamePayout){
            if(appPackage.contains(packageName)){
                payout=appPackage.substring(appPackage.indexOf("payout") + 6, appPackage.indexOf("appName"));
                appName=appPackage.substring(appPackage.indexOf("appName")+7,appPackage.length());
            }
        }
        System.out.println("kamalverma000 " + myPrefs.getExpletusOfferNativePackage());
        System.out.println("kamalverma111 " + packageName);
        if(myPrefs.getExpletusOfferNativePackage().contains(packageName)){

            Map<String, String> params = new HashMap<String, String>();
            params.put("deviceId", Constants.getImeiNum(getApplicationContext()));
//            params.put("uniqueId", myPrefs.getUserKey());
            params.put("uniqueId", "35385337");
            params.put("Payout",payout);
            params.put("packageName", packageName);
            params.put("appName", "frsNative" + appName);
            System.out.println("kamalverma222 " + packageName);
            System.out.println("kamalverma333 "+"frsNative"+appName);
            AppController.getInstance().addToRequestQueue(requestWithPackageName("http://www.mpaisa.info/MtechAppsPromotion.asmx/installAppsFrsCampaign", params), TAG);

        }
    }

    private CustomJsonObjectRequest requestWithPackageName(String url, Map<String, String> params) {
        CustomJsonObjectRequest customJsonObjectRequest = new CustomJsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("response", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        customJsonObjectRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 4,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        return customJsonObjectRequest;
    }
}
