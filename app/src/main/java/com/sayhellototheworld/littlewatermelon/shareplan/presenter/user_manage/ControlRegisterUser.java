package com.sayhellototheworld.littlewatermelon.shareplan.presenter.user_manage;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.widget.Button;
import android.widget.TextView;

import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.ViewHolder;
import com.sayhellototheworld.littlewatermelon.shareplan.R;
import com.sayhellototheworld.littlewatermelon.shareplan.customwidget.DialogLoading;
import com.sayhellototheworld.littlewatermelon.shareplan.model.data_manage.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.shareplan.model.data_manage.data.ManageUser;
import com.sayhellototheworld.littlewatermelon.shareplan.model.local_file.MySharedPreferences;
import com.sayhellototheworld.littlewatermelon.shareplan.my_interface.userManage_interface.UserGetKeyCodeDo;
import com.sayhellototheworld.littlewatermelon.shareplan.my_interface.userManage_interface.UserRegisterDo;
import com.sayhellototheworld.littlewatermelon.shareplan.my_interface.userManage_interface.ViRegisterUserCoDo;
import com.sayhellototheworld.littlewatermelon.shareplan.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.shareplan.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.shareplan.view.base_activity.MyActivityManager;
import com.sayhellototheworld.littlewatermelon.shareplan.view.centerplaza_view.centerplaza_fragment.UserFragment;
import com.sayhellototheworld.littlewatermelon.shareplan.view.user_view.PersonalInformationActivity;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by 123 on 2017/9/7.
 */

public class ControlRegisterUser implements ViRegisterUserCoDo,UserGetKeyCodeDo,UserRegisterDo {

    private Context mContext;
    private CountDownTimer timer = null;
    private Button keyCodeSend;
    private ManageUser mManageUser;
    private BaseNiceDialog dialog;

    private String userPassword = "";
    private String userName = "";
    private final Handler handler;

    public ControlRegisterUser(Context context, Button keyCodeSend) {
        mContext = context;
        this.keyCodeSend = keyCodeSend;
        mManageUser = new ManageUser(mContext);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.arg1 == DialogLoading.MSG_SUCCESS){
                    dialog.dismiss();
                    MyToastUtil.showToast("注册成功");
                }else if (msg.arg1 == DialogLoading.MSG_FAIL){
                    dialog.dismiss();
                }
            }
        };

    }

    @Override
    public void getKeyCode(String phoneNum) {
         timer = new CountDownTimer(1000 * 60 * 2, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                keyCodeSend.setText((millisUntilFinished / 1000) + "秒后重发");
                keyCodeSend.setClickable(false);
                keyCodeSend.setBackgroundResource(R.drawable.radius_background_buttongreen3);
            }

            @Override
            public void onFinish() {
                keyCodeSend.setText("重新发送");
                keyCodeSend.setClickable(true);
                keyCodeSend.setBackgroundResource(R.drawable.state_button1);
            }
        };
        timer.start();
        mManageUser.getKeyCode(phoneNum,this, ManageUser.SMS_TEMPLATE_REGISTER);
    }

    @Override
    public void registerSubmit(final MyUserBean userBean, final String keyCode,final String password) {
        DialogLoading.showLoadingDialog(((FragmentActivity) mContext).getSupportFragmentManager(),
                new DialogLoading.ShowLoadingDone() {
                    @Override
                    public void done(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                        userPassword = password;
                        userName = userBean.getUsername();
                        dialog = baseNiceDialog;
                        TextView textView = viewHolder.getView(R.id.nicedialog_loading_textView);
                        textView.setText("注册中...");
                        mManageUser.registerCommit(userBean,keyCode,ControlRegisterUser.this);
                    }
                });
    }

    @Override
    public void sendNoteSuccess() {
        MyToastUtil.showToast("验证码已发送");
    }

    @Override
    public void sendNoteFail(BmobException ex) {
        timer.cancel();
        keyCodeSend.setClickable(true);
        keyCodeSend.setText("重新发送");
        keyCodeSend.setBackgroundResource(R.drawable.state_button1);
        BmobExceptionUtil.dealWithException(mContext, ex, "getKeyCode");
    }

    @Override
    public void registerSuccess() {
        DialogLoading.dismissLoadingDialog(handler,dialog,"注册成功", DialogLoading.MSG_SUCCESS);
        MySharedPreferences.getInstance().saveMessage(MySharedPreferences.KEY_USER_ID,userName);
        MySharedPreferences.getInstance().saveMessage(MySharedPreferences.KEY_USER_PASSWORD,userPassword);
        MySharedPreferences.getInstance().saveMessage(MySharedPreferences.KEY_USER_LOGIN_STATUS,true);
        UserFragment.setLogin(true);
        MyActivityManager.getDestoryed().destroyedListActivity();
        PersonalInformationActivity.startPersonalInformationActivity(mContext);
    }

    @Override
    public void registerFail(BmobException ex) {
        DialogLoading.dismissLoadingDialog(handler,dialog,"", DialogLoading.MSG_FAIL);
        BmobExceptionUtil.dealWithException(mContext, ex, "getKeyCode");
    }

}
