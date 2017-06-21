package com.mtechno.freerechargeswipe.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.mtechno.freerechargeswipe.R;
import com.mtechno.freerechargeswipe.activities.TransparentActivity;
import com.mtechno.freerechargeswipe.adapters.ExpletusAdapter;
import com.mtechno.freerechargeswipe.adapters.MinimobAdAdapter;
import com.mtechno.freerechargeswipe.adapters.OnItemButtonClickListner;
import com.mtechno.freerechargeswipe.extras.Constants;
import com.mtechno.freerechargeswipe.extras.Frs_Constants;
import com.mtechno.freerechargeswipe.minimobworks.OnListItemClickListner;
import com.mtechno.freerechargeswipe.minimobworks.ScrollingListView;
import com.mtechno.freerechargeswipe.pojos.PojoExpletus;
import com.mtechno.freerechargeswipe.pojos.Pojo_availableoffer;
import com.mtechno.freerechargeswipe.utils.CustomProgressDialog;
import com.mtechno.freerechargeswipe.utils.SharedPrefs;
import com.mtechno.freerechargeswipe.volley_works.AppController;
import com.mtechno.freerechargeswipe.volley_works.ConnectionCheck;
import com.mtechno.freerechargeswipe.volley_works.CustomJsonArrayRequest;
import com.mtechno.freerechargeswipe.volley_works.CustomJsonObjectRequest;
import com.nativex.monetization.MonetizationManager;
import com.nativex.monetization.business.reward.Reward;
import com.nativex.monetization.communication.RedeemRewardData;
import com.nativex.monetization.enums.AdEvent;
import com.nativex.monetization.enums.NativeXAdPlacement;
import com.nativex.monetization.listeners.OnAdEventV2;
import com.nativex.monetization.listeners.RewardListener;
import com.nativex.monetization.listeners.SessionListener;
import com.nativex.monetization.mraid.AdInfo;
import com.purplebrain.adbuddiz.sdk.AdBuddiz;
import com.purplebrain.adbuddiz.sdk.AdBuddizDelegate;
import com.purplebrain.adbuddiz.sdk.AdBuddizError;
import com.purplebrain.adbuddiz.sdk.AdBuddizRewardedVideoDelegate;
import com.purplebrain.adbuddiz.sdk.AdBuddizRewardedVideoError;
import com.startapp.android.publish.Ad;
import com.startapp.android.publish.AdDisplayListener;
import com.startapp.android.publish.AdEventListener;
import com.startapp.android.publish.StartAppAd;
import com.supersonic.adapters.supersonicads.SupersonicConfig;
import com.supersonic.mediationsdk.logger.SupersonicError;
import com.supersonic.mediationsdk.sdk.OfferwallListener;
import com.supersonic.mediationsdk.sdk.Supersonic;
import com.supersonic.mediationsdk.sdk.SupersonicFactory;
import com.woobi.Woobi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DEVEN SINGH on 4/21/2015.
 */
public class OffersFragment extends Fragment implements View.OnClickListener, OfferwallListener, OnListItemClickListner, OnItemButtonClickListner {

    CustomProgressDialog customProgressDialog;
    SharedPrefs sharedPrefs;
    String TAG_JSON1 = "requestAddMoney";
    String TAG_JSON = "requestOffer";
    String TAG_JSON_MINIMOB = "requestOffersMinimob";
    String URL_MINIMOBAVAILABLE="http://fundoapp.com/rcregistration.asmx/minimobavailableoffersdetails";
    String URL_INSERTCLICKIDTO_DATABASE="http://fundoapp.com/rcregistration.asmx/insertclicks";
    String URL_INSERTCLICKEXPLETUS="http://mpaisa.info/MtechAppsPromotion.asmx/insertclicksAppCompaign?";

    private InterstitialAd interstitialAd1;
    private InterstitialAd interstitialAd2;
    AdRequest adRequest1;
    AdRequest adRequest2;

    CardView cvWoobi;
//    CardView cvExpletus;
    CardView cvSupersonic;
    CardView cvAdbuddizFullScreen;
    CardView cvAdbuddizVideo;
    CardView cvAdmobFullScreen;
    CardView cvAdmobVideo;
    CardView cvStartapp;
    CardView cvNativex;
    CardView cvMinimob;
//    private LinearLayout llExpletus;
    LinearLayout llWoobiOfferwall;
    LinearLayout llSuperSonic;
    LinearLayout llAdBuddiz;
    LinearLayout llAdBuddizVideoAd;
    LinearLayout llAdmobFullScreen;
    LinearLayout llAdMobVideo;
    LinearLayout llStartapp;
    LinearLayout llNativex;
    LinearLayout llMinimob;
    private Supersonic mMediationAgent;
    private StartAppAd startAppAd;
    View parentView;
    Handler handler;
    private Handler handlerNativex = new Handler();
    int numbers[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    //    Dialog dialog;
    FragmentManager fm;
    Handler handlerCampaign;
    boolean appReady = false;
    private ProgressBar progressBarMinimob;
    private boolean showlistminimob = false;
    private boolean offersminimob = false;
    private ImageView iv_arrowminimob;
//    private ImageView iv_arrowexpletus;
    private ListView listViewMinimob;
    private ArrayList<Pojo_availableoffer> listOfOffersMinimob;
    private MinimobAdAdapter adapterMinimob;
    Dialog dialog;


//    private boolean isExpletusOfferListShowing = false;
    private ArrayList<PojoExpletus> listOfOffersExpletus;
    String TAG_EXPLETUS = "expletusapps";
    private ListView expletusListview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_offers, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initAds();
        initAdBuddizfullpage();
        fm = getActivity().getSupportFragmentManager();


    }


