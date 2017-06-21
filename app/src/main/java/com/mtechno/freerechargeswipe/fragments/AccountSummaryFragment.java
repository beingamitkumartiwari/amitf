package com.mtechno.freerechargeswipe.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DEVEN SINGH on 4/21/2015.
 */
public class AccountSummaryFragment extends Fragment {

    String TAG_JSON = "accountSummary";
    TextView summaryOffer;
    TextView summaryAdClick;
    TextView summaryRedeem;
    TextView summaryTotalBalance;
    SharedPrefs sharedPrefs;
    CustomProgressDialog customProgressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account_summary, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponents(view);
    }

    private void initComponents(View rootView) {
        customProgressDialog = new CustomProgressDialog(getActivity(), R.mipmap.loading);
        sharedPrefs = new SharedPrefs(getActivity());
        summaryOffer = (TextView) rootView.findViewById(R.id.summaryOffer);
        summaryAdClick = (TextView) rootView.findViewById(R.id.summaryAdClick);
        summaryRedeem = (TextView) rootView.findViewById(R.id.summaryRedeem);
        summaryTotalBalance = (TextView) rootView.findViewById(R.id.summaryTotalBalance);
        requestForAccountSummary();
    }

    private void requestForAccountSummary() {
        if (new ConnectionCheck(getActivity()).isConnectionAvailable()) {
            customProgressDialog.show();
            Map<String, String> params = new HashMap<String, String>();
            params.put("deviceid", Constants.getImeiNum(getActivity()));
            params.put("uniqueid",sharedPrefs.getUserKey());
            AppController.getInstance().addToRequestQueue(requestAccountSummary(Frs_Constants.ACCOUNT_SUMMARY, params), TAG_JSON);
        } else {
            alertNetwork();
        }
    }

    private CustomJsonObjectRequest requestAccountSummary(String url, Map<String, String> params) {
        return new CustomJsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (customProgressDialog != null)
                    customProgressDialog.dismiss();
                try {
                    summaryOffer.setText(response.getString("offer").toString().trim());
                    summaryAdClick.setText(response.getString("adclick").toString().trim());
                    summaryRedeem.setText(response.getString("redeem").toString().trim());
                    summaryTotalBalance.setText(response.getString("totalcredit").toString().trim());
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
