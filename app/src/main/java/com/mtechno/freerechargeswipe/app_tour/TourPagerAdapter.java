package com.mtechno.freerechargeswipe.app_tour;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TourPagerAdapter extends FragmentPagerAdapter {

    private int PAGE_NUM_ITEMS = 5;

    public TourPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AppTourFragmentFirst();
            case 1:
                return new AppTourFragmentSecond();
            case 2:
                return new AppTourFragmentThird();
            case 3:
                return new AppTourFragmentFourth();
            case 4:
                return new AppTourFragmentFifth();
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }

    @Override
    public int getCount() {
        return PAGE_NUM_ITEMS;
    }
}