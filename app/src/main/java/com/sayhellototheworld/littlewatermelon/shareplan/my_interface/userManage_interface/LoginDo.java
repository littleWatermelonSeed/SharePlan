package com.sayhellototheworld.littlewatermelon.shareplan.my_interface.userManage_interface;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by 123 on 2017/9/10.
 */

public interface LoginDo {

    void loginSuccess();
    void loginFail(BmobException e);

}
