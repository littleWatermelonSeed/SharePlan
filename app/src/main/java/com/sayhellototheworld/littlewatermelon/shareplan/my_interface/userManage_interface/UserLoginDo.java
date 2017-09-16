package com.sayhellototheworld.littlewatermelon.shareplan.my_interface.userManage_interface;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by 123 on 2017/9/7.
 */

public interface UserLoginDo {

    void loginSuccess();
    void loginFail(BmobException ex);

}
