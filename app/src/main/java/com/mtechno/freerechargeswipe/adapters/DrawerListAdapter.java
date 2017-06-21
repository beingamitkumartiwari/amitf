package com.mtechno.freerechargeswipe.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mtechno.freerechargeswipe.R;
import com.mtechno.freerechargeswipe.pojos.DrawerItem;

import java.util.ArrayList;


/**
 * Created by DEVEN SINGH on 4/21/2015.
 */
public class DrawerListAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<DrawerItem> drawerItems;

    public DrawerListAdapter(Context context, ArrayList<DrawerItem> drawerItems){
        this.context = context;
        this.drawerItems = drawerItems;
    }

    @Override
    public int getCount() {
        return drawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return drawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
        }

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);

        imgIcon.setImageResource(drawerItems.get(position).getIcon());
        txtTitle.setText(drawerItems.get(position).getTitle());
        return convertView;
    }

}
