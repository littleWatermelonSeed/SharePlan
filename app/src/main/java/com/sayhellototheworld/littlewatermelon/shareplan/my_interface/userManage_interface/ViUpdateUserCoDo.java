package com.sayhellototheworld.littlewatermelon.shareplan.my_interface.userManage_interface;

import com.sayhellototheworld.littlewatermelon.shareplan.model.data_manage.bean.MyUserBean;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by 123 on 2017/9/7.
 */

public interface ViUpdateUserCoDo {

    void updateUser(MyUserBean userBean, BmobFile headPic);

}
