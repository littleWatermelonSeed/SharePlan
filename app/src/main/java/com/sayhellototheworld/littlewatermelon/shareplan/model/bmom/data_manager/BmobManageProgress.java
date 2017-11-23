package com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.data_manager;

import android.content.Context;

import com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.bean.PlanBean;
import com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.bean.ProgressBean;
import com.sayhellototheworld.littlewatermelon.shareplan.model.localDB.table.TablePlanProgress;
import com.sayhellototheworld.littlewatermelon.shareplan.model.localDB.table.TableProgressImagePath;
import com.sayhellototheworld.littlewatermelon.shareplan.model.local_file.GetFile;
import com.sayhellototheworld.littlewatermelon.shareplan.my_interface.function_interface.QueryProgressDo;
import com.sayhellototheworld.littlewatermelon.shareplan.my_interface.function_interface.SavePlanProgressDo;
import com.sayhellototheworld.littlewatermelon.shareplan.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.shareplan.util.PictureUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

/**
 * Created by 123 on 2017/9/23.
 */

public class BmobManageProgress {

    private Context mContext;

    public BmobManageProgress(Context context) {
        mContext = context;
    }

    public void saveProgress(final ProgressBean progressBean, List<String> imageUrls, final SavePlanProgressDo done){
        if (imageUrls.size() <= 0){
            progressBean.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null){
                        done.saveSuccess(progressBean,null,null);
                    }else {
                        done.saveFail(e);
                    }
                }
            });
        }else {
            saveProgressImages(progressBean,imageUrls,done);
        }
    }

    private void saveProgressImages(final ProgressBean progressBean, final List<String> imageUrls, final SavePlanProgressDo done){
        String[] paths = new String[imageUrls.size()];
        for (int i = 0;i < paths.length;i++){
            paths[i] = imageUrls.get(i);
        }
        BmobFile.uploadBatch(paths, new UploadBatchListener() {
            @Override
            public void onSuccess(final List<BmobFile> list, final List<String> list1) {
                if (list.size() == imageUrls.size()){
                    final List<String> l = new ArrayList<String>();
                    for (BmobFile b:list){
                        l.add(b.getUrl());
                    }
                    progressBean.setImageUrls(l);
                    progressBean.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null){
                                done.saveSuccess(progressBean,imageUrls,l);
                            }else {
                                done.saveFail(e);
                            }
                        }
                    });
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

    public void queryProgress(int skin,PlanBean planBean, final QueryProgressDo done){
        BmobQuery<ProgressBean> query = new BmobQuery<>();
        query.addWhereEqualTo("plan",new BmobPointer(planBean));
        query.setLimit(5);
        query.setSkip(skin);
        query.order("createdAt");
        query.findObjects(new FindListener<ProgressBean>() {
            @Override
            public void done(List<ProgressBean> list, BmobException e) {
                if (e == null){
                    done.querySuccess(list);
                }else {
                    done.queryFail(e);
                }
            }
        });
    }

    public void downLoadProgressImage(String url, final TablePlanProgress tablePlanProgress){
        final BmobFile planImage = new BmobFile(PictureUtil.getPicNameFromUrl(url) + ".png","", url);
        final File saveFile = new File(GetFile.getExternalPlanImageFile(), planImage.getFilename());
        planImage.download(saveFile, new DownloadFileListener() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null){
                    TableProgressImagePath imagePath = new TableProgressImagePath();
                    imagePath.setImagePath(s);
                    imagePath.setTablePlanProgress(tablePlanProgress);
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

}
