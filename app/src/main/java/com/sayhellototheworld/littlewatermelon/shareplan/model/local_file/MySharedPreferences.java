package com.sayhellototheworld.littlewatermelon.shareplan.model.local_file;

import android.content.Context;
import android.content.SharedPreferences;

import com.sayhellototheworld.littlewatermelon.shareplan.SPApplication;

/**
 * Created by 123 on 2017/9/10.
 */

public class MySharedPreferences {

    public final static String KEY_USER_ID = "userID";
    public final static String KEY_USER_PASSWORD = "userPS";

    public final static int TYPE_USER_ID = 0;
    public final static int TYPE_USER_PASSWORD = 1;

    private Context mContext;

    private SharedPreferences mSharedPreferences;
    private static MySharedPreferences mPreferences;

    public static MySharedPreferences getInstance(){
        if(mPreferences == null){
            mPreferences = new MySharedPreferences();
        }
        return mPreferences;
    }

    private MySharedPreferences() {
        mContext = SPApplication.getAppContext();
        mSharedPreferences = mContext.getSharedPreferences("MySharedPreferences",Context.MODE_PRIVATE);
    }

    public String getMessage(int msgType){
        String message = "";
        switch (msgType){
            case TYPE_USER_ID:
                message = mSharedPreferences.getString(KEY_USER_ID,"");
                break;
            case TYPE_USER_PASSWORD:
                message = mSharedPreferences.getString(KEY_USER_PASSWORD,"");
                break;
        }
        return message;
    }

    public String getMessage(String msgKey){
        return mSharedPreferences.getString(msgKey,"");
    }

    public void saveMessage(String message,int msgType){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        switch (msgType){
            case TYPE_USER_ID:
                editor.putString(KEY_USER_ID,message);
                break;
            case TYPE_USER_PASSWORD:
                editor.putString(KEY_USER_PASSWORD,message);
                break;
        }
        editor.commit();
    }

    public void saveMessage(String msgKey,String message){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(msgKey,message);
        editor.commit();
    }

}
