package com.sayhellototheworld.littlewatermelon.shareplan.presenter.centerplaza;

import android.content.Context;

import com.sayhellototheworld.littlewatermelon.shareplan.model.data_manage.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.shareplan.model.data_manage.data.ManageUser;
import com.sayhellototheworld.littlewatermelon.shareplan.my_interface.base_interface.ShowCurUserInfo;

/**
 * Created by 123 on 2017/9/8.
 */

public class ControlUserFragment {

    private Context mContext;
    private static ShowCurUserInfo showInfo;
    private static ManageUser mManageUser;
    private static MyUserBean mUserBean;
    private static boolean UFshow = false;

    public ControlUserFragment(Context context, ShowCurUserInfo showInfo) {
        mContext = context;
        this.showInfo = showInfo;
        mManageUser = new ManageUser(mContext);
        mUserBean = mManageUser.getCurrentUser();

        showInfo.showUserInformation(mUserBean);
        UFshow = true;
    }

    public static boolean syncUserFragment() {
        if (!UFshow) {
            return false;
        }

        mUserBean = mManageUser.getCurrentUser();
        showInfo.showUserInformation(mUserBean);
        return true;
    }

    public void userFragmentDestroy() {
        showInfo = null;
        mManageUser = null;
        mUserBean = null;
        UFshow = false;
    }

}
