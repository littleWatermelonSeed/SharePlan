package com.sayhellototheworld.littlewatermelon.shareplan.util;

import android.widget.Toast;

import com.sayhellototheworld.littlewatermelon.shareplan.SPApplication;

/**
 * Created by 123 on 2017/8/21.
 */

public final class MyToastUtil {

    public final static int LONG = 0;
    public final static int SHORT = 1;

    public static void showToast(String showMessage){
        Toast.makeText(SPApplication.getAppContext(),showMessage,Toast.LENGTH_SHORT).show();
    }

    public static void showToast(String showMessage,int time){
        if(time == LONG){
            Toast.makeText(SPApplication.getAppContext(),showMessage,Toast.LENGTH_LONG).show();
        }else if(time == SHORT){
            Toast.makeText(SPApplication.getAppContext(),showMessage,Toast.LENGTH_SHORT).show();
        }
    }

}
