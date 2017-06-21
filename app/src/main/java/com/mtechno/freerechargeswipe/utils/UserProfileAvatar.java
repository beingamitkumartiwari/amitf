package com.mtechno.freerechargeswipe.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * Created by DEVEN SINGH on 6/12/2015.
 */
public class UserProfileAvatar {

    Context context;

    public UserProfileAvatar(Context context){
        this.context=context;
    }

    public Bitmap getUserAvatar(){
        byte[] imageAsBytes = Base64.decode(new SharedPrefs(context).getUserAvatar().getBytes(), Base64.DEFAULT);
        return  BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }
}