    @Override
    public void onResume() {
        super.onResume();
        requestForOffers();
        mMediationAgent.setOfferwallListener(this);
        mMediationAgent.getOfferwallCredits();
        if (mMediationAgent != null) {
            mMediationAgent.onResume(getActivity());
        }
        nativex();

    }

    private void nativex() {
        MonetizationManager.createSession(getActivity(), sharedPrefs.getNativexId(), sessionListener);
        MonetizationManager.setRewardListener(rewardListener);
    }

    private SessionListener sessionListener = new SessionListener() {
        @Override
        public void createSessionCompleted(boolean success, boolean isOfferWallEnabled, String sessionId) {
            if (getActivity() == null)
                return;
            if (success) {
                appReady = true;
                MonetizationManager.fetchAd(getActivity(), NativeXAdPlacement.Game_Launch, onAdEventListener);
            } else {
                appReady = false;
            }
        }
    };
    Runnable mNativeXLoadSession = new Runnable() {
        @Override
        public void run() {
            if (getActivity() == null) {
                return;
            }
            if (appReady) {
                customProgressDialog.dismiss();
                MonetizationManager.showReadyAd(getActivity(), NativeXAdPlacement.Game_Launch, onAdEventListener);
                handlerNativex.removeCallbacks(mNativeXLoadSession);
            } else {
                handlerNativex.postDelayed(mNativeXLoadSession, 500);
            }
        }
    };
    private RewardListener rewardListener = new RewardListener() {
        @Override
        public void onRedeem(RedeemRewardData data) {
            int totalRewardAmount = 0;
            for (Reward reward : data.getRewards()) {
                totalRewardAmount += reward.getAmount();
            }
            requestForAddingMoney(String.valueOf(totalRewardAmount));
        }
    };
    private OnAdEventV2 onAdEventListener = new OnAdEventV2() {
        @Override
        public void onEvent(AdEvent event, AdInfo adInfo, String message) {
            switch (event) {
                case FETCHED:
                    break;
                case NO_AD:
                    break;
                case DISMISSED:
                    MonetizationManager.fetchAd(getActivity(), adInfo.getPlacement(), onAdEventListener);
                    break;
                case BEFORE_DISPLAY:
                    break;
                case USER_TOUCH:
                    break;
                case DOWNLOADING:
                    break;
                case EXPANDED:
                    break;
                case COLLAPSED:
                    break;
                case VIDEO_COMPLETED:
                    break;
                case ERROR:
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        if (mMediationAgent != null) {
            mMediationAgent.onPause(getActivity());
        }
        if (handlerCampaign != null) {
            handlerCampaign.removeCallbacks(campaignRunnable);
            handlerCampaign = null;
        }
        if (mNativeXLoadSession != null)
            handlerNativex.removeCallbacks(mNativeXLoadSession);
    }

    private void initViews(View rootView) {
        parentView = rootView;
        customProgressDialog = new CustomProgressDialog(getActivity(), R.mipmap.loading);
        sharedPrefs = new SharedPrefs(getActivity());
        listOfOffersMinimob = new ArrayList<Pojo_availableoffer>();
        listOfOffersExpletus = new ArrayList<PojoExpletus>();
        expletusListview = (ListView) rootView.findViewById(R.id.listviewExpletus);
        progressBarMinimob= (ProgressBar) rootView.findViewById(R.id.progressBarOfferminimob);
        cvWoobi = (CardView) rootView.findViewById(R.id.cardWoobiOfferwal);
//        cvExpletus = (CardView) rootView.findViewById(R.id.cardViewExpletus);
        cvSupersonic = (CardView) rootView.findViewById(R.id.cardViewSuperSonic);
        cvAdbuddizFullScreen = (CardView) rootView.findViewById(R.id.cardViewAdBuddiz);
        cvAdbuddizVideo = (CardView) rootView.findViewById(R.id.cardViewAdBuddizVideoAd);
        cvAdmobFullScreen = (CardView) rootView.findViewById(R.id.cardViewAdmobFullPage);
        cvAdmobVideo = (CardView) rootView.findViewById(R.id.cardViewAdmobVideo);
        cvStartapp = (CardView) rootView.findViewById(R.id.cardViewStartAp);
        cvNativex = (CardView) rootView.findViewById(R.id.cardViewNativeX);
        cvMinimob = (CardView) rootView.findViewById(R.id.cardViewMinimob);
        llWoobiOfferwall = (LinearLayout) rootView.findViewById(R.id.llWoobiOfferwall);
//        llExpletus = (LinearLayout) rootView.findViewById(R.id.ll_Expletus);
        llSuperSonic = (LinearLayout) rootView.findViewById(R.id.llSuperSonic);
        llAdBuddiz = (LinearLayout) rootView.findViewById(R.id.llAdBuddiz);
        llAdBuddizVideoAd = (LinearLayout) rootView.findViewById(R.id.llAdBuddizVideoAd);
        llAdmobFullScreen = (LinearLayout) rootView.findViewById(R.id.llAdmobFullPage);
        llAdMobVideo = (LinearLayout) rootView.findViewById(R.id.llAdmobVideo);
        llStartapp = (LinearLayout) rootView.findViewById(R.id.llStartAp);
        llNativex = (LinearLayout) rootView.findViewById(R.id.llNativeX);
        llMinimob = (LinearLayout) rootView.findViewById(R.id.llMinimob);
        iv_arrowminimob = (ImageView) rootView.findViewById(R.id.imageview_arrow);
//        iv_arrowexpletus = (ImageView) rootView.findViewById(R.id.imageview_arrow_expletus);
        listViewMinimob = (ListView) rootView.findViewById(R.id.listview_available_offers);
//        llExpletus.setOnClickListener(this);
        llWoobiOfferwall.setOnClickListener(this);
        llSuperSonic.setOnClickListener(this);
        llAdBuddiz.setOnClickListener(this);
        llAdBuddizVideoAd.setOnClickListener(this);
        llAdmobFullScreen.setOnClickListener(this);
        llAdMobVideo.setOnClickListener(this);
        llStartapp.setOnClickListener(this);
        llNativex.setOnClickListener(this);
        llMinimob.setOnClickListener(this);
    }

    private void initAds() {
        interstitialAd1 = new InterstitialAd(getActivity());
        interstitialAd1.setAdUnitId(sharedPrefs.getAdmobId());
//        adRequest1 = new AdRequest.Builder().addTestDevice("09BD7D61262E93D391E404915854F167").build();
        adRequest1 = new AdRequest.Builder().build();
        interstitialAd2 = new InterstitialAd(getActivity());
        interstitialAd2.setAdUnitId(sharedPrefs.getAdmobIdVideo());
        adRequest2 = new AdRequest.Builder().build();
        mMediationAgent = SupersonicFactory.getInstance();
        String mAppKey = sharedPrefs.getSupersonicId();
        SupersonicConfig.getConfigObj().setClientSideCallbacks(true);
        mMediationAgent.initOfferwall(getActivity(), mAppKey, Constants.getImeiNum(getActivity()));
        startAppAd = new StartAppAd(getActivity());
    }

    private void requestForOffers() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("deviceid", Constants.getImeiNum(getActivity()));
        params.put("uniqueid", sharedPrefs.getUserKey());
//        params.put("uniqueid", "35385337");
        customProgressDialog.show();
        AppController.getInstance().addToRequestQueue(requestCustomOffers(Frs_Constants.OFFERS, params), TAG_JSON);
    }

    private CustomJsonObjectRequest requestCustomOffers(String url, Map<String, String> params) {
        return new CustomJsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (customProgressDialog != null)
                    customProgressDialog.dismiss();
                try {
                    if (!response.get("Successfull").toString().equals("0")) {
                        System.out.println("kamalresponseoffer " + response.get("offer"));
                        System.out.println("kamalresponseoffer " + sharedPrefs.getAdmobId());
                        System.out.println("kamalresponseoffer " + sharedPrefs.getAdmobIdVideo());
                        requestForRemainingOffers(Long.parseLong(response.get("time_stamp").toString()));
                        if(response.get("offer").toString().contains("a")){
//                            cvExpletus.setVisibility(View.VISIBLE);
                            openExpletusOffer();
                        }
                        if (response.get("offer").toString().contains("1")) {
                            cvAdmobFullScreen.setVisibility(View.VISIBLE);
                        }
                        if (response.get("offer").toString().contains("2")) {
//                            llAdMobVideo.setVisibility(View.VISIBLE);
                            cvWoobi.setVisibility(View.VISIBLE);
                        }
                        if (response.get("offer").toString().contains("3")) {
                            cvSupersonic.setVisibility(View.VISIBLE);
                        }
                        if (response.get("offer").toString().contains("4")) {
                            cvStartapp.setVisibility(View.VISIBLE);
                        }
                        if (response.get("offer").toString().contains("5")) {
                            cvAdbuddizVideo.setVisibility(View.VISIBLE);
                        }
                        if (response.get("offer").toString().contains("6")) {
                            cvAdbuddizFullScreen.setVisibility(View.VISIBLE);
                        }
                        if (response.get("offer").toString().contains("7")) {
                            cvAdmobVideo.setVisibility(View.VISIBLE);
                        }
                        if (response.get("offer").toString().contains("8")) {
                            cvNativex.setVisibility(View.VISIBLE);
                        }
                        if(response.get("offer").toString().contains("9")){
                            cvMinimob.setVisibility(View.VISIBLE);
                        }
                        if (sharedPrefs.isShowCampaign()) {
                            handlerCampaign = new Handler();
                            handlerCampaign.postDelayed(campaignRunnable, 4000);
                        }
                    } else {
                        Toast.makeText(getActivity(), "There is some problem. Try again later!", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (getActivity() != null) {
                    if (customProgressDialog != null)
                        customProgressDialog.dismiss();
                    Toast.makeText(getActivity(), "Error! try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void requestForRemainingOffers(long timeStamp) {

        if (timeStamp > sharedPrefs.getCurrentTimeStamp()) {
            sharedPrefs.setAdMobFullPageMoney(50);
            sharedPrefs.setAdMobMoneyvideo(70);
            sharedPrefs.setStartAppMoney(50);
            sharedPrefs.setAdBuddizFullscreenMoney(50);
            sharedPrefs.setAdBuddizVideoMoney(50);
            sharedPrefs.setCurrentTimeStamp(timeStamp);
        }
    }

    private void alertNetwork() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.DevenDialogTheme);
        alertDialogBuilder.setTitle("Internet unavailable");
        alertDialogBuilder
                .setMessage("Check your Internet connection and try again.")
                .setCancelable(false)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            Window window = alertDialog.getWindow();
            window.setBackgroundDrawable(new ColorDrawable(
                    android.graphics.Color.TRANSPARENT));
        }
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
//        if(v == llExpletus){
//            if(!isExpletusOfferListShowing) {
//                if(listOfOffersExpletus.size()==0) {
//                    customProgressDialog.show();
//                    AppController.getInstance().addToRequestQueue(requestExpletusOffer(getResources().getString(R.string.url_expletus)), TAG_EXPLETUS);
//
//                }else{
//                    iv_arrowexpletus.setImageResource(R.mipmap.ic_down);
//                    expletusListview.setVisibility(View.VISIBLE);
//                    isExpletusOfferListShowing = true;
//                }
//            }else{
//                isExpletusOfferListShowing = false;
//                expletusListview.setVisibility(View.GONE);
//                iv_arrowexpletus.setImageResource(R.mipmap.ic_go);
//            }
//        }
        if (v == llWoobiOfferwall) {
            if (new ConnectionCheck(getActivity()).isConnectionAvailable()) {
//                offerPopUp(1, "To avail your best offer click Get Offer and watch your favourite ad video fully and then click to view your favourite ad.\n" +
//                        "\n" +
//                        "If you fail to do so you won't be get your offer." + "\nYou will only get rewards for 4 videos per day in order 100, 75, 50, and 25 credits.");
                offerPopUp(1, "To avail your best offer, install and run application.\nYou will get credits only when we get confirmation that you have done all above and have not installed this app before.\n" +
                        "\n" +
                        "If you fail to do so you won't be get your offer.\"");
            } else {
                alertNetwork();
            }
        }
        if (v == llAdmobFullScreen) {
            sharedPrefs.setAdClicked(false);
            if (new ConnectionCheck(getActivity()).isConnectionAvailable()) {
                offerPopUp(2, "To avail your best offer click Get Offer and click to your favourite ad.\n" +
                        "\n" +
                        "If you fail to do so you won't be get your offer.\"\n" +
                        "You will only get rewards for 5 ads per day in order 50, 40, 30, 20, and 10 credits.");
            } else {
                alertNetwork();
            }
        }
        if (v == llAdMobVideo) {
            sharedPrefs.setAdClicked(false);
            if (new ConnectionCheck(getActivity()).isConnectionAvailable()) {
                offerPopUp(7, "To avail your best offer click Get Offer and watch your favourite ad video fully and then click to install the app.\n" +
                        "\n" +
                        "If you fail to do so you won't be get your offer." + "\nYou will only get rewards for 4 videos per day in order 70, 50, 30, and 10 credits.");
            } else {
                alertNetwork();
            }
        }
        if (v == llSuperSonic) {
            if (new ConnectionCheck(getActivity()).isConnectionAvailable()) {
                offerPopUp(3, "To avail your best offer, install and run application.\nYou will get credits only when we get confirmation that you have done all above and have not installed this app before.\n" +
                        "\n" +
                        "If you fail to do so you won't be get your offer.\"");
            } else {
                alertNetwork();
            }
        }

        if (v == llAdBuddiz) {
            sharedPrefs.setAdClicked(false);
            if (new ConnectionCheck(getActivity()).isConnectionAvailable()) {
                offerPopUp(6, "To avail your best offer click Get Offer and click and install your favourite apps.\n" +
                        "\n" +
                        "If you fail to do so you won't be get your offer.\"\n" +
                        "You will only get rewards for 5 ads per day in order 50, 40, 30, 20, and 10 credits.");
            } else {
                alertNetwork();
            }
        }
        if (v == llAdBuddizVideoAd) {
            sharedPrefs.setAdClicked(false);
            if (new ConnectionCheck(getActivity()).isConnectionAvailable()) {
                offerPopUp(5, "To avail your best offer click Get Offer and watch your favourite ad video fully and then click to install the app.\n" +
//                        "\n" +
                        "If you fail to do so you won't be get your offer." + "\nYou will only get rewards for 5 videos per day in order 50, 40, 30 ,20 , and 10 credits.");
            } else {
                alertNetwork();
            }
        }
        if (v == llStartapp) {
            sharedPrefs.setAdClicked(false);
            if (new ConnectionCheck(getActivity()).isConnectionAvailable()) {
                offerPopUp(4, "To avail your best offer click Get Offer and click and install your favourite apps.\n" +
                        "\n" +
                        "If you fail to do so you won't be get your offer.\"\n" +
                        "You will only get rewards for 5 ads per day in order 50, 40, 30, 20, and 10 credits.");
            } else {
                alertNetwork();
            }
        }
        if (v == llNativex) {
            if (new ConnectionCheck(getActivity()).isConnectionAvailable()) {
                offerPopUp(8, "To avail your best offer, install and run application.\nYou will get credits only when we get confirmation that you have done all above and have not installed this app before.\n" +
                        "\n" +
                        "If you fail to do so you won't be get your offer.\"");
            } else {
                alertNetwork();
            }
        }
        if(v == llMinimob){
            minimobOffersAccess();
        }

    }
    private void openExpletusOffer(){
                AppController.getInstance().addToRequestQueue(requestExpletusOffer(getResources().getString(R.string.url_expletus)), TAG_EXPLETUS);
            }

    private void offerPopUp(final int i, String message) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.DevenDialogTheme);
        alertDialogBuilder.setTitle("Offer");
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("Get Offer",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                customProgressDialog.show();
                                if (i == 1) {
                                    long value = System.currentTimeMillis();
                                    String myClientId = getImeiNum() + "mtech" + String.valueOf(value);
                                    Woobi.showOffers(getActivity(), sharedPrefs.getWoobiId(), myClientId);
                                } else if (i == 2) {
                                    interstitialAd1.loadAd(adRequest1);
                                    addAdmobAdListner1();
                                } else if (i == 3) {
                                    mMediationAgent.showOfferwall();
                                } else if (i == 4) {
                                    loadStartAppAd();
                                } else if (i == 5) {
                                    initAdBuddizvideo();
                                } else if (i == 6) {
                                    if (AdBuddiz.isReadyToShowAd(getActivity())) {
                                        if (customProgressDialog != null)
                                            customProgressDialog.dismiss();
                                        AdBuddiz.showAd(getActivity());
                                    } else {
                                        if (customProgressDialog != null)
                                            customProgressDialog.dismiss();
                                        Toast.makeText(getActivity(), "Error in loading offer.Try again", Toast.LENGTH_SHORT).show();
                                    }

                                } else if (i == 7) {
                                    interstitialAd2.loadAd(adRequest2);
                                    addAdmobAdListner2();
                                } else if (i == 8) {
                                    handlerNativex.postDelayed(mNativeXLoadSession, 0);
                                }
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            Window window = alertDialog.getWindow();
            window.setBackgroundDrawable(new ColorDrawable(
                    android.graphics.Color.TRANSPARENT));
        }
        alertDialog.show();
    }

    private void initAdBuddizfullpage() {
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
                Log.d("adClicked", " Ad clicked");
                if (!sharedPrefs.isAdClicked()) {
                    requestForAddingMoney(String.valueOf(sharedPrefs.getAdBuddizFullscreenMoney()));
                    sharedPrefs.setAdClicked(true);
                    if (sharedPrefs.getAdBuddizFullscreenMoney() > 0)
                        sharedPrefs.setAdBuddizFullscreenMoney(sharedPrefs.getAdBuddizFullscreenMoney() - 10);
                }

            }

            @Override
            public void didHideAd() {

            }
        });

    }

    private void initAdBuddizvideo() {
        AdBuddiz.RewardedVideo.fetch(getActivity());
        AdBuddiz.RewardedVideo.setDelegate(new AdBuddizRewardedVideoDelegate() {
            @Override
            public void didFetch() {
                if (customProgressDialog != null) {
                    customProgressDialog.dismiss();
                    AdBuddiz.RewardedVideo.show(getActivity());
                }
            }

            @Override
            public void didFail(AdBuddizRewardedVideoError adBuddizRewardedVideoError) {
                if (customProgressDialog != null) {
                    customProgressDialog.dismiss();
                    Toast.makeText(getActivity(), "Try again later offers not available", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Try again later offers not available", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void didComplete() {
                if (!sharedPrefs.isAdClicked()) {
                    requestForAddingMoney(String.valueOf(sharedPrefs.getAdBuddizVideoMoney()));
                    sharedPrefs.setAdClicked(true);
                    if (sharedPrefs.getAdBuddizVideoMoney() > 0)
                        sharedPrefs.setAdBuddizVideoMoney(sharedPrefs.getAdBuddizVideoMoney() - 10);
                }

            }

            @Override
            public void didNotComplete() {
                new AlertDialog.Builder(getActivity()).setMessage("Either you did not completed the offer or your device doesn't support the video format.So no credit will be added").setPositiveButton("Ok", null).show();

            }
        });
    }

    private void loadStartAppAd() {
        startAppAd.loadAd(new AdEventListener() {
            @Override
            public void onReceiveAd(Ad ad) {
                if (customProgressDialog != null)
                    customProgressDialog.dismiss();
                showAd();
            }

            @Override
            public void onFailedToReceiveAd(Ad ad) {
                if (customProgressDialog != null)
                    customProgressDialog.dismiss();
            }
        });
    }

    private void showAd() {
        startAppAd.showAd(new AdDisplayListener() {
            @Override
            public void adHidden(Ad ad) {
                Log.d("adHidden", " Ad hidden");

            }

            @Override
            public void adDisplayed(Ad ad) {
                Log.d("adDisplayed", " Ad Displayed");
            }

            @Override
            public void adClicked(Ad ad) {
                Log.d("adClicked", " Ad clicked");
                if (!sharedPrefs.isAdClicked()) {
                    requestForAddingMoney(String.valueOf(sharedPrefs.getStartAppMoney()));
                    sharedPrefs.setAdClicked(true);
                    if (sharedPrefs.getStartAppMoney() > 0)
                        sharedPrefs.setStartAppMoney(sharedPrefs.getStartAppMoney() - 10);
                }
            }
        });
    }

    private void addAdmobAdListner1() {
        interstitialAd1.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (customProgressDialog != null)
                    customProgressDialog.dismiss();
                if (interstitialAd1.isLoaded()) {
                    interstitialAd1.show();
                } else {
                }

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                if (customProgressDialog != null)
                    customProgressDialog.dismiss();

            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                if (handler != null) {
                    handler.removeCallbacks(mRunnable);
                }
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                Log.e("admob ", "leftapp");
                if (!sharedPrefs.isAdClicked()) {
                    requestForAddingMoney(String.valueOf(sharedPrefs.getAdMobFullPageMoney()));
                    sharedPrefs.setAdClicked(true);
                    if (sharedPrefs.getAdMobFullPageMoney() > 0)
                        sharedPrefs.setAdMobFullPageMoney(sharedPrefs.getAdMobFullPageMoney() - 10);
                }
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                startActivity(new Intent(getActivity(), TransparentActivity.class));
//                int value = Constants.getRandomnumForAd(numbers);
//                if (value != 1 && value != 3 && value != 5 && value != 7) {
//                    handler = new Handler();
//                    handler.postDelayed(mRunnable, 1000);
//                }

            }
        });
    }

    private void addAdmobAdListner2() {
        interstitialAd2.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (customProgressDialog != null)
                    customProgressDialog.dismiss();
                if (interstitialAd2.isLoaded()) {
                    interstitialAd2.show();
                } else {
                }
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                if (customProgressDialog != null) {
                    customProgressDialog.dismiss();
                }
                Toast.makeText(getActivity(), "Error in Loading Offer.Try Again Later", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                if (!sharedPrefs.isAdClicked()) {
                    requestForAddingMoney(String.valueOf(sharedPrefs.getAdMobMoneyvideo()));
                    sharedPrefs.setAdClicked(true);
                    if (sharedPrefs.getAdMobMoneyvideo() > 0)
                        if (sharedPrefs.getAdMobMoneyvideo() == 10) {
                            sharedPrefs.setAdMobMoneyvideo(0);
                        } else {
                            sharedPrefs.setAdMobMoneyvideo(sharedPrefs.getAdMobMoneyvideo() - 20);
                        }
                }

            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    };
    private CustomJsonArrayRequest requestExpletusOffer(String url){

        Map<String, String> params = new HashMap<String, String>();
        params.put("deviceid", Constants.getImeiNum(getActivity()));
        CustomJsonArrayRequest customJsonArrayRequest = new CustomJsonArrayRequest(Request.Method.POST, url, params, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (customProgressDialog != null)
                    customProgressDialog.dismiss();
                expletusListview.setVisibility(View.VISIBLE);
                listOfOffersExpletus.clear();
                sharedPrefs.setExpletusOfferNativePackage("");
                if(response.length()>0) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject result = response.getJSONObject(i);
                            PojoExpletus pojo = new PojoExpletus();
                            pojo.setAppname(result.getString("surveyname"));
                            pojo.setImageurl(result.getString("imgurl"));
                            pojo.setDesciption(result.getString("description"));
                            pojo.setPayout(result.getString("payout"));
                            pojo.setPlayStoreLink(result.getString("linkurl"));
                            pojo.setCampaignType(result.getString("CampaignType"));
                            listOfOffersExpletus.add(pojo);

                           String pay= String.valueOf(((int) (Float.parseFloat(result.getString("payout").replaceAll("[^0-9.]", "").substring(0, pojo.getPayout().replaceAll("[^0-9.]", "").length() - 1)) * 100))/4) ;

                            if (TextUtils.isEmpty(sharedPrefs.getExpletusOfferNativePackage())) {
                                sharedPrefs.setExpletusOfferNativePackage(result.getString("CampaignType").toString() + "payout" + pay + "appName" + result.getString("surveyname").toString());

                            } else {
                                if (!sharedPrefs.getExpletusOfferNativePackage().contains(result.getString("CampaignType").toString())) {
                                    sharedPrefs.setExpletusOfferNativePackage(sharedPrefs.getExpletusOfferNativePackage() + "," + result.getString("CampaignType").toString() + "payout" + pay + "appName" + result.getString("surveyname").toString());
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    expletusListview.setAdapter(new ExpletusAdapter(getActivity(), listOfOffersExpletus, AppController.getInstance().getImageLoader(), OffersFragment.this));
                    ScrollingListView.setListViewHeightBasedOnChildren(expletusListview);
                }
                else{
                    Toast.makeText(getActivity(),"No Offers Available",Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (customProgressDialog != null)
                    customProgressDialog.dismiss();
            }
        });
        customJsonArrayRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 4,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        return customJsonArrayRequest;
    }
    private void requestForAddingMoney(String credit) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("deviceid", Constants.getImeiNum(getActivity()));
        params.put("uniqueid", sharedPrefs.getUserKey());
//        params.put("uniqueid", "35385337");
        params.put("credit", credit);
        params.put("fromwhere", "offer");
        AppController.getInstance().addToRequestQueue(requestCustomAddMoney(Frs_Constants.INSERT_CREDIT, params), TAG_JSON1);
    }

    private CustomJsonObjectRequest requestCustomAddMoney(String url, Map<String, String> params) {
        return new CustomJsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

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


    @Override
    public void onOfferwallInitSuccess() {

    }

    @Override
    public void onOfferwallInitFail(SupersonicError supersonicError) {

    }

    @Override
    public void onOfferwallOpened() {

    }

    @Override
    public void onOfferwallShowFail(SupersonicError supersonicError) {

    }

    @Override
    public boolean onOfferwallAdCredited(final int i, final int i1, boolean b) {
//        if(b)
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (i == i1) {
                    } else {
                        if (i > 0) {
                            Toast.makeText(getActivity(), "Rewarded Credits " + i, Toast.LENGTH_LONG).show();
                            requestForAddingMoney(String.valueOf(i));
                        }
                    }
                }
            });
        }
        return true;
    }

    @Override
    public void onGetOfferwallCreditsFail(SupersonicError supersonicError) {
    }

    @Override
    public void onOfferwallClosed() {
    }

    private void minimobOffersAccess() {
        if (showlistminimob == false) {
            if (offersminimob == false) {
                if (progressBarMinimob != null)
                    progressBarMinimob.setVisibility(View.VISIBLE);
                AppController.getInstance().addToRequestQueue(requestMiniMobAdArray(URL_MINIMOBAVAILABLE), TAG_JSON_MINIMOB);

            } else {
                iv_arrowminimob.setImageResource(R.mipmap.ic_down);
                listViewMinimob.setVisibility(View.VISIBLE);
                showlistminimob = true;
            }
        } else {
            showlistminimob = false;
            listViewMinimob.setVisibility(View.GONE);
            iv_arrowminimob.setImageResource(R.mipmap.ic_go);
        }
    }
    private CustomJsonArrayRequest requestMiniMobAdArray(String miniMobUrl) {

        CustomJsonArrayRequest customJsonArrayRequest = new CustomJsonArrayRequest(Request.Method.GET, miniMobUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                iv_arrowminimob.setImageResource(R.mipmap.ic_down);
                listViewMinimob.setVisibility(View.VISIBLE);
                showlistminimob = true;
                offersminimob = true;
                listOfOffersMinimob.clear();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject result = response.getJSONObject(i);
                        Pojo_availableoffer pojo = new Pojo_availableoffer();
                        pojo.setId(result.getString("id").toString());
                        pojo.setAppName(result.getString("name").toString());
                        pojo.setPayOut(convertPayoutIntoCreditsMinimob(result.getString("payout").toString()));
                        pojo.setImageUrl(result.getString("appIconLink").toString());
                        pojo.setPlayStoreLink(result.getString("objectiveUrl").toString());
                        pojo.setAppDescription(result.getString("appDescription").toString());
                        listOfOffersMinimob.add(pojo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapterMinimob = new MinimobAdAdapter(getActivity(), listOfOffersMinimob, OffersFragment.this, AppController.getInstance().getImageLoader());
                listViewMinimob.setAdapter(adapterMinimob);
                ScrollingListView.setListViewHeightBasedOnChildren(listViewMinimob);
                if (progressBarMinimob != null)
                    progressBarMinimob.setVisibility(View.GONE);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error ", error.toString());
                if (progressBarMinimob != null)
                    progressBarMinimob.setVisibility(View.GONE);
            }
        });
        customJsonArrayRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 4,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        return customJsonArrayRequest;
    }

    @Override
    public void setOnListItemClickListner_minimob(int position, String playstoreLink) {
        offer_detail_dialog(position);
    }
    private String convertPayoutIntoCreditsMinimob(String value) {
        String pay = null;
        double totalCredits = Double.parseDouble(value) * sharedPrefs.getMinimobConversionRate();
        double miniCredits=(round(totalCredits, 2))*100;
        pay = String.valueOf((int)miniCredits);
        return pay;
    }

    private double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    private void offer_detail_dialog(final int position) {
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.offers_fulldetail_dialog);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        NetworkImageView icon = (NetworkImageView) dialog.findViewById(R.id.imageview_offer_fullinfo);
        icon.setImageUrl(listOfOffersMinimob.get(position).getImageUrl(), AppController.getInstance().getImageLoader());
        TextView appname = (TextView) dialog.findViewById(R.id.textview_appname_fullinfo);
        appname.setText(listOfOffersMinimob.get(position).getAppName());
        TextView description = (TextView) dialog.findViewById(R.id.textview_littleinfo_fullinfo);
        description.setText(listOfOffersMinimob.get(position).getAppDescription());
        TextView payout = (TextView) dialog.findViewById(R.id.textview_payout_fullinfo);
        Button bt_install = (Button) dialog.findViewById(R.id.button_install_and_open);
        payout.setText("Earn " + listOfOffersMinimob.get(position).getPayOut() + " Credits");
        bt_install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentTimeMillis = System.currentTimeMillis();
                String clickId = Constants.getImeiNum(getActivity()) + "mtech" + String.valueOf(currentTimeMillis) + "payOut" + listOfOffersMinimob.get(position).getPayOut() + "offerName" + listOfOffersMinimob.get(position).getAppName()+"frs";
                 makeJsonObjectRequest_downloadMinimob(clickId, listOfOffersMinimob.get(position).getPlayStoreLink());
            }
        });
    }


    private void makeJsonObjectRequest_downloadMinimob(final String clickid, final String playstoreLink) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("deviceid", Constants.getImeiNum(getActivity()));
        params.put("clickid", clickid);
        CustomJsonObjectRequest customJsonObjectRequest = new CustomJsonObjectRequest(Request.Method.POST, URL_INSERTCLICKIDTO_DATABASE, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (progressBarMinimob != null)
                    progressBarMinimob.setVisibility(View.GONE);
                try {
                    if (response.get("Successfull").equals("1")) {
                        String[] separated = playstoreLink.split("&");
                        String link = "http://clicks.minimob.com/tracking/click?clickid=" + clickid + "&" + separated[1] + "&" + separated[2];
                        Intent moreapps = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                        startActivity(moreapps);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressBarMinimob != null)
                    progressBarMinimob.setVisibility(View.GONE);
            }
        });
        customJsonObjectRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 4,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        AppController.getInstance().addToRequestQueue(customJsonObjectRequest);
    }
    public String getImeiNum() {
        String identifier = null;
        TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null)
            identifier = tm.getDeviceId();
        if (identifier == null || identifier.length() == 0)
            identifier = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        return identifier;
    }

    Runnable campaignRunnable = new Runnable() {
        @Override
        public void run() {
            Dfragment dFragment = new Dfragment(sharedPrefs.getCampaignAppname(), sharedPrefs.getCampaignDescription(), sharedPrefs.getCampaignImgurl(), sharedPrefs.getCampaignPlaystoreLink(), sharedPrefs.getCampaignPayout(), sharedPrefs.getCampaignType());
            dFragment.show(fm, "Dialog Fragment");
        }
    };

    @Override
    public void setOnItemButtonClickListenerExpletusOffers(int position, String playStoreLink) {
        campaignAppsDialog(listOfOffersExpletus.get(position).getImageurl(), listOfOffersExpletus.get(position).getAppname(), listOfOffersExpletus.get(position).getDesciption(), listOfOffersExpletus.get(position).getPayout(), playStoreLink, listOfOffersExpletus.get(position).getCampaignType());

    }
    private void campaignAppsDialog(String appIconUrl, final String appName, String appDescription, final String appPayout, final String playStoreLink,final String campaignType) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.offers_fulldetail_dialog);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        NetworkImageView icon = (NetworkImageView) dialog.findViewById(R.id.imageview_offer_fullinfo);
        icon.setImageUrl(appIconUrl, AppController.getInstance().getImageLoader());
        TextView appname = (TextView) dialog.findViewById(R.id.textview_appname_fullinfo);
        appname.setText(appName);
        TextView description = (TextView) dialog.findViewById(R.id.textview_littleinfo_fullinfo);
        description.setText(appDescription);
        TextView payout = (TextView) dialog.findViewById(R.id.textview_payout_fullinfo);
        Button bt_install = (Button) dialog.findViewById(R.id.button_install_and_open);
        payout.setText("Complete offer and Earn " + String.valueOf((int) (Float.parseFloat(appPayout.replaceAll("[^0-9.]", "").substring(0, appPayout.replaceAll("[^0-9.]", "").length() - 1)) * 100)) + " Credits");
