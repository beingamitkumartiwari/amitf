package com.mtechno.freerechargeswipe.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.mtechno.freerechargeswipe.R;
import com.mtechno.freerechargeswipe.minimobworks.CustomViewHolder;
import com.mtechno.freerechargeswipe.minimobworks.OnListItemClickListner;
import com.mtechno.freerechargeswipe.pojos.Pojo_availableoffer;

import java.util.ArrayList;

/**
 * Created by Chandra on 25/06/2015.
 */
public class MinimobAdAdapter extends BaseAdapter {
    Context context;
    ArrayList<Pojo_availableoffer> list_availableoffers;
    CustomViewHolder holder;
    OnListItemClickListner onListItemClickListner;
    ImageLoader imageLoader;

    public MinimobAdAdapter(Context context, ArrayList<Pojo_availableoffer> list_availableoffers, OnListItemClickListner onListItemClickListner, ImageLoader imageLoader) {
        this.context = context;
        this.list_availableoffers = list_availableoffers;
        this.onListItemClickListner = onListItemClickListner;
        this.imageLoader=imageLoader;
    }

    @Override
    public int getCount() {
        return list_availableoffers.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.offer_row, null);
            holder = new CustomViewHolder();
            holder.tv_appname = (TextView) convertView.findViewById(R.id.textview_appname);
            holder.tv_payout = (TextView) convertView.findViewById(R.id.textview_payout);
            holder.icon = (NetworkImageView) convertView.findViewById(R.id.imageview_icon);
            holder.bt_install = (Button) convertView.findViewById(R.id.button_install);

            convertView.setTag(holder);
        } else {
            holder = (CustomViewHolder) convertView.getTag();
        }
        final Pojo_availableoffer pojo = list_availableoffers.get(position);
        holder.tv_appname.setText(pojo.getAppName());
        holder.tv_payout.setText(pojo.getPayOut() + " Credits");

        holder.icon.setImageUrl(pojo.getImageUrl(),imageLoader);
        holder.bt_install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListItemClickListner.setOnListItemClickListner_minimob(position, pojo.getPlayStoreLink());
            }
        });
        return convertView;
    }
}
