package com.sayhellototheworld.littlewatermelon.shareplan.my_interface.function_interface;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by 123 on 2017/10/11.
 */

public interface SaveCommentDo {

    void saveSuccess(String s);
    void saveFail(BmobException e);

}
