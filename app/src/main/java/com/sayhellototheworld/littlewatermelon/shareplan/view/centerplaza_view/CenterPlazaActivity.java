package com.sayhellototheworld.littlewatermelon.shareplan.view.centerplaza_view;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.sayhellototheworld.littlewatermelon.shareplan.R;
import com.sayhellototheworld.littlewatermelon.shareplan.customwidget.DialogConfirm;
import com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.shareplan.my_interface.base_interface.BaseActivityDo;
import com.sayhellototheworld.littlewatermelon.shareplan.util.LayoutBackgroundUtil;
import com.sayhellototheworld.littlewatermelon.shareplan.view.base_activity.BaseStatusActivity;
import com.sayhellototheworld.littlewatermelon.shareplan.view.base_activity.SystemBarTintManager;
import com.sayhellototheworld.littlewatermelon.shareplan.view.centerplaza_view.centerplaza_fragment.HomePageFragment;
import com.sayhellototheworld.littlewatermelon.shareplan.view.centerplaza_view.centerplaza_fragment.MessageFragment;
import com.sayhellototheworld.littlewatermelon.shareplan.view.centerplaza_view.centerplaza_fragment.PlanFragment;
import com.sayhellototheworld.littlewatermelon.shareplan.view.centerplaza_view.centerplaza_fragment.UserFragment;
import com.sayhellototheworld.littlewatermelon.shareplan.view.user_view.LoginActivity;

public class CenterPlazaActivity extends BaseStatusActivity implements View.OnClickListener,BaseActivityDo{

    private FrameLayout contentLayout;
    private TextView messageRemind;
    private ImageView iHome;
    private ImageView iMessage;
    private LinearLayout lWrite;
    private ImageView iPlan;
    private ImageView iUser;
    private TextView tHome;
    private TextView tMessage;
    private TextView tPlan;
    private TextView tUser;

    private HomePageFragment mFragment_homePage;
    private MessageFragment mFragment_message;
    private PlanFragment mFragment_plan;
    private UserFragment mFragment_user;
    private FragmentManager fm;
    private FragmentTransaction mTransaction;

