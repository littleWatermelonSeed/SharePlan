package com.sayhellototheworld.littlewatermelon.shareplan.view.user_view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sayhellototheworld.littlewatermelon.shareplan.R;
import com.sayhellototheworld.littlewatermelon.shareplan.customwidget.LiTopBar;
import com.sayhellototheworld.littlewatermelon.shareplan.my_interface.base_interface.BaseActivityDo;
import com.sayhellototheworld.littlewatermelon.shareplan.presenter.centerplaza.ControlUserFragment;
import com.sayhellototheworld.littlewatermelon.shareplan.customwidget.DialogConfirm;
import com.sayhellototheworld.littlewatermelon.shareplan.view.base_activity.BaseSlideBcakStatusActivity;

import cn.bmob.v3.BmobUser;

public class UserSettingActivity extends BaseSlideBcakStatusActivity implements BaseActivityDo, View.OnClickListener {

    private LiTopBar mLiTopBar;
    private Button button_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);
        init();
    }

    @Override
    public void init() {
        initWidget();
        initParam();
        initShow();
    }

    @Override
    public void initWidget() {
        button_login = (Button) findViewById(R.id.activity_user_setting_loginOutButton);
        button_login.setOnClickListener(this);
        mLiTopBar = (LiTopBar) findViewById(R.id.activity_user_setting_topbar);
        mLiTopBar.setLeftContainerListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initParam() {

    }

    @Override
    public void initShow() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_user_setting_loginOutButton:
                LoginOut();
                break;
        }
    }

    private void LoginOut() {
        DialogConfirm.newInstance("提示", "确定退出登录?", new DialogConfirm.CancleAndOkDo() {
            @Override
            public void cancle() {

            }

            @Override
            public void ok() {
                BmobUser.logOut();
                ControlUserFragment.syncUserFragment();
                finish();
                LoginActivity.startLoginActivity(UserSettingActivity.this);
            }
        }).setMargin(60)
                .setOutCancel(false)
                .show(getSupportFragmentManager());
    }

    public static void startLoginActivity(final Context context) {
        context.startActivity(new Intent(context, UserSettingActivity.class));
    }

}
