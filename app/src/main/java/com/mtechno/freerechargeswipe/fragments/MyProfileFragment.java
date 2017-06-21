package com.mtechno.freerechargeswipe.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mtechno.freerechargeswipe.R;
import com.mtechno.freerechargeswipe.extras.Constants;
import com.mtechno.freerechargeswipe.extras.Frs_Constants;
import com.mtechno.freerechargeswipe.utils.CustomProgressDialog;
import com.mtechno.freerechargeswipe.utils.SharedPrefs;
import com.mtechno.freerechargeswipe.volley_works.AppController;
import com.mtechno.freerechargeswipe.volley_works.ConnectionCheck;
import com.mtechno.freerechargeswipe.volley_works.CustomJsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DEVEN SINGH on 4/21/2015.
 */
public class MyProfileFragment extends Fragment implements View.OnClickListener {

    TextView userMob;
    TextView userDob;
    TextView userGender;
    TextView userOccupation;
    TextView userIncome;
    TextView userMarriageStatus;
    TextView submitDetails;
    String[] gender = {"Male", "Female"};
    String[] occupation = {"Student", "Employed", "Unemployed", "Housewife", "Retired", "Self Employed"};
    String[] income = {"Nil", "Less than 1 LPA", "Less than 3 LPA", "Less than 5 LPA", "More than 5 LPA"};
    String[] mStatus = {"Single", "Married"};
    String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    String TAG_JSON = "requestProfile";
    String TAG_JSON1 = "requestProfileUpdate";
    SharedPrefs sharedPrefs;
    CustomProgressDialog customProgressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_myprofile, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponents(view);
        setUsersProfile();
    }

    private void setUsersProfile() {
//        if (new ConnectionCheck(getActivity()).isConnectionAvailable()) {
//            customProgressDialog.show();
//            Map<String, String> params = new HashMap<String, String>();
//            params.put("mobile", sharedPrefs.getMobileNumber());
//            AppController.getInstance().addToRequestQueue(requestProfile(Constants.FRS_URLs.PROFILE, params), TAG_JSON);
//        } else {
//            alertNetwork();
//        }
        userDob.setText(sharedPrefs.getUserDob());
        userGender.setText(sharedPrefs.getUserGender());
        userOccupation.setText(sharedPrefs.getUserOccupation());
        userIncome.setText(sharedPrefs.getUserIncome());
        userMarriageStatus.setText(sharedPrefs.getUserMaritalStatus());
    }

    private void initComponents(View rootView) {
        customProgressDialog = new CustomProgressDialog(getActivity(), R.mipmap.loading);
        sharedPrefs = new SharedPrefs(getActivity());
        userMob = (TextView) rootView.findViewById(R.id.userMobNum);
        userDob = (TextView) rootView.findViewById(R.id.user_dob);
        userGender = (TextView) rootView.findViewById(R.id.user_gender);
        userOccupation = (TextView) rootView.findViewById(R.id.user_occupation);
        userIncome = (TextView) rootView.findViewById(R.id.user_income);
        userMarriageStatus = (TextView) rootView.findViewById(R.id.user_marital_status);
        submitDetails = (TextView) rootView.findViewById(R.id.user_details_submit_bt);
        userDob.setOnClickListener(this);
        userGender.setOnClickListener(this);
        userOccupation.setOnClickListener(this);
        userIncome.setOnClickListener(this);
        userMarriageStatus.setOnClickListener(this);
        submitDetails.setOnClickListener(this);
        userMob.setText(sharedPrefs.getUserName()+"\n" + sharedPrefs.getMobileNumber());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_dob:
                setUserDob();
                break;
            case R.id.user_gender:
                setProfileValues(R.string.select_gender, R.array.gender, "Gender");
                break;
            case R.id.user_occupation:
                setProfileValues(R.string.select_occupation, R.array.occupation, "Occupation");
                break;
            case R.id.user_income:
                setProfileValues(R.string.select_income, R.array.income, "Income");
                break;
            case R.id.user_marital_status:
                setProfileValues(R.string.select_m_status, R.array.m_status, "Mstatus");
                break;
            case R.id.user_details_submit_bt:
                updateProfile();
                break;
        }
    }

    private void updateProfile() {
        if (new ConnectionCheck(getActivity()).isConnectionAvailable()) {
            customProgressDialog.show();
            Map<String, String> params = new HashMap<String, String>();
            params.put("deviceid", Constants.getImeiNum(getActivity()));
            params.put("uniqueid",sharedPrefs.getUserKey());
            params.put("dob", userDob.getText().toString());
            params.put("gender", userGender.getText().toString());
            params.put("occupation", userOccupation.getText().toString());
            params.put("income", userIncome.getText().toString());
            params.put("maritalstatus", userMarriageStatus.getText().toString());
            AppController.getInstance().addToRequestQueue(requestUpdateProfile(Frs_Constants.UPDATE_PROFILE, params), TAG_JSON1);
        } else {
            alertNetwork();
        }
    }

    private void setProfileValues(int tittle, final int array, final String fromWhich) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DevenDialogTheme);
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
                            userMarriageStatus.setText(mStatus[which]);
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
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

    private CustomJsonObjectRequest requestProfile(String url, Map<String, String> params) {
        return new CustomJsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (customProgressDialog != null)
                    customProgressDialog.dismiss();
                try {
                    userDob.setText(response.getString("dob"));
                    userGender.setText(response.getString("gender"));
                    userOccupation.setText(response.getString("occupation"));
                    userIncome.setText(response.getString("income"));
                    userMarriageStatus.setText(response.getString("maritalstatus"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Error! try again later.", Toast.LENGTH_SHORT).show();
                if (customProgressDialog != null)
                    customProgressDialog.dismiss();
            }
        });
    }

    private CustomJsonObjectRequest requestUpdateProfile(String url, Map<String, String> params) {
        return new CustomJsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (customProgressDialog != null)
                    customProgressDialog.dismiss();

                Toast.makeText(getActivity(), "profile updated successfully.", Toast.LENGTH_SHORT).show();
                sharedPrefs.setUserDob(userDob.getText().toString());
                sharedPrefs.setUserGender(userGender.getText().toString());
                sharedPrefs.setUserOccupation(userOccupation.getText().toString());
                sharedPrefs.setUserIncome(userIncome.getText().toString());
                sharedPrefs.setUserMaritalStatus(userMarriageStatus.getText().toString());
//                try {
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Error! try again later.", Toast.LENGTH_SHORT).show();
                if (customProgressDialog != null)
                    customProgressDialog.dismiss();
            }
        });
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

}