    private SystemBarTintManager mTintManager;
    private int textColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center_plaza);
        init();
    }

    @Override
    public void init(){
        initParam();
        initShow();
        initWidget();
        setFragment(R.id.activity_center_plaza_bottomBar_home);
    }

    @Override
    public  void initParam(){
        fm = getFragmentManager();
        mTintManager = getTintManager();
    }

    @Override
    public void initShow() {

    }

    @Override
    public  void initWidget(){
        contentLayout = (FrameLayout)findViewById(R.id.activity_center_plaza_content);
        messageRemind = (TextView)findViewById(R.id.activity_center_plaza_bottomBar_messageRemind);
        iHome = (ImageView) findViewById(R.id.activity_center_plaza_bottomBar_home);
        iHome.setOnClickListener(this);
        iMessage = (ImageView) findViewById(R.id.activity_center_plaza_bottomBar_message);
        iMessage.setOnClickListener(this);
        lWrite = (LinearLayout)findViewById(R.id.activity_center_plaza_bottomBar_write);
        lWrite.setOnClickListener(this);
        iPlan = (ImageView) findViewById(R.id.activity_center_plaza_bottomBar_plan);
        iPlan.setOnClickListener(this);
        iUser = (ImageView) findViewById(R.id.activity_center_plaza_bottomBar_user);
        iUser.setOnClickListener(this);
        tHome = (TextView)findViewById(R.id.activity_center_plaza_bottomBar_home_text);
        tMessage = (TextView)findViewById(R.id.activity_center_plaza_bottomBar_message_text);
        tPlan = (TextView)findViewById(R.id.activity_center_plaza_bottomBar_plan_text);
        tUser = (TextView)findViewById(R.id.activity_center_plaza_bottomBar_user_text);
        textColor = tUser.getCurrentHintTextColor();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_center_plaza_bottomBar_home:
            case R.id.activity_center_plaza_bottomBar_message:
            case R.id.activity_center_plaza_bottomBar_plan:
                setFragment(v.getId());
                if(mTintManager != null){
                    mTintManager.setStatusBarAlpha(0.9f);
                }
                break;
            case R.id.activity_center_plaza_bottomBar_user:
                setFragment(v.getId());
                if(mTintManager != null){
                    mTintManager.setStatusBarAlpha(0);
                }
                break;
            case R.id.activity_center_plaza_bottomBar_write:
                if (BmobManageUser.getCurrentUser() != null){
                    WriteActivity.startWriteActivity(this);
                }else {
                    DialogConfirm.newInstance("提示", "你还没有登录，是否马上前去登录？", new DialogConfirm.CancleAndOkDo() {
                        @Override
                        public void cancle() {

                        }

                        @Override
                        public void ok() {
                            LoginActivity.startLoginActivity(CenterPlazaActivity.this);
                        }
                    }).setMargin(60)
                            .setOutCancel(false)
                            .show(getSupportFragmentManager());
                }
                break;
        }
    }

    private void setFragment(int id){
        mTransaction = fm.beginTransaction();
        hideFragment();
        switch (id){
            case R.id.activity_center_plaza_bottomBar_home:
                if(mFragment_homePage == null){
                    mFragment_homePage = new HomePageFragment();
                    mTransaction.add(R.id.activity_center_plaza_content,mFragment_homePage);
                }else {
                    mTransaction.show(mFragment_homePage);
                }
                initBottomBarItemImage();
                iHome.setImageResource(R.drawable.activity_center_plaza_bottombar_home_selected);
                tHome.setTextColor(getResources().getColor(R.color.centerPlazaBottomBar_textColor));
                mTransaction.commit();
                break;
            case R.id.activity_center_plaza_bottomBar_message:
                if(mFragment_message == null){
                    mFragment_message = new MessageFragment();
                    mTransaction.add(R.id.activity_center_plaza_content,mFragment_message);
                }else {
                    mTransaction.show(mFragment_message);
                }
                initBottomBarItemImage();
                iMessage.setImageResource(R.drawable.activity_center_plaza_bottombar_message_selected);
                tMessage.setTextColor(getResources().getColor(R.color.centerPlazaBottomBar_textColor));
                mTransaction.commit();
                break;
            case R.id.activity_center_plaza_bottomBar_plan:
                if(mFragment_plan == null){
                    mFragment_plan = new PlanFragment();
                    mTransaction.add(R.id.activity_center_plaza_content,mFragment_plan);
                }else {
                    mTransaction.show(mFragment_plan);
                }
                initBottomBarItemImage();
                iPlan.setImageResource(R.drawable.activity_center_plaza_bottombar_plan_selected);
                tPlan.setTextColor(getResources().getColor(R.color.centerPlazaBottomBar_textColor));
                mTransaction.commit();
                break;
            case R.id.activity_center_plaza_bottomBar_user:
                if(mFragment_user == null){
                    LayoutBackgroundUtil.preloadBackgroundResource(this, R.drawable.user_background, new RequestListener() {
                        @Override
                        public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
                            mFragment_user = new UserFragment();
                            mTransaction.add(R.id.activity_center_plaza_content,mFragment_user);
                            mTransaction.commit();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
                            mFragment_user = new UserFragment();
                            mTransaction.add(R.id.activity_center_plaza_content,mFragment_user);
                            mTransaction.commit();
                            return false;
                        }
                    });
                    initBottomBarItemImage();
                    iUser.setImageResource(R.drawable.activity_center_plaza_bottombar_user_selected);
                    tUser.setTextColor(getResources().getColor(R.color.centerPlazaBottomBar_textColor));
                    return;
                }else {
                    mTransaction.show(mFragment_user);
                }
                initBottomBarItemImage();
                iUser.setImageResource(R.drawable.activity_center_plaza_bottombar_user_selected);
                tUser.setTextColor(getResources().getColor(R.color.centerPlazaBottomBar_textColor));
                mTransaction.commit();
                break;
        }
    }

    private void hideFragment(){
        if(mFragment_homePage != null){
            mTransaction.hide(mFragment_homePage);
        }
        if(mFragment_message != null){
            mTransaction.hide(mFragment_message);
        }
        if(mFragment_plan != null){
            mTransaction.hide(mFragment_plan);
        }
        if(mFragment_user != null){
            mTransaction.hide(mFragment_user);
        }
    }

    private void initBottomBarItemImage(){
        iHome.setImageResource(R.drawable.activity_center_plaza_bottombar_home_disselected);
        iMessage.setImageResource(R.drawable.activity_center_plaza_bottombar_message_disselected);
        iPlan.setImageResource(R.drawable.activity_center_plaza_bottombar_plan_disselected);
        iUser.setImageResource(R.drawable.activity_center_plaza_bottombar_user_disselected);
        tHome.setTextColor(textColor);
        tMessage.setTextColor(textColor);
        tPlan.setTextColor(textColor);
        tUser.setTextColor(textColor);
    }

}
