package com.mtechno.freerechargeswipe.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
 * Created by DEVEN SINGH on 5/4/2015.
 */
public class RedeemFragment extends Fragment implements View.OnClickListener {

    TextView remainingBalance;
    RadioGroup radioGroup;
    RadioButton selfRadio;
    RadioButton othersRadio;
    EditText redeemNumber;
    Button redeemButton;
    TextView selectOperator;
    TextView selectAmount;
    SharedPrefs sharedPrefs;
    CustomProgressDialog customProgressDialog;
    String TAG_REDEEM = "requestRedeem";
    String[] amount = {"50", "100"};
    String[] operator = {"Airtel", "BSNL", "MTNL", "Reliance", "Idea", "Vodafone", "Aircel", "Tata Docomo", "Uninor", "MTS", "Videocon", "Loop Mobile"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_redeem, container, false);
        initComponents(rootView);
        return rootView;
    }

    private void initComponents(View rootView) {
        sharedPrefs = new SharedPrefs(getActivity());
        customProgressDialog = new CustomProgressDialog(getActivity(), R.mipmap.loading);
        radioGroup = (RadioGroup) rootView.findViewById(R.id.radioGroup);
        selfRadio = (RadioButton) rootView.findViewById(R.id.radioSelf);
        othersRadio = (RadioButton) rootView.findViewById(R.id.radioOthers);
        redeemNumber = (EditText) rootView.findViewById(R.id.redeemMobileNum);
        redeemButton = (Button) rootView.findViewById(R.id.redeemButton);
        selectOperator = (TextView) rootView.findViewById(R.id.select_operator);
        selectAmount = (TextView) rootView.findViewById(R.id.select_ammount);
        remainingBalance= (TextView) rootView.findViewById(R.id.remainingBalanceRedeem);
        radioGroup.setOnCheckedChangeListener(radioGroupCheckedListner);
        redeemNumber.setText(sharedPrefs.getMobileNumber());
        redeemNumber.setEnabled(false);
        redeemNumber.setTextColor(Color.BLACK);
        redeemButton.setOnClickListener(this);
        selectAmount.setOnClickListener(this);
        selectOperator.setOnClickListener(this);
        remainingBalance.setText("Your remaining balance is "+sharedPrefs.getFrsCredits()+" Credits = "+(((float)sharedPrefs.getFrsCredits())/100)+" Rs.");
    }


    private void rechargeMobileWithRedeemAmount() {
        if (redeemNumber.getText().toString().equals("") || redeemNumber.getText().toString().length() < 10 || selectOperator.getText().toString().trim().contains("Select Operator") || selectAmount.getText().toString().trim().contains("Select Amount")) {
            Toast.makeText(getActivity(), "Please fill details correctly", Toast.LENGTH_LONG).show();
        } else if (Integer.parseInt(selectAmount.getText().toString().trim()) > sharedPrefs.getFrsCredits()/100) {
            Toast.makeText(getActivity(), "You don't have enough FRS credits.", Toast.LENGTH_LONG).show();
        } else {
            verifyPopUp(redeemNumber.getText().toString().trim(), Integer.parseInt(selectAmount.getText().toString().trim()), selectOperator.getText().toString());

        }
    }

    private void verifyPopUp(final String num, final int amnt, final String opertor) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.DevenDialogTheme);
        alertDialogBuilder.setTitle("Redeem ?");
        alertDialogBuilder
                .setMessage("Mobile Number: "+num+"\n"+"Amount: "+amnt+"\n"+"Operator: "+opertor)
                .setCancelable(true)
                .setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                sharedPrefs.setFrsCredits(sharedPrefs.getFrsCredits() - amnt * 100);
                                remainingBalance.setText("Your remaining balance is " + sharedPrefs.getFrsCredits() + " Credits = " + (((float) sharedPrefs.getFrsCredits()) / 100) + " Rs.");
                                AppController.getInstance().cancelPendingRequests(TAG_REDEEM);
                                requestForRedeemAmount(num, String.valueOf(amnt*100), opertor);
                            }
                        })
        .setNegativeButton("Decline", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
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

    RadioGroup.OnCheckedChangeListener radioGroupCheckedListner = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.radioSelf:
                    redeemNumber.setEnabled(false);
                    redeemNumber.setText(sharedPrefs.getMobileNumber().toString().trim());
                    redeemNumber.setTextColor(Color.BLACK);
                    break;
                case R.id.radioOthers:
                    redeemNumber.setEnabled(true);
                    redeemNumber.setText("");
                    break;
            }
        }
    };

    private void requestForRedeemAmount(String rechargeMobile, String amount, String operator) {
        if (new ConnectionCheck(getActivity()).isConnectionAvailable()) {
            customProgressDialog.show();
            Map<String, String> params = new HashMap<String, String>();
            params.put("deviceid", Constants.getImeiNum(getActivity()));
            params.put("uniqueid",sharedPrefs.getUserKey());
            params.put("rechargemobile", rechargeMobile);
            params.put("redeemamount", "-" + amount);
            params.put("operatorr", operator);
            AppController.getInstance().addToRequestQueue(requestRedeemMoney(Frs_Constants.REDEEM_CREDIT, params), TAG_REDEEM);
        } else {
            alertNetwork();
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

    private CustomJsonObjectRequest requestRedeemMoney(String url, Map<String, String> params) {
        return new CustomJsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (customProgressDialog != null)
                    customProgressDialog.dismiss();
                try {
                    sharedPrefs.setFrsCredits(Integer.parseInt(response.getString("totalmoney").toString().trim()));
                    remainingBalance.setText("Your remaining balance is "+sharedPrefs.getFrsCredits()+" Credits = "+(((float)sharedPrefs.getFrsCredits())/100)+" Rs.");
                    alertPopUp(response.getString("totalmoney"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Error! your request can't be process right now, please try again later.", Toast.LENGTH_SHORT).show();
                if (customProgressDialog != null)
                    customProgressDialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == redeemButton) {
            rechargeMobileWithRedeemAmount();
        }
        if (v == selectAmount) {
            setRedeemValues("Select Amount", R.array.amount, "amount");
        }
        if (v == selectOperator) {
            setRedeemValues("Select Operator", R.array.operator, "operator");
        }
    }

    private void setRedeemValues(String tittle, final int array, final String fromWhich) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DevenDialogTheme);
        builder.setTitle(tittle)
                .setItems(array, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (fromWhich == "amount") {
                            selectAmount.setText(amount[which]);
                        }
                        if (fromWhich == "operator") {
                            selectOperator.setText(operator[which]);
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

    private void alertPopUp(final String balance) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.DevenDialogTheme);
        alertDialogBuilder.setTitle("Offer");
        alertDialogBuilder
                .setMessage("Your recharge will be done within 48 hours. In case any delay please contact support team with your registered email ID." + "\nYour remaining credits are: "+balance)
                .setCancelable(true)
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
