package com.mtechno.freerechargeswipe.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.mtechno.freerechargeswipe.R;
import com.mtechno.freerechargeswipe.extras.Constants;
import com.mtechno.freerechargeswipe.extras.Frs_Constants;
import com.mtechno.freerechargeswipe.utils.CustomProgressDialog;
import com.mtechno.freerechargeswipe.volley_works.AppController;
import com.mtechno.freerechargeswipe.volley_works.CustomJsonObjectRequest;

import org.json.JSONObject;

/**
 * Created by DEVEN SINGH on 10/30/2015.
 */
public class Dfragment extends DialogFragment {

    NetworkImageView iv_campaign;
    TextView tv_appname;
    TextView tv_description;
    Button bt_earn;
    ImageView bt_close;
    String appname;
    String desciption;
    String imageurl;
    String playStoreLink;
    String payout;
    String campaignType;

    CustomProgressDialog customProgressDialog;

    @SuppressLint("ValidFragment")
    public Dfragment(){

    }
    @SuppressLint("ValidFragment")
    public Dfragment(final String appname, String desciption, String imageurl, final String playStoreLink, final String payout,final String campaignType){
          this.appname=appname;
          this.desciption=desciption;
          this.imageurl=imageurl;
          this.playStoreLink=playStoreLink;
          this.payout=payout;
          this.campaignType=campaignType;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.campaign_dialog, container, false);

        customProgressDialog = new CustomProgressDialog(getActivity(), R.mipmap.loading);
        iv_campaign = (NetworkImageView) rootView.findViewById(R.id.networkimage_campaignimage);
        tv_appname = (TextView) rootView.findViewById(R.id.textview_campainappname);
        tv_description = (TextView) rootView.findViewById(R.id.textview_campaigndesc);
        bt_earn = (Button) rootView.findViewById(R.id.button_earncampaign);
        bt_close = (ImageView) rootView.findViewById(R.id.button_campaignclose);
        iv_campaign.setImageUrl(imageurl, AppController.getInstance().getImageLoader());
        tv_appname.setText(appname);
        tv_description.setText(desciption);
        bt_earn.setText(payout);
        bt_earn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentTimeMillis = System.currentTimeMillis();
                String clickid = String.valueOf(currentTimeMillis);
                String subid2 = Constants.getImeiNum(getActivity()) + "mtech" + String.valueOf(currentTimeMillis) + "Appname" + "frs" + "Campaigntype" +campaignType;
                sendClickidForCampaign(clickid, subid2, playStoreLink);
                customProgressDialog.show();
            }
        });
        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
//
        return rootView;
    }

    private void sendClickidForCampaign(final String clickid, final String subid2, final String playStoreLink) {
        String url = Frs_Constants.SETIDSFORCAMPAIGN;
        url = url + "deviceid=" + Constants.getImeiNum(getActivity()) + "&appname=" + "frs" + "Campaigntype" + campaignType;
        CustomJsonObjectRequest customJsonObjectRequest = new CustomJsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (customProgressDialog != null)
                    customProgressDialog.dismiss();
                getDialog().dismiss();
                String link = playStoreLink + clickid + "/?sub_id2=" + subid2;
                Intent campaignApps = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(campaignApps);

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (customProgressDialog != null)
                    customProgressDialog.dismiss();
                Toast.makeText(getActivity(), "Error in loading offer.Try again", Toast.LENGTH_SHORT).show();
            }
        });
        customJsonObjectRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 4,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        AppController.getInstance().addToRequestQueue(customJsonObjectRequest);
    }
}
