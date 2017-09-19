package com.sayhellototheworld.littlewatermelon.shareplan;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.sayhellototheworld.littlewatermelon.shareplan.model.data_manage.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.shareplan.model.data_manage.data.ManageUser;
import com.sayhellototheworld.littlewatermelon.shareplan.model.local_file.MySharedPreferences;
import com.sayhellototheworld.littlewatermelon.shareplan.my_interface.EnterDo;
import com.sayhellototheworld.littlewatermelon.shareplan.my_interface.base_interface.BaseActivityDo;
import com.sayhellototheworld.littlewatermelon.shareplan.my_interface.userManage_interface.UserLoginDo;
import com.sayhellototheworld.littlewatermelon.shareplan.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.shareplan.util.NetWorkUtil;
import com.sayhellototheworld.littlewatermelon.shareplan.view.base_activity.BaseStatusActivity;
import com.sayhellototheworld.littlewatermelon.shareplan.view.centerplaza_view.CenterPlazaActivity;

import cn.bmob.v3.exception.BmobException;

public class EnterActivity extends BaseStatusActivity implements BaseActivityDo,EnterDo{

    private Button button;
    private Button button_test;
    private ManageUser mManageUser = new ManageUser(this);

    private boolean login = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        init();
    }

    @Override
    public void init() {
        button = (Button) findViewById(R.id.test);
        button.setClickable(false);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EnterActivity.this, CenterPlazaActivity.class));
            }
        });
        button_test = (Button) findViewById(R.id.test1);
        button_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EnterActivity.this, TestActivity.class));
            }
        });
        if (mManageUser.getCurrentUser() == null) {
            button.setClickable(true);
            login = false;
        } else {
            login = true;
        }
        initUser();
    }

    @Override
    public void initWidget() {

    }

    @Override
    public void initParam() {

    }

    @Override
    public void initShow() {

    }

    private void initUser() {
        if (!MySharedPreferences.getInstance().getBooleanMessage(MySharedPreferences.KEY_USER_LOGIN_STATUS)){
            return;
        }
        if (!NetWorkUtil.isNetworkAvailable(this)){
            Log.i("niyuanjie","没有网啦");
            return;
        }
        mManageUser.loginAndSyncUser(MySharedPreferences.getInstance().getStringMessage(MySharedPreferences.KEY_USER_ID),
                MySharedPreferences.getInstance().getStringMessage(MySharedPreferences.KEY_USER_PASSWORD),
                new UserLoginDo() {
                    @Override
                    public void loginSuccess(MyUserBean myUserBean) {
                        Log.i("niyuanjie","登录成功");
                    }

                    @Override
                    public void loginFail(BmobException ex) {
                        Log.i("niyuanjie","登录失败");
                        BmobExceptionUtil.dealWithException(EnterActivity.this,ex);
                    }
                });

    }


    @Override
    public void initUser(boolean loginStatus) {

    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            getTintManager().setStatusBarAlpha(0);
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

}
