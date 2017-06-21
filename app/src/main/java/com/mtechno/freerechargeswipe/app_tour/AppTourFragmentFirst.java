package com.mtechno.freerechargeswipe.app_tour;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mtechno.freerechargeswipe.R;

/**
 * Created by DEVEN SINGH on 4/23/2015.
 */
public class AppTourFragmentFirst extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_apptour_first,container,false);
        return rootView;
    }
}
