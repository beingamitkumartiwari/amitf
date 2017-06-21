package com.mtechno.freerechargeswipe.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mtechno.freerechargeswipe.R;
import com.mtechno.freerechargeswipe.adapters.DrawerListAdapter;
import com.mtechno.freerechargeswipe.fragments.AccountSummaryFragment;
import com.mtechno.freerechargeswipe.fragments.HomeFragment;
import com.mtechno.freerechargeswipe.fragments.MyProfileFragment;
import com.mtechno.freerechargeswipe.fragments.NotificationFragment;
import com.mtechno.freerechargeswipe.fragments.OffersFragment;
import com.mtechno.freerechargeswipe.fragments.RedeemFragment;
import com.mtechno.freerechargeswipe.fragments.SettingFragment;
import com.mtechno.freerechargeswipe.pojos.DrawerItem;
import com.mtechno.freerechargeswipe.utils.FrsDatabase;
import com.mtechno.freerechargeswipe.utils.GcmNotificationObserver;
import com.mtechno.freerechargeswipe.utils.SharedPrefs;
import com.mtechno.freerechargeswipe.utils.UserAvatar;
import com.mtechno.freerechargeswipe.utils.UserProfileAvatar;
import com.startapp.android.publish.StartAppSDK;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by DEVEN SINGH on 1/13/2015.
 */

public class MainActivity extends ActionBarActivity implements GcmNotificationObserver {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] menuTitles;
    private TypedArray menuIcons;

    private ArrayList<DrawerItem> drawerItems;
    private DrawerListAdapter adapter;
    Fragment fragment;
    FragmentManager fragmentManager;
    FrsDatabase frsDatabase;
    private RelativeLayout rlSideDrawer;
    private UserAvatar userAvatar;
    private TextView userName;
    private TextView userEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StartAppSDK.init(this, new SharedPrefs(this).getStartappId1(), new SharedPrefs(this).getStartappId2(), false);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        initViews();
        mTitle = mDrawerTitle = getTitle();
        menuTitles = getResources().getStringArray(R.array.drawer_items);
        menuIcons = getResources()
                .obtainTypedArray(R.array.drawer_icons);
        drawerItems = new ArrayList<DrawerItem>();
        drawerItems.add(new DrawerItem(menuTitles[0], menuIcons.getResourceId(0, -1)));
        drawerItems.add(new DrawerItem(menuTitles[1], menuIcons.getResourceId(1, -1)));
        drawerItems.add(new DrawerItem(menuTitles[2], menuIcons.getResourceId(2, -1)));
        drawerItems.add(new DrawerItem(menuTitles[3], menuIcons.getResourceId(3, -1)));
        drawerItems.add(new DrawerItem(menuTitles[4], menuIcons.getResourceId(4, -1)));
        drawerItems.add(new DrawerItem(menuTitles[5], menuIcons.getResourceId(5, -1)));
        drawerItems.add(new DrawerItem(menuTitles[6], menuIcons.getResourceId(6, -1)));
        drawerItems.add(new DrawerItem(menuTitles[7], menuIcons.getResourceId(7, -1)));
        drawerItems.add(new DrawerItem(menuTitles[8], menuIcons.getResourceId(8, -1)));
//        drawerItems.add(new DrawerItem(menuTitles[9], menuIcons.getResourceId(9, -1)));
        menuIcons.recycle();
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
        adapter = new DrawerListAdapter(getApplicationContext(),
                drawerItems);
        mDrawerList.setAdapter(adapter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, R.string.app_name, R.string.app_name) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                supportInvalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            displayView(0);
        }

        frsDatabase = new FrsDatabase(MainActivity.this);
        invalidateOptionsMenu();

    }

    private void initViews() {
        SharedPrefs sharedPrefs=new SharedPrefs(MainActivity.this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.slider_menu_list);
        rlSideDrawer= (RelativeLayout) findViewById(R.id.sideDrawer);
        userAvatar= (UserAvatar) findViewById(R.id.imgAvatar);
        userName= (TextView) findViewById(R.id.txtUsername);
        userEmail= (TextView) findViewById(R.id.txtUserEmail);
        userName.setText(sharedPrefs.getUserName());
        userEmail.setText(sharedPrefs.getUserEmail());
        userAvatar.setImageBitmap(new UserProfileAvatar(MainActivity.this).getUserAvatar());

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();
        if (id == R.id.action_home) {
            displayView(0);
        }
        if (id == R.id.action_notification) {
            displayView(4);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(rlSideDrawer);
        menu.findItem(R.id.action_home).setVisible(!drawerOpen);
        menu.findItem(R.id.action_notification).setVisible(!drawerOpen);
        if (frsDatabase != null)
            setActionIcon(frsDatabase.unreadMessagesCount(), menu);
        return super.onPrepareOptionsMenu(menu);
    }

    private void setActionIcon(int unreadCount, Menu mMenu) {
        MenuItem item = mMenu.findItem(R.id.action_notification);

        if (mMenu != null) {
            switch (unreadCount) {
                case 1:
                    item.setIcon(R.mipmap.ic_notification1);
                    break;
                case 2:
                    item.setIcon(R.mipmap.ic_notification2);
                    break;
                case 3:
                    item.setIcon(R.mipmap.ic_notification3);
                    break;
                case 4:
                    item.setIcon(R.mipmap.ic_notification4);
                    break;
                case 5:
                    item.setIcon(R.mipmap.ic_notification5);
                    break;
                default:
                    item.setIcon(R.mipmap.ic_notification);
                    break;
            }
        }
    }


    private void displayView(int position) {

        switch (position) {
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:
//                fragment = new OffersFragment();
                fragment = new RedeemFragment();
                break;
            case 2:
                fragment = new AccountSummaryFragment();
                break;
            case 3:
                fragment = new MyProfileFragment();
                break;
            case 4:
                fragment = new NotificationFragment();
                break;
            case 5:
                fragment = new SettingFragment();
                break;
//            case 6:
//                break;
            default:
                break;
        }

        if (fragment != null) {
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(menuTitles[position]);
            mDrawerLayout.closeDrawer(rlSideDrawer);
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment).commit();
        } else {
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onRecievedGcmNotification() {
        invalidateOptionsMenu();
    }

    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            if (position == 7) {
                mDrawerLayout.closeDrawer(rlSideDrawer);
                goToPageFaq("http://mtechnovation.com/rechargeTC.aspx");
            } else if (position == 8) {
                mDrawerLayout.closeDrawer(rlSideDrawer);
                goToPageFaq("http://mtechnovation.com/freerechargepp.aspx");
            } else if (position == 9) {
                mDrawerLayout.closeDrawer(rlSideDrawer);
                alertExit();
            } else {
                displayView(position);
            }
        }
    }

    private void goToPageFaq(String url) {
        Intent intentPage = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intentPage);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(rlSideDrawer))
            mDrawerLayout.closeDrawer(rlSideDrawer);
        else
            alertExit();
    }

    private void alertExit() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.DevenDialogTheme);
        alertDialogBuilder.setTitle("Free Recharge Swipe");
        alertDialogBuilder
                .setMessage("Do you want to exit ?")
                .setCancelable(false)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                finish();
                            }
                        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
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
}
