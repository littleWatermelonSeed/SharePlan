package com.sayhellototheworld.littlewatermelon.shareplan;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sayhellototheworld.littlewatermelon.shareplan.model.data_manage.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.shareplan.model.data_manage.data.ManageUser;
import com.sayhellototheworld.littlewatermelon.shareplan.model.local_file.GetFile;
import com.sayhellototheworld.littlewatermelon.shareplan.model.local_file.ManageFile;
import com.sayhellototheworld.littlewatermelon.shareplan.model.local_file.MySharedPreferences;
import com.sayhellototheworld.littlewatermelon.shareplan.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.shareplan.util.PictureUtil;
import com.sayhellototheworld.littlewatermelon.shareplan.view.base_activity.BaseStatusActivity;
import com.sayhellototheworld.littlewatermelon.shareplan.view.centerplaza_view.CenterPlazaActivity;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.SaveListener;

public class EnterActivity extends BaseStatusActivity {

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

    private void init() {
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

    private void downloadFile(BmobFile file){
        //允许设置下载文件的存储路径，默认下载文件的目录为：context.getApplicationContext().getCacheDir()+"/bmob/"
        File saveFile = new File(GetFile.getInternalImageFile(), file.getFilename());
        file.download(saveFile, new DownloadFileListener() {

            @Override
            public void onStart() {

            }

            @Override
            public void done(String savePath,BmobException e) {
                if(e==null){
                    button.setClickable(true);
                }else{
                    BmobExceptionUtil.dealWithException(EnterActivity.this,e);
                }
            }

            @Override
            public void onProgress(Integer value, long newworkSpeed) {

            }

        });
    }

    private void initUser() {
        if (!login) {
            return;
        }

        MyUserBean bu2 = new MyUserBean();

        bu2.setUsername(MySharedPreferences.getInstance().getMessage(MySharedPreferences.TYPE_USER_ID));
        bu2.setPassword(MySharedPreferences.getInstance().getMessage(MySharedPreferences.TYPE_USER_PASSWORD));

        bu2.login(new SaveListener<MyUserBean>() {

            @Override
            public void done(final MyUserBean myUserBean, final BmobException e) {
                if (e == null) {
                    if (mManageUser.getCurrentUser().getHeadPortrait().getUrl() == null) {
                        return;
                    } else {
                        if (ManageFile.getHeadPortrait(PictureUtil.getPicNameFromUrl(myUserBean.getHeadPortrait().getUrl())) != null) {
                            return;
                        }
                        final BmobFile headPic = new BmobFile(PictureUtil.getPicNameFromUrl(myUserBean.getHeadPortrait().getUrl()) + ".png",
                                "",myUserBean.getHeadPortrait().getUrl());
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                downloadFile(headPic);
                            }
                        }).start();
                    }
                } else {
                    BmobUser.logOut();
                    BmobExceptionUtil.dealWithException(EnterActivity.this, e);
                }
            }
        });

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
