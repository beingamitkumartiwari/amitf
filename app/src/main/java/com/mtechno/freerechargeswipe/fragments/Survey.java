package com.mtechno.freerechargeswipe.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.markelytics.markelyticssdkjar.MarkelyticsSDK;
import com.mtechno.freerechargeswipe.R;
import com.mtechno.freerechargeswipe.activities.MainActivity;
import com.mtechno.freerechargeswipe.extras.Constants;
import com.mtechno.freerechargeswipe.extras.Frs_Constants;
import com.mtechno.freerechargeswipe.utils.SharedPrefs;
import com.mtechno.freerechargeswipe.volley_works.AppController;
import com.mtechno.freerechargeswipe.volley_works.CustomJsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DEVEN SINGH on 11/4/2015.
 */
public class Survey extends AppCompatActivity implements View.OnClickListener {

    ProgressBar progressBar;
    SharedPrefs sharedPrefs;
    String TAG_JSON1="addMoney";
    boolean showSurvey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_survey);
        initComponents();
    }
    private void initComponents(){
        sharedPrefs=new SharedPrefs(this);
        progressBar= (ProgressBar) findViewById(R.id.progressBarSurvey);
        progressBar.setVisibility(View.VISIBLE);
        MarkelyticsSDK.init(sharedPrefs.getSurveyDistributioId(), sharedPrefs.getSurveyAppId(), this);
        MarkelyticsSDK.tryLoadSurvey();
        MarkelyticsSDK.getCompatibility(new JSONObject());
        showSurvey();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        MarkelyticsSDK.removeSurvey();
    }

    @Override
    public void onClick(View v) {

    }

    private void showSurvey() {
        MarkelyticsSDK.instance.registerListener(new MarkelyticsSDK.SurveyListener() {
            @Override
            public void onSurveyClosed(String s) {
                if (s.contains("success")) {
                    requestForAddingMoney(String.valueOf(sharedPrefs.getSurveyAmount()));
                }

            }

            @Override
            public void surveyTriedToLoad(String s) {
                progressBar.setVisibility(View.GONE);
                if (s.contains("loaded")) {
                    MarkelyticsSDK.showSurveyForm();
                } else {
                    Toast.makeText(Survey.this, "Error in loading survey.Try again later ", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCompatibilityReceived(String s) {
//Toast.makeText(Survey.this,"compatibility "+s,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onQuestionReceived(String s) {
            }

            @Override
            public void onNoInternetConnection(String s) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Survey.this, "No Internet Connection ", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void requestForAddingMoney(String credit) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("deviceid", Constants.getImeiNum(Survey.this));
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
                Toast.makeText(Survey.this, "Congratulation.You got " + sharedPrefs.getSurveyAmount() + " Credits", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Survey.this, MainActivity.class));
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Survey.this, "Error! try again later.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Survey.this, MainActivity.class));
                finish();
            }
        });
    }
}
