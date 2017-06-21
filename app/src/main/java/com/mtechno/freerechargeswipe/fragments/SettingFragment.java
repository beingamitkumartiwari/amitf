package com.mtechno.freerechargeswipe.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.mtechno.freerechargeswipe.R;
import com.mtechno.freerechargeswipe.activities.AboutActivity;
import com.mtechno.freerechargeswipe.app_tour.AppTourPagerActivity;
import com.mtechno.freerechargeswipe.services.ScreenListener;
import com.mtechno.freerechargeswipe.utils.SharedPrefs;

/**
 * Created by DEVEN SINGH on 4/21/2015.
 */
public class SettingFragment extends Fragment implements View.OnClickListener {

    TextView aboutUs;
    TextView feedBack;
    TextView shareApp;
    TextView rateUs;
    TextView faq;
    TextView appTour;
    LinearLayout llActivator;
    CheckBox activatorCB;
    SharedPrefs sharedPrefs;
    TextView activatorTv;
    Spinner spinnerClock;
    String[] clock = {"Digital Clock", "Analog Clock"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        initComponents(rootView);
        return rootView;
    }

    private void initComponents(View rootView) {
        sharedPrefs = new SharedPrefs(getActivity());
        llActivator = (LinearLayout) rootView.findViewById(R.id.ll_activator);
        activatorCB = (CheckBox) rootView.findViewById(R.id.activator);
        activatorTv = (TextView) rootView.findViewById(R.id.activateTv);
        aboutUs = (TextView) rootView.findViewById(R.id.about_us);
        feedBack = (TextView) rootView.findViewById(R.id.feeback);
        rateUs = (TextView) rootView.findViewById(R.id.rate_us);
        faq = (TextView) rootView.findViewById(R.id.faq);
        shareApp = (TextView) rootView.findViewById(R.id.share_app);
        appTour = (TextView) rootView.findViewById(R.id.appTour);
        shareApp.setOnClickListener(this);
        aboutUs.setOnClickListener(this);
        feedBack.setOnClickListener(this);
        rateUs.setOnClickListener(this);
        faq.setOnClickListener(this);
        appTour.setOnClickListener(this);
        spinnerClock = (Spinner) rootView.findViewById(R.id.spinnerClock);
        llActivator.setOnClickListener(this);
        if (sharedPrefs.isServiceRunning()) {
            activatorTv.setText("Deactivate Lock");
        } else {
            activatorTv.setText("Activate Lock");
        }
        activatorCB.setChecked(sharedPrefs.isServiceRunning());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, clock);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerClock.setAdapter(adapter);
        if (sharedPrefs.isAnologClock()) {
            spinnerClock.setSelection(1);
        } else {
            spinnerClock.setSelection(0);
        }
        spinnerClock.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#000000"));
                if (position == 0) {
                    sharedPrefs.setAnologClock(false);
                } else {
                    sharedPrefs.setAnologClock(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.about_us:
                Intent iAbout=new Intent(getActivity(),AboutActivity.class);
                startActivity(iAbout);
                break;
            case R.id.feeback:
                sendFeedBack();
                break;
            case R.id.rate_us:
                Intent intentPage = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.mtechno.freerechargeswipe"));
                startActivity(intentPage);
                break;
            case R.id.faq:
                goToPageFaq("http://mtechnovation.com/faq.aspx");
                break;
            case R.id.share_app:
                shareApp();
                break;
            case R.id.appTour:
                Intent iTour = new Intent(getActivity(), AppTourPagerActivity.class);
                iTour.putExtra("appTour", "fromHelp");
                startActivity(iTour);
                break;
            case R.id.ll_activator:
                    Intent iService = new Intent(getActivity(), ScreenListener.class);
                    if (activatorCB.isChecked()) {
                        activatorCB.setChecked(false);
                        sharedPrefs.setServiceRunning(false);
                        activatorTv.setText("Activate Lock");
                        getActivity().stopService(iService);
                    } else {
                        getActivity().startService(iService);
                        sharedPrefs.setServiceRunning(true);
                        activatorCB.setChecked(true);
                        activatorTv.setText("Deactivate Lock");
                    }
                break;
        }
    }

    private void sendFeedBack() {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL,
                new String[]{"support@mtechnovation.com"});
        email.putExtra(Intent.EXTRA_SUBJECT, "Free Recharge Swipe Feedback");
        email.setType("message/rfc822");
        startActivity(Intent.createChooser(email,
                "Choose an Email client:"));
    }

    private void shareApp() {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "This application is awesome you should try it." + "\n" + "https://play.google.com/store/apps/details?id=com.mtechno.freerechargeswipe";
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    private void goToPageFaq(String url) {
        Intent intentPage = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intentPage);
    }
}
