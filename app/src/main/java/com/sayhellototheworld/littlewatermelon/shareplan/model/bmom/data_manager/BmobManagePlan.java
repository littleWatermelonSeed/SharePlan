package com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.data_manager;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.bean.PlanBean;
import com.sayhellototheworld.littlewatermelon.shareplan.model.localDB.table.TableImagePath;
import com.sayhellototheworld.littlewatermelon.shareplan.model.localDB.table.TablePlan;
import com.sayhellototheworld.littlewatermelon.shareplan.model.local_file.GetFile;
import com.sayhellototheworld.littlewatermelon.shareplan.model.thread_manager.JoinToThreadPool;
import com.sayhellototheworld.littlewatermelon.shareplan.my_interface.centerplaza_interface.SavePlanDo;
import com.sayhellototheworld.littlewatermelon.shareplan.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.shareplan.util.PictureUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
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
                done.saveImageFail(s);
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

    public void downLoadPlanImage(String url, final TablePlan tablePlan){
        final BmobFile planImage = new BmobFile(PictureUtil.getPicNameFromUrl(url) + ".png","", url);
        final File saveFile = new File(GetFile.getExternalPlanImageFile(), planImage.getFilename());
        planImage.download(saveFile, new DownloadFileListener() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null){
                    TableImagePath imagePath = new TableImagePath();
                    imagePath.setImagePath(s);
                    List<TableImagePath> list = new ArrayList<TableImagePath>();
                    list.add(imagePath);
                    tablePlan.setImagePath(list);
                    tablePlan.save();
                    imagePath.save();
                }else {
                    BmobExceptionUtil.dealWithException(mContext,e);
                }

            }

            @Override
            public void onProgress(Integer integer, long l) {

            }
        });
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

    public void queryPlanToHome(int skip,FindListener<PlanBean> findListener){
        BmobQuery<PlanBean> planBean = new BmobQuery<>();
//        planBeen.addWhereEqualTo("limit",true);
//        planBeen.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
//        planBean.order("-createdAt");
        planBean.include("user");
        planBean.setLimit(10);
        planBean.setSkip(skip);
        planBean.findObjects(findListener);
    }

    public void queryPlanToHome(int skip, Date date, FindListener<PlanBean> findListener){
        BmobQuery<PlanBean> planBeen = new BmobQuery<>();
        planBeen.addWhereEqualTo("limit",true);
//        planBeen.addWhereLessThanOrEqualTo("createdAt",new BmobDate(date));
//        planBeen.order("-createdAt");
        planBeen.include("user");
        planBeen.setLimit(10);
        planBeen.setSkip(skip);
        planBeen.findObjects(findListener);
    }

    public void updateLikes(String planObjectID,final int n){
        PlanBean planBean = new PlanBean();
        planBean.increment("stars",n);
        planBean.update(planObjectID, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    if (n < 0){

                    }
                }else {
                    BmobExceptionUtil.dealWithException(mContext,e);
                }
            }
        });
    }

}

















