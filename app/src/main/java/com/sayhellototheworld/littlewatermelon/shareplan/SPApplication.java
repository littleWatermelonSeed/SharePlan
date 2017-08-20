package com.sayhellototheworld.littlewatermelon.shareplan;

import android.app.Application;
import android.content.Context;

/**
 * Created by 123 on 2017/8/20.
 */

public class SPApplication extends Application {

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init(){
        appContext = getApplicationContext();
    }

    public static Context getAppContext(){
        return appContext;
    }

}
