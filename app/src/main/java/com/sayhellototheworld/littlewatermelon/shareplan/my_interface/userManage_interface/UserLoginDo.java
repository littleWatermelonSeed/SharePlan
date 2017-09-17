package com.sayhellototheworld.littlewatermelon.shareplan.my_interface.userManage_interface;

import com.sayhellototheworld.littlewatermelon.shareplan.model.data_manage.bean.MyUserBean;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by 123 on 2017/9/7.
 */

public interface UserLoginDo {

    void loginSuccess(MyUserBean myUserBean);
    void loginFail(BmobException ex);

}
