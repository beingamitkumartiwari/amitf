package com.mtechno.freerechargeswipe.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mtechno.freerechargeswipe.R;
import com.mtechno.freerechargeswipe.extras.Constants;
import com.mtechno.freerechargeswipe.extras.Frs_Constants;
import com.mtechno.freerechargeswipe.utils.CustomProgressDialog;
import com.mtechno.freerechargeswipe.utils.SharedPrefs;
import com.mtechno.freerechargeswipe.utils.UserAvatar;
import com.mtechno.freerechargeswipe.utils.UserProfileAvatar;
import com.mtechno.freerechargeswipe.volley_works.AppController;
import com.mtechno.freerechargeswipe.volley_works.ConnectionCheck;
import com.mtechno.freerechargeswipe.volley_works.CustomJsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DEVEN SINGH on 6/11/2015.
 */
public class ProfileCompletionActivity extends AppCompatActivity implements View.OnClickListener {

    UserAvatar userAvatar;
    TextView userName;
    TextView userEmail;
    TextView userGender;
    TextView userDob;
    TextView userIncome;
    TextView userMaritalStatus;
    TextView userOccupation;
    Button skip;
    Button saveChange;
    SharedPrefs sharedPrefs;
    GoogleCloudMessaging gcmObj;
    String regId = "";
    final String TAG_JSON_REG = "requestReg";
    final String TAG_JSON_UPDATE_PROFILE = "requestProfileUpdate";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    CustomProgressDialog customProgressDialog;
    String[] gender = {"Male", "Female"};
    String[] occupation = {"Student", "Employed", "Unemployed", "Housewife", "Retired", "Self Employed"};
    String[] income = {"Nil", "Less than 1 LPA", "Less than 3 LPA", "Less than 5 LPA", "More than 5 LPA"};
    String[] mStatus = {"Single", "Married"};
    String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_completion);
        initViews();
        initGcmReq();
    }


    private void initViews() {
        customProgressDialog = new CustomProgressDialog(ProfileCompletionActivity.this, R.mipmap.loading);
        sharedPrefs = new SharedPrefs(ProfileCompletionActivity.this);
        userAvatar = (UserAvatar) findViewById(R.id.userAvatarC);
        userName = (TextView) findViewById(R.id.userNameC);
        userEmail = (TextView) findViewById(R.id.userEmailC);
        userGender = (TextView) findViewById(R.id.genderC);
        userDob = (TextView) findViewById(R.id.dobC);
        userIncome = (TextView) findViewById(R.id.incomeC);
        userMaritalStatus = (TextView) findViewById(R.id.msC);
        userOccupation = (TextView) findViewById(R.id.occupationC);
        skip = (Button) findViewById(R.id.skipCompleteProf);
        saveChange = (Button) findViewById(R.id.completeProf);
        userGender.setOnClickListener(this);
        userDob.setOnClickListener(this);
        userIncome.setOnClickListener(this);
        userMaritalStatus.setOnClickListener(this);
        userOccupation.setOnClickListener(this);
        skip.setOnClickListener(this);
        saveChange.setOnClickListener(this);
        userName.setText(sharedPrefs.getUserName());
        userGender.setText(sharedPrefs.getUserGender());
        userEmail.setText(sharedPrefs.getUserEmail());
        userDob.setText(sharedPrefs.getUserDob());
        userAvatar.setImageBitmap(new UserProfileAvatar(ProfileCompletionActivity.this).getUserAvatar());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.genderC:
                setProfileValues(R.string.select_gender, R.array.gender, "Gender");
                break;
            case R.id.dobC:
                setUserDob();
                break;
            case R.id.incomeC:
                setProfileValues(R.string.select_income, R.array.income, "Income");
                break;
            case R.id.msC:
                setProfileValues(R.string.select_m_status, R.array.m_status, "Mstatus");
                break;
            case R.id.occupationC:
                setProfileValues(R.string.select_occupation, R.array.occupation, "Occupation");
                break;
            case R.id.skipCompleteProf:
                sharedPrefs.setRegistered(true);
                startActivity(new Intent(ProfileCompletionActivity.this, MainActivity.class));
                finish();
                break;
            case R.id.completeProf:
                updateProfile();
                break;
        }
    }

    private void setProfileValues(int tittle, final int array, final String fromWhich) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileCompletionActivity.this, R.style.DevenDialogTheme);
        builder.setTitle(tittle)
                .setItems(array, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (fromWhich == "Gender") {
                            userGender.setText(gender[which]);
                        }
                        if (fromWhich == "Occupation") {
                            userOccupation.setText(occupation[which]);
                        }
                        if (fromWhich == "Income") {
                            userIncome.setText(income[which]);
                        }
                        if (fromWhich == "Mstatus") {
                            userMaritalStatus.setText(mStatus[which]);
                        }
                    }
                });
        Dialog dialog = builder.create();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            Window window = dialog.getWindow();
            window.setBackgroundDrawable(new ColorDrawable(
                    android.graphics.Color.TRANSPARENT));
        }
        dialog.show();
    }


    private void setUserDob() {
        Date today = new Date();
        Calendar calender = Calendar.getInstance();
        calender.setTime(today);
        calender.add(Calendar.YEAR, -18);
        long minDate = calender.getTime().getTime();
        int mYear = calender.get(Calendar.YEAR);
        int mMonth = calender.get(Calendar.MONTH);
        int mDay = calender.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(ProfileCompletionActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        userDob.setText(dayOfMonth + " " + months[monthOfYear] + " " + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(minDate);
        datePickerDialog.show();
    }

    private void updateProfile() {
        if (new ConnectionCheck(ProfileCompletionActivity.this).isConnectionAvailable()) {
            customProgressDialog.show();
            Map<String, String> params = new HashMap<String,String>();
            params.put("gender", userGender.getText().toString());
            params.put("occupation", userOccupation.getText().toString());
            params.put("maritalstatus", userMaritalStatus.getText().toString());
            params.put("income", userIncome.getText().toString());
            params.put("dob", userDob.getText().toString());
            params.put("deviceid", Constants.getImeiNum(ProfileCompletionActivity.this));
            params.put("uniqueid",sharedPrefs.getUserKey());
            AppController.getInstance().addToRequestQueue(requestUpdateProfile(Frs_Constants.UPDATE_PROFILE, params), TAG_JSON_UPDATE_PROFILE);
        } else {
            alertNetwork();
        }
    }

    private CustomJsonObjectRequest requestUpdateProfile(String url, Map<String, String> params) {
        return new CustomJsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (customProgressDialog != null)
                    customProgressDialog.dismiss();


                try {
                    if (response.getString("Successfull").toString().equals("1")) {
                        Toast.makeText(ProfileCompletionActivity.this, "profile updated successfully.", Toast.LENGTH_SHORT).show();
                        sharedPrefs.setUserGender(userGender.getText().toString());
                        sharedPrefs.setUserDob(userDob.getText().toString());
                        sharedPrefs.setUserIncome(userIncome.getText().toString());
                        sharedPrefs.setUserMaritalStatus(userMaritalStatus.getText().toString());
                        sharedPrefs.setUserOccupation(userOccupation.getText().toString());
                        sharedPrefs.setRegistered(true);
                        startActivity(new Intent(ProfileCompletionActivity.this, MainActivity.class));
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProfileCompletionActivity.this, "Error! try again later.", Toast.LENGTH_SHORT).show();
                if (customProgressDialog != null)
                    customProgressDialog.dismiss();
            }
        });
    }


    /**
     * GCM registration code
     */
    private void initGcmReq() {
        if (!TextUtils.isEmpty(sharedPrefs.getGcmRegId())) {
            if (sharedPrefs.isGcmRegIdSavedInServer()) {
            } else {
                if (customProgressDialog != null)
                    customProgressDialog.show();
                Map<String, String> params = new HashMap<String, String>();
                params.put("deviceid", Constants.getImeiNum(ProfileCompletionActivity.this));
                params.put("uniqueid",sharedPrefs.getUserKey());
                params.put("registrationid", sharedPrefs.getGcmRegId());
                AppController.getInstance().addToRequestQueue(requestCustomGcmReg(Frs_Constants.GCM_REG_URL, params), TAG_JSON_REG);
            }
        } else {
            if (checkPlayServices()) {
                registerInBackground();
            }
        }
    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (customProgressDialog != null)
                    customProgressDialog.show();
            }

            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcmObj == null) {
                        gcmObj = GoogleCloudMessaging.getInstance(ProfileCompletionActivity.this);
                    }
                    regId = gcmObj.register(Constants.GOOGLE_PROJ_ID);
                    msg = "Registration ID :" + regId;

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                if (customProgressDialog != null)
                    customProgressDialog.dismiss();
                if (!TextUtils.isEmpty(regId)) {
                    sharedPrefs.setGcmRegId(regId);
                    if (customProgressDialog != null)
                        customProgressDialog.show();
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("deviceid", Constants.getImeiNum(ProfileCompletionActivity.this));
                    params.put("uniqueid",sharedPrefs.getUserKey());
                    params.put("registrationid", sharedPrefs.getGcmRegId());
                    AppController.getInstance().addToRequestQueue(requestCustomGcmReg(Frs_Constants.GCM_REG_URL, params), TAG_JSON_REG);
                } else {
                    Toast.makeText(ProfileCompletionActivity.this, "Reg ID Creation Failed." + msg, Toast.LENGTH_LONG).show();
                }
            }
        }.execute(null, null, null);
    }

    private CustomJsonObjectRequest requestCustomGcmReg(String url, Map<String, String> params) {
        return new CustomJsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (customProgressDialog != null)
                    customProgressDialog.dismiss();
                sharedPrefs.setGcmRegIdSavedInServer(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (customProgressDialog != null)
                    customProgressDialog.dismiss();
                Toast.makeText(
                        ProfileCompletionActivity.this,
                        "Unexpected Error occcured! [Most common Error: Device might "
                                + "not be connected to Internet or remote server is not up and running], check for other errors as well",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(ProfileCompletionActivity.this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, ProfileCompletionActivity.this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(
                        ProfileCompletionActivity.this,
                        "This device doesn't support Play services, App will not work normally",
                        Toast.LENGTH_LONG).show();
            }
            return false;
        } else {
        }
        return true;
    }

    private void alertNetwork() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ProfileCompletionActivity.this, R.style.DevenDialogTheme);
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
}
