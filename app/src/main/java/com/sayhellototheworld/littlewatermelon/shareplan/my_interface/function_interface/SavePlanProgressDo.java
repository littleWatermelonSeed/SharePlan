package com.sayhellototheworld.littlewatermelon.shareplan.my_interface.function_interface;

import com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.bean.ProgressBean;

import java.util.List;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by 123 on 2017/10/5.
 */

public interface SavePlanProgressDo {

    void saveSuccess(ProgressBean progressBean, List<String> imagePaths,List<String> urls);
    void saveFail(BmobException e);
    void saveImageFail(String s);

}
