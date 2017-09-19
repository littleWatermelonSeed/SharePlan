package com.sayhellototheworld.littlewatermelon.shareplan.model.data_manage.real_data;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.sayhellototheworld.littlewatermelon.shareplan.SPApplication;
import com.sayhellototheworld.littlewatermelon.shareplan.customwidget.DialogConfirm;
import com.sayhellototheworld.littlewatermelon.shareplan.model.data_manage.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.shareplan.model.data_manage.data.ManageUser;
import com.sayhellototheworld.littlewatermelon.shareplan.presenter.centerplaza.ControlUserFragment;
import com.sayhellototheworld.littlewatermelon.shareplan.view.base_activity.MyActivityManager;
import com.sayhellototheworld.littlewatermelon.shareplan.view.user_view.ForgetPasswordActivity;

import org.json.JSONObject;

import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.listener.ValueEventListener;

/**
 * Created by 123 on 2017/9/19.
 */

public class RealTimeData {

    private static RealTimeData mRealTimeData;
    private static BmobRealTimeData dataUserLogin;
    private static Context mContext;

    public static RealTimeData getInstance() {
        if (mRealTimeData == null) {
            mRealTimeData = new RealTimeData();
        }
        return mRealTimeData;
    }

    private RealTimeData() {
        mContext = SPApplication.getAppContext();
        dataUserLogin = new BmobRealTimeData();
    }

    public void subUserLoginDeviceId(final MyUserBean currentUser) {
        dataUserLogin.start(new ValueEventListener() {
            @Override
            public void onConnectCompleted(Exception e) {
                if (dataUserLogin.isConnected()) {
                    dataUserLogin.subRowUpdate("_User", currentUser.getObjectId());
                }
            }

            @Override
            public void onDataChange(JSONObject jsonObject) {
                JSONObject data = jsonObject.optJSONObject("data");
                if (!currentUser.getLoginDeviceId().equals(data.optString("loginDeviceId"))){
                    userLoginChange();
                    Log.i("niyuanjie","登录手机变化 新ID为：" + data.optString("loginDeviceId"));
                }else {
                    Log.i("niyuanjie","登录手机没变化变化");
                }
            }
        });
    }

    public void unsubUserLoginDeviceId(final MyUserBean currentUser) {
        if (dataUserLogin != null && dataUserLogin.isConnected()) {
            dataUserLogin.unsubRowUpdate("_User", currentUser.getObjectId());
        }
    }

    private void userLoginChange(){
        final MyActivityManager activityManager = MyActivityManager.getDestoryed();
        final Activity topActivity = activityManager.getTopActivity();
        String activityName = topActivity.getClass().getSimpleName();
        boolean topUserActivity = false;
        if (activityManager.isExistInUserMap(activityName)){
            topUserActivity = true;
            Log.i("niyuanjie","top activity存在于userMap");
        }else {
            topUserActivity = false;
            Log.i("niyuanjie","top activity不存在于userMap");
        }
        DialogConfirm.newInstance("警告", "你的账号被异地登录,将关闭所有用户相关界面,如不是本人操作建议修改密码,是否修改密码?", new DialogConfirm.CancleAndOkDo() {
            @Override
            public void cancle() {
                ManageUser.loginOutUser();
                ControlUserFragment.syncUserFragment();
                activityManager.clearUserMap();
            }

            @Override
            public void ok() {
                ManageUser.loginOutUser();
                ForgetPasswordActivity.startLoginActivity(topActivity);
                ControlUserFragment.syncUserFragment();
                activityManager.clearUserMap();
            }
        }).setMargin(60)
                .setOutCancel(false)
                .show(((FragmentActivity)topActivity).getSupportFragmentManager());
    }

}
