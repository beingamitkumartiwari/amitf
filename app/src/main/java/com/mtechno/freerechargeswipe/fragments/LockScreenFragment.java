package com.mtechno.freerechargeswipe.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AnalogClock;
import android.widget.DigitalClock;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.mtechno.freerechargeswipe.R;
import com.mtechno.freerechargeswipe.activities.MainActivity;
import com.mtechno.freerechargeswipe.extras.Constants;
import com.mtechno.freerechargeswipe.extras.DateAndTime;
import com.mtechno.freerechargeswipe.extras.Frs_Constants;
import com.mtechno.freerechargeswipe.glow_pad.GlowPadView;
import com.mtechno.freerechargeswipe.glow_pad.GlowPadView.OnTriggerListener;
import com.mtechno.freerechargeswipe.utils.SharedPrefs;
import com.mtechno.freerechargeswipe.volley_works.AppController;
import com.mtechno.freerechargeswipe.volley_works.CustomJsonObjectRequest;
import com.purplebrain.adbuddiz.sdk.AdBuddiz;
import com.purplebrain.adbuddiz.sdk.AdBuddizDelegate;
import com.purplebrain.adbuddiz.sdk.AdBuddizError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DEVEN SINGH on 5/1/2015.
 */
public class LockScreenFragment extends Fragment implements OnTriggerListener {

