package com.sayhellototheworld.littlewatermelon.shareplan.my_interface.centerplaza_interface;

import com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.bean.PlanBean;

import java.util.List;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by 123 on 2017/9/23.
 */

public interface SavePlanDo {

    void saveSuccess(PlanBean planBean, List<String> imageUrls);
    void saveFail(BmobException e);

}
