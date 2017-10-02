package com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.data_manager;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.bean.PlanBean;
import com.sayhellototheworld.littlewatermelon.shareplan.model.thread_manager.JoinToThreadPool;
import com.sayhellototheworld.littlewatermelon.shareplan.my_interface.centerplaza_interface.SavePlanDo;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

/**
 * Created by 123 on 2017/9/23.
 */

public class BmobManagePlan {

    private Context mContext;
    private Runnable mRunnable;

    public BmobManagePlan(Context context) {
        mContext = context;
    }

    public void savePlan(final PlanBean planBean, final List<String> imagePaths, final SavePlanDo done){
        if (imagePaths.size() <= 0){
            savePlanWithoutImage(null, planBean, done);
            return;
        }
        savePlanImage(imagePaths, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> list, final List<String> list1) {
                if (list1.size() == imagePaths.size()){
                    savePlanWithoutImage(list1, planBean, done);
                }
            }

            @Override
            public void onProgress(int i, int i1, int i2, int i3) {

            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    private void savePlanWithoutImage(final List<String> list1, final PlanBean planBean, final SavePlanDo done) {
        ((FragmentActivity)mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (list1 != null){
                    planBean.addAll("imageUrls",list1);
                }
                planBean.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null){
                            done.saveSuccess(planBean,list1);
                        }else {
                            done.saveFail(e);
                        }
                    }
                });
            }
        });
    }

    public void savePlanImage(final List<String> imagePaths,final UploadBatchListener listener){
        mRunnable = new Runnable() {
            @Override
            public void run() {
                String[] paths = new String[imagePaths.size()];
                for (int i = 0;i < paths.length;i++){
                    paths[i] = imagePaths.get(i);
                }
                BmobFile.uploadBatch(paths,listener);
            }
        };
        JoinToThreadPool.joinToSingle(mRunnable);
    }

    public void queryAllObjectID(int skip, FindListener<PlanBean> findListener){
        BmobQuery<PlanBean> planBeen = new BmobQuery<>();
        planBeen.addWhereEqualTo("user",BmobManageUser.getCurrentUser());
        planBeen.addQueryKeys("objectId");
        planBeen.order("-createdAt");
        planBeen.setLimit(10);
        planBeen.setSkip(skip);
        planBeen.findObjects(findListener);
    }

    public void queryPlanByID(List<String> objectID,FindListener<PlanBean> listener){
        List<BmobQuery<PlanBean>> queries = new ArrayList<BmobQuery<PlanBean>>();
        for (String s:objectID){
            BmobQuery<PlanBean> planBeen = new BmobQuery<>();
            planBeen.addWhereEqualTo("objectId",s);
            queries.add(planBeen);
        }
        BmobQuery<PlanBean> planBeen = new BmobQuery<>();
        planBeen.or(queries);
        planBeen.findObjects(listener);
    }

    public void queryPlanByID(String objectID,QueryListener<PlanBean> listener){
        BmobQuery<PlanBean> planBeen = new BmobQuery<>();
        planBeen.getObject(objectID,listener);
    }

}

















