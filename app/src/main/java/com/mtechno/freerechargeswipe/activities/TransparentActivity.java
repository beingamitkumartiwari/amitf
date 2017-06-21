package com.mtechno.freerechargeswipe.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.mtechno.freerechargeswipe.R;
import com.mtechno.freerechargeswipe.utils.SharedPrefs;

/**
 * Created by KAMAL VERMA on 3/25/2016.
 */
public class TransparentActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        setContentView(R.layout.transparent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent goToA = new Intent(this,MainActivity.class);
        goToA.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(goToA);
    }
}
