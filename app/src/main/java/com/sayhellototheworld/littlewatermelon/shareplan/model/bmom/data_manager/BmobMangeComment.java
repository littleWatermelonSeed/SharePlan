package com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.data_manager;

import android.content.Context;

import com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.bean.CommentBean;
import com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.bean.PlanBean;
import com.sayhellototheworld.littlewatermelon.shareplan.my_interface.centerplaza_interface.CommentCountQueryListener;
import com.sayhellototheworld.littlewatermelon.shareplan.my_interface.function_interface.SaveCommentDo;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by 123 on 2017/10/11.
 */

public class BmobMangeComment {

    private Context mContext;

    public BmobMangeComment(Context context) {
        mContext = context;
    }

    public void queryCommentNum(final PlanBean planBean, final CommentCountQueryListener done, final int planNum){
        BmobQuery<CommentBean> query = new BmobQuery<>();
        query.addWhereEqualTo("plan", planBean);
        query.count(CommentBean.class, new CountListener() {
            @Override
            public void done(Integer count, BmobException e) {
                if(e==null){
                    done.querySuccess(planBean,count,planNum);
                }else{
                    done.queryFail(e);
                }
            }
        });
    }

    public void saveComment(CommentBean commentBean, final SaveCommentDo done){
        commentBean.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null){
                    done.saveSuccess(s);
                }else {
                    done.saveFail(e);
                }
            }
        });
    }

}
