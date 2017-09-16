package com.sayhellototheworld.littlewatermelon.shareplan.util;

import android.util.DisplayMetrics;

import com.sayhellototheworld.littlewatermelon.shareplan.SPApplication;

/**
 * Created by 123 on 2017/9/12.
 */

public class SysUtil {

    public static int getDisplayHeight(){
        DisplayMetrics dm = SPApplication.getAppContext().getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    public static int getDisplayWidth(){
        DisplayMetrics dm = SPApplication.getAppContext().getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

}
