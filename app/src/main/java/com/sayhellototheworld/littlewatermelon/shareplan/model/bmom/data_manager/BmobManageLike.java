package com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.data_manager;

import android.content.Context;

import com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.bean.LikeBean;
import com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.bean.PlanBean;
import com.sayhellototheworld.littlewatermelon.shareplan.util.BmobExceptionUtil;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by 123 on 2017/10/11.
 */

public class BmobManageLike {

    private Context mContext;

    public BmobManageLike(Context context) {
        mContext = context;
    }

    public void saveLikeBean(final MyUserBean userBean, String planObjectID){
        final PlanBean planBean = new PlanBean();
        planBean.setObjectId(planObjectID);
        BmobQuery<LikeBean> query = new BmobQuery<>();
        query.addWhereEqualTo("user",userBean);
        query.addWhereEqualTo("plan",planBean);
        query.findObjects(new FindListener<LikeBean>() {
            @Override
            public void done(List<LikeBean> list, BmobException e) {
                if (e == null){
                    if (list.size() == 0){
                        LikeBean likeBean = new LikeBean();
                        likeBean.setUser(userBean);
                        likeBean.setPlan(planBean);
                        likeBean.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e != null){
                                    BmobExceptionUtil.dealWithException(mContext,e);
                                }
                            }
                        });
                    }else {
                        for (LikeBean l:list){
                            l.delete(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e != null){
                                        BmobExceptionUtil.dealWithException(mContext,e);
                                    }
                                }
                            });
                        }
                    }
                }else {
                    BmobExceptionUtil.dealWithException(mContext,e);
                }
            }
        });
    }

}
