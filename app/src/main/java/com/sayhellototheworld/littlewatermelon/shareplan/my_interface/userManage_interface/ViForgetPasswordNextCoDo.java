package com.sayhellototheworld.littlewatermelon.shareplan.my_interface.userManage_interface;

/**
 * Created by 123 on 2017/9/11.
 */

public interface ViForgetPasswordNextCoDo {

    void sentSmsCode(String phoneNum);
    void resetPassWord(String smsCode,String newPassword);

}
