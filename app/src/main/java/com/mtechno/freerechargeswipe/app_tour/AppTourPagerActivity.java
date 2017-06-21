package com.mtechno.freerechargeswipe.app_tour;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mtechno.freerechargeswipe.R;
import com.mtechno.freerechargeswipe.activities.RegistrationActivity;
import com.mtechno.freerechargeswipe.services.ScreenListener;
import com.mtechno.freerechargeswipe.utils.SharedPrefs;

import java.util.ArrayList;
import java.util.List;

public class AppTourPagerActivity extends ActionBarActivity {

    TextView skipJoin;
    Bundle bundle;
    SharedPrefs sharedPrefs;
//    final int PERMISSION_READPHONE=1;
    final int REQUEST_CODE_SOME_FEATURES_PERMISSIONS=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        bundle = getIntent().getExtras();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            permissionCheck(); 
        }
        ViewPager customViewpager = (ViewPager) findViewById(R.id.viewpager);
        customViewpager.setPageTransformer(true,new CubeOutTransformer());
        FragmentIndicator customIndicator = (FragmentIndicator) findViewById(R.id.fragment_indicator);
        TourPagerAdapter customPagerAdapter = new TourPagerAdapter(getSupportFragmentManager());
        customViewpager.setAdapter(customPagerAdapter);
        customViewpager.setCurrentItem(0);
        customIndicator.setViewPager(customViewpager);
        customIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        skipJoin = (TextView) findViewById(R.id.skip_join);
        skipJoin.bringToFront();
        if (bundle != null) {
            if (bundle.getString("appTour").equalsIgnoreCase("fromHelp")) ;
            {
                skipJoin.setText("Skip Tour");
            }
        }
        skipJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
                    if (PackageManager.PERMISSION_GRANTED == checkSelfPermission(Manifest.permission.READ_PHONE_STATE) && PackageManager.PERMISSION_GRANTED == checkSelfPermission(Manifest.permission.RECORD_AUDIO)) {
                        if (skipJoin.getText().toString().equalsIgnoreCase("Skip Tour")) {

                        } else {
                            Intent intent = new Intent(AppTourPagerActivity.this, RegistrationActivity.class);
                            startActivity(intent);
                        }
                        finish();
                    } else {
                        permissionCheck();
                    }
                } else {
                    if (skipJoin.getText().toString().equalsIgnoreCase("Skip Tour")) {

                    } else {
                        Intent intent = new Intent(AppTourPagerActivity.this, RegistrationActivity.class);
                        startActivity(intent);
                    }
                    finish();
                }
//
            }
        });
        sharedPrefs=new SharedPrefs(this);
        Intent iService = new Intent(this, ScreenListener.class);
        startService(iService);
        sharedPrefs.setServiceRunning(true);
    }
    @TargetApi(Build.VERSION_CODES.M)
              private void permissionCheck() {
        int readphoneState = checkSelfPermission(Manifest.permission.READ_PHONE_STATE);
        int audioRecord = checkSelfPermission(Manifest.permission.RECORD_AUDIO);
        List<String> permissions = new ArrayList<String>();
        if (readphoneState != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (audioRecord != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.RECORD_AUDIO);
        }
        if (!permissions.isEmpty()) {
            requestPermissions(permissions.toArray(new String[permissions.size()]), REQUEST_CODE_SOME_FEATURES_PERMISSIONS);
        }
    }

//
//    private void readphonePermission(){
//            if (ContextCompat.checkSelfPermission(AppTourPagerActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                if (ActivityCompat.shouldShowRequestPermissionRationale(AppTourPagerActivity.this, android.Manifest.permission.READ_PHONE_STATE)) {
//                    ActivityCompat.requestPermissions(AppTourPagerActivity.this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, PERMISSION_READPHONE);
//
//                } else {
//                    ActivityCompat.requestPermissions(AppTourPagerActivity.this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, PERMISSION_READPHONE);
//                }
//            }
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        switch (requestCode){
//            case PERMISSION_READPHONE:
//                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
//                    if (skipJoin.getText().toString().equalsIgnoreCase("Skip Tour")) {
//
//                    } else {
//                        Intent intent = new Intent(AppTourPagerActivity.this, RegistrationActivity.class);
//                        startActivity(intent);
//                    }
//                    finish();
//                }else{
//                    readphonePermission();
//                }
//                break;
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_SOME_FEATURES_PERMISSIONS: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Log.d("Permissions", "Permission Granted: " + permissions[i]);

                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    }
                }
            }
            break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }
}