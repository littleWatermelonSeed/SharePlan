package com.sayhellototheworld.littlewatermelon.shareplan.model.data_manage.bean;

import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobUser;

/**
 * Created by 123 on 2017/9/17.
 */

public class MyBmobInstallation extends BmobInstallation{

    private BmobUser mBmobUser;

    public BmobUser getBmobUser() {
        return mBmobUser;
    }

    public void setBmobUser(BmobUser bmobUser) {
        mBmobUser = bmobUser;
    }
}
