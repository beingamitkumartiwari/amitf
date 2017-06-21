package com.mtechno.freerechargeswipe.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.mtechno.freerechargeswipe.R;
import com.mtechno.freerechargeswipe.app_tour.AppTourPagerActivity;
import com.mtechno.freerechargeswipe.utils.SharedPrefs;

/**
 * Created by DEVEN SINGH on 4/22/2015.
 */
public class WelcomeActivity extends Activity {

    SharedPrefs sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        sharedPrefs = new SharedPrefs(this);




        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (sharedPrefs.isRegistered()) {

                        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(WelcomeActivity.this, AppTourPagerActivity.class);
                        startActivity(intent);
                    }
                    finish();
                }
            }
        }).start();
    }
}