    String TAG_JSON = "requestAddEarnMoney";
//    private static final String AD_UNIT_ID = "ca-app-pub-1029146915153475/1465228147";
    private InterstitialAd interstitialAd;
    AdRequest adRequest;
    SharedPrefs sharedPrefs;
    int i = 0;
    TextView dateDays;
    Typeface typeface;
    DigitalClock digitalClock;
    AnalogClock analogClock;
    LinearLayout layoutMain;
    private GlowPadView mGlowPadView;
    boolean adLoaded;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_lockscreen, container, false);

        return rootView;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FullScreencall();
        sharedPrefs = new SharedPrefs(getActivity());
        initPallets(view);
        initAds();

    }

    private void initAds() {
        if (sharedPrefs.getLockScreenAd() == 1) {
            layoutMain.setBackgroundDrawable(getActivity().getResources().getDrawable(R.mipmap.bg1));
            interstitialAd = new InterstitialAd(getActivity());
            interstitialAd.setAdUnitId(sharedPrefs.getAdmobId());
            adRequest = new AdRequest.Builder().build();
            interstitialAd.loadAd(adRequest);
            addAdListner();
        } else if (sharedPrefs.getLockScreenAd() == 2) {
            layoutMain.setBackgroundDrawable(getActivity().getResources().getDrawable(R.mipmap.bg2));
            initAdBuddiz();
        } else if (sharedPrefs.getLockScreenAd() == 3) {
            layoutMain.setBackgroundDrawable(getActivity().getResources().getDrawable(R.mipmap.bg3));

        }
    }

    private void initPallets(final View rootView) {
        layoutMain = (LinearLayout) rootView.findViewById(R.id.mainBack);
        typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/indie_flower.ttf");
        dateDays = (TextView) rootView.findViewById(R.id.dateDays);
        dateDays.setText(new DateAndTime().DateNTime());
        dateDays.setTypeface(typeface);
        digitalClock = (DigitalClock) rootView.findViewById(R.id.digitalClock);
        digitalClock.setTypeface(typeface);
        analogClock = (AnalogClock) rootView.findViewById(R.id.analogClock);

        if (sharedPrefs.isAnologClock()) {
            digitalClock.setVisibility(View.GONE);
            analogClock.setVisibility(View.VISIBLE);
        } else {
            analogClock.setVisibility(View.GONE);
            digitalClock.setVisibility(View.VISIBLE);
        }
        mGlowPadView = (GlowPadView) rootView.findViewById(R.id.glow_pad_view);
        mGlowPadView.setOnTriggerListener(this);
        mGlowPadView.setPointsMultiplier(8);
    }
    private void initAdBuddiz() {
        AdBuddiz.setPublisherKey(sharedPrefs.getAdbuddizId());
        AdBuddiz.cacheAds(getActivity());
        AdBuddiz.setDelegate(new AdBuddizDelegate() {
            @Override
            public void didCacheAd() {
            }

            @Override
            public void didShowAd() {
            }

            @Override
            public void didFailToShowAd(AdBuddizError adBuddizError) {
            }

            @Override
            public void didClick() {
                Log.d("adClicked"," Ad clicked");
                if (getActivity() != null && !sharedPrefs.isAdClicked()) {
                    sharedPrefs.setAdClicked(true);
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("deviceid", Constants.getImeiNum(getActivity()));
                    params.put("uniqueid",sharedPrefs.getUserKey());
                    params.put("credit", "10");
                    params.put("fromwhere", "adclick");
                    AppController.getInstance().addToRequestQueue(requestToAddClickEarnMoney(Frs_Constants.INSERT_CREDIT, params), TAG_JSON);
                    sharedPrefs.setLockScreenAd(3);
                    getActivity().finish();
                }
            }

            @Override
            public void didHideAd() {
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });
    }
//    private void showAd() {
//        startAppAd.showAd(new AdDisplayListener() {
//            @Override
//            public void adHidden(Ad ad) {
//                Log.d("adHidden", " Ad hidden");
//                if (getActivity() != null) {
//                    getActivity().finish();
//                }
//            }
//
//            @Override
//            public void adDisplayed(Ad ad) {
//                Log.d("adDisplayed"," Ad Displayed");
//            }
//
//            @Override
//            public void adClicked(Ad ad) {
//                Log.d("adClicked"," Ad clicked");
//                if (getActivity() != null && !sharedPrefs.isAdClicked()) {
//                    sharedPrefs.setAdClicked(true);
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("deviceid", Constants.getImeiNum(getActivity()));
//                    params.put("uniqueid",sharedPrefs.getUserKey());
//                    params.put("credit", "10");
//                    params.put("fromwhere", "adclick");
//                    AppController.getInstance().addToRequestQueue(requestToAddClickEarnMoney(Frs_Constants.INSERT_CREDIT, params), TAG_JSON);
//                    sharedPrefs.setLockScreenAd(3);
//                    getActivity().finish();
//                }
//            }
//        });
//    }
//
//    private void loadAd() {
//        startAppAd.loadAd(new AdEventListener() {
//            @Override
//            public void onReceiveAd(Ad ad) {
//                adLoaded = true;
//            }
//
//            @Override
//            public void onFailedToReceiveAd(Ad ad) {
//
//            }
//        });
//    }

    private void addAdListner() {
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                if (getActivity() != null && !sharedPrefs.isAdClicked()) {
                    sharedPrefs.setAdClicked(true);
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("deviceid", Constants.getImeiNum(getActivity()));
                    params.put("uniqueid",sharedPrefs.getUserKey());
                    params.put("credit", "15");
                    params.put("fromwhere", "adclick");
                    AppController.getInstance().addToRequestQueue(requestToAddClickEarnMoney(Frs_Constants.INSERT_CREDIT, params), TAG_JSON);
//                    sharedPrefs.setLockScreenAd(2);
                    sharedPrefs.setLockScreenAd(1);
                }
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });
    }


    private CustomJsonObjectRequest requestToAddClickEarnMoney(String url, Map<String, String> params) {
        return new CustomJsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("earn monew added");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), "Error! try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String getImeiNum() {
        String identifier = null;
        TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null)
            identifier = tm.getDeviceId();
        if (identifier == null || identifier.length() == 0)
            identifier = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);

        return identifier;
    }


    public void FullScreencall() {
        if (Build.VERSION.SDK_INT < 19) {
            View v = getActivity().getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else {
            View decorView = getActivity().getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    @Override
    public void onGrabbed(View v, int handle) {

    }

    @Override
    public void onReleased(View v, int handle) {
        mGlowPadView.ping();
    }

    @Override
    public void onTrigger(View v, int target) {
        final int resId = mGlowPadView.getResourceIdForTarget(target);
        switch (resId) {
            case R.drawable.ic_lockscreen_unlock:
                getActivity().finish();
                break;

            case R.drawable.ic_lockscreen_frs:
                frsEarn();
                break;
            default:
                // Code should never reach here.
        }
    }

    @Override
    public void onGrabbedStateChange(View v, int handle) {

    }

    @Override
    public void onFinishFinalAnimation() {

    }

    private void frsEarn() {
        sharedPrefs.setAdClicked(false);
        if (i == 0) {
            i++;
            if (System.currentTimeMillis() > sharedPrefs.getTime()) {
                if (sharedPrefs.getLockScreenAd() == 1) {
                    if (interstitialAd.isLoaded()) {
                        interstitialAd.show();
                        sharedPrefs.setTime(System.currentTimeMillis() + 1000 * 60 * 5);
                    } else {
                        Toast.makeText(getActivity(), "There is some problem. Offers are not loaded.", Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }
                } else if (sharedPrefs.getLockScreenAd() == 2) {
//                    if (adLoaded) {
//                        System.out.println("startApp readyToShow");
//                        showAd();
//                        sharedPrefs.setTime(System.currentTimeMillis() + 1000 * 60 * 5);
//                    } else {
//                        sharedPrefs.setLockScreenAd(3);
//                        Toast.makeText(getActivity(), "There is some problem. Offers are not loaded.", Toast.LENGTH_SHORT).show();
//                        getActivity().finish();
//                    }
                    if(AdBuddiz.isReadyToShowAd(getActivity())){
                        AdBuddiz.showAd(getActivity());
                        sharedPrefs.setTime(System.currentTimeMillis() + 1000 * 60 * 5);
                    }else{
                        sharedPrefs.setLockScreenAd(3);
                        Toast.makeText(getActivity(), "There is some problem. Offers are not loaded.", Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }
                } else if (sharedPrefs.getLockScreenAd() == 3) {
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    sharedPrefs.setLockScreenAd(1);
                    sharedPrefs.setTime(System.currentTimeMillis() + 1000 * 60 * 5);
                    getActivity().finish();
                }
            } else {
                Toast.makeText(getActivity(), "Wait 5 minutes for next offers.", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        }
    }
}
