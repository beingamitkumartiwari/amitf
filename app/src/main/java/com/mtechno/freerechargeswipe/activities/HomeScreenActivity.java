package com.mtechno.freerechargeswipe.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.WindowManager;

import com.mtechno.freerechargeswipe.R;
import com.mtechno.freerechargeswipe.fragments.LockScreenFragment;

/**
 * Created by DEVEN SINGH on 5/1/2015.
 */
public class HomeScreenActivity extends Activity {

    LockScreenFragment lockScreenFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        setContentView(R.layout.activity_homescreen);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        lockScreenFragment = new LockScreenFragment();
        fragmentTransaction.add(R.id.container, lockScreenFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
    }
}
