package com.mtechno.freerechargeswipe.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mtechno.freerechargeswipe.R;

/**
 * Created by DEVEN SINGH on 6/4/2015.
 */
public class NotificationRowHolder extends RecyclerView.ViewHolder {

    protected TextView notificationMsg;
    protected ImageView unreadStatus;

    public NotificationRowHolder(View itemView) {
        super(itemView);
        notificationMsg= (TextView) itemView.findViewById(R.id.notificationMsg);
        unreadStatus= (ImageView) itemView.findViewById(R.id.unreadMsg);
    }
}
