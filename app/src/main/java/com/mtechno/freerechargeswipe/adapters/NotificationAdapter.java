package com.mtechno.freerechargeswipe.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mtechno.freerechargeswipe.R;
import com.mtechno.freerechargeswipe.pojos.NotificationItem;

import java.util.ArrayList;

/**
 * Created by DEVEN SINGH on 6/4/2015.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationRowHolder> {

    private ArrayList<NotificationItem> notificationItemArrayList;
    private Context context;

    public NotificationAdapter(Context context, ArrayList<NotificationItem> notificationItemArrayList) {
        this.context = context;
        this.notificationItemArrayList = notificationItemArrayList;
    }

    @Override
    public NotificationRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_row, parent, false);
        NotificationRowHolder notificationRowHolder = new NotificationRowHolder(view);
        return notificationRowHolder;
    }

    @Override
    public void onBindViewHolder(NotificationRowHolder holder, int position) {
        NotificationItem notificationItem=notificationItemArrayList.get(position);
        holder.notificationMsg.setText(notificationItem.getMessage());
        if(notificationItem.getMsgReadStatus().equals("unread")){
            holder.unreadStatus.setVisibility(View.VISIBLE);
        }else{
            holder.unreadStatus.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return (null != notificationItemArrayList ? notificationItemArrayList.size() : 0);
    }
}