//        payout.setText("Earn " + String.valueOf((int) (Float.parseFloat(appPayout.replaceAll("rs.","").replaceAll("Rs.",""))*100)) + " Credits");

        bt_install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String payout=getFloatPayout(appPayout);
                long currentTimeMillis = System.currentTimeMillis();
                String clickid = String.valueOf(currentTimeMillis);
                String subid2 = Constants.getImeiNum(getActivity()) + "mtech" + String.valueOf(currentTimeMillis) + "Appname" + "frs" + "Payout"+ payout + "Campaigntype" +campaignType;
                makeRequestForCampaign(clickid, subid2, playStoreLink,payout,campaignType);

            }
        });
    }
    private void makeRequestForCampaign(final String clickid, final String subid2, final String playStoreLink,final String payout,final String campaignType) {
        String url =URL_INSERTCLICKEXPLETUS;
        url = url + "deviceid=" + Constants.getImeiNum(getActivity()) + "&appname=" + "frs" +"Payout"+ payout + "Campaigntype" + campaignType;
        CustomJsonObjectRequest customJsonObjectRequest = new CustomJsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String link = playStoreLink + clickid + "/?sub_id2=" + subid2;
                System.out.println("kamalvermacamp "+link);
                Intent campaignApps = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(campaignApps);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Error.Try again", Toast.LENGTH_SHORT).show();
            }
        });
        customJsonObjectRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 4,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        AppController.getInstance().addToRequestQueue(customJsonObjectRequest);
    }
    private String getFloatPayout(String value){
        String str=String.valueOf(((int)(Float.parseFloat(value.replaceAll("[^0-9.]", "").substring(0, value.replaceAll("[^0-9.]", "").length() - 1))*100))/4);
        return  str;
    }
}
