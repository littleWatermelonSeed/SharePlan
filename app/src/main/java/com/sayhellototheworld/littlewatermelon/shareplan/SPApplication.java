package com.sayhellototheworld.littlewatermelon.shareplan;

import android.app.Application;
import android.content.Context;

import com.sayhellototheworld.littlewatermelon.shareplan.model.local_file.MySharedPreferences;

import org.litepal.LitePal;
import org.litepal.tablemanager.Connector;

import cn.bmob.v3.Bmob;

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
        initBmob();
        initLitepal();
        initAutoLayout();
    }

    private void initAutoLayout(){
//        AutoLayoutConifg.getInstance().useDeviceSize();
    }

    private void initBmob(){
        Bmob.initialize(this,"a4fd16189837834c3b7f0f9faa0f519e");
    }

    private void initLitepal(){
        LitePal.initialize(this);
        if (!MySharedPreferences.getInstance().getBooleanMessage(MySharedPreferences.KEY_FIRST_LOGIN)){
            Connector.getDatabase();
            MySharedPreferences.getInstance().saveMessage(MySharedPreferences.KEY_FIRST_LOGIN,true);
        }
        Connector.getDatabase();
    }

    public static Context getAppContext(){
        return appContext;
    }

}
