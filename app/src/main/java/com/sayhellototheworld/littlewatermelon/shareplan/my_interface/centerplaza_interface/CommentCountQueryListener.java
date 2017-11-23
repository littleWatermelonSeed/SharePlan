package com.sayhellototheworld.littlewatermelon.shareplan.my_interface.centerplaza_interface;

import com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.bean.PlanBean;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by 123 on 2017/10/11.
 */

public interface CommentCountQueryListener {

    void querySuccess(PlanBean planBean,int count,int planNum);
    void queryFail(BmobException e);

}
