package com.mtechno.freerechargeswipe.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mtechno.freerechargeswipe.R;
import com.mtechno.freerechargeswipe.adapters.NotificationAdapter;
import com.mtechno.freerechargeswipe.utils.FrsDatabase;

/**
 * Created by DEVEN SINGH on 6/4/2015.
 */
public class NotificationFragment extends Fragment {

    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FrsDatabase frsDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notification, container, false);
         initComponents(rootView);
        return rootView;
    }

    private void initComponents(View rootView) {
        frsDatabase=new FrsDatabase(getActivity());
        recyclerView= (RecyclerView) rootView.findViewById(R.id.notification_recycler_view);
        mLayoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        notificationAdapter=new NotificationAdapter(getActivity(),frsDatabase.getNotificationData());
        recyclerView.setAdapter(notificationAdapter);
        frsDatabase.updateMsgReadStatus();
        getActivity().invalidateOptionsMenu();
    }
}
