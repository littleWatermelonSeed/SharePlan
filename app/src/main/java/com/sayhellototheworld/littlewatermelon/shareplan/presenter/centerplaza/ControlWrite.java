package com.sayhellototheworld.littlewatermelon.shareplan.presenter.centerplaza;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.ViewHolder;
import com.sayhellototheworld.littlewatermelon.shareplan.R;
import com.sayhellototheworld.littlewatermelon.shareplan.customwidget.DialogLoading;
import com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.bean.PlanBean;
import com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.data_manager.BmobManagePlan;
import com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.shareplan.model.localDB.data_manager.LocalManageImagePath;
import com.sayhellototheworld.littlewatermelon.shareplan.model.localDB.data_manager.LocalManagePlan;
import com.sayhellototheworld.littlewatermelon.shareplan.model.localDB.table.TablePlan;
import com.sayhellototheworld.littlewatermelon.shareplan.model.local_file.ManageFile;
import com.sayhellototheworld.littlewatermelon.shareplan.model.thread_manager.JoinToThreadPool;
import com.sayhellototheworld.littlewatermelon.shareplan.my_interface.centerplaza_interface.SavePlanDo;
import com.sayhellototheworld.littlewatermelon.shareplan.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.shareplan.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.shareplan.util.PictureUtil;
import com.sayhellototheworld.littlewatermelon.shareplan.util.TimeFormatUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by 123 on 2017/9/23.
 */

public class ControlWrite implements SavePlanDo {

    private Context mContext;
    private BmobManagePlan bmp;
    private LocalManagePlan lmp;
    private LocalManageImagePath lmip;

    private List<String> myImagePaths;

    private BaseNiceDialog dialog;
    private final Handler handler;

    public ControlWrite(Context context) {
        mContext = context;
        bmp = new BmobManagePlan(mContext);
        lmp = new LocalManagePlan();
        lmip = new LocalManageImagePath();


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.arg1 == DialogLoading.MSG_FAIL) {
                    dialog.dismiss();
                    MyToastUtil.showToast("计划提交失败");
                } else if (msg.arg1 == DialogLoading.MSG_SUCCESS) {
                    dialog.dismiss();
                    MyToastUtil.showToast("计划提交成功");
                    ((Activity) mContext).finish();
                }
            }
        };
    }

    public void submitPlan(final PlanBean planBean, final List<String> imagePaths) {
        myImagePaths = imagePaths;
        DialogLoading.showLoadingDialog(((FragmentActivity) mContext).getSupportFragmentManager(),
                new DialogLoading.ShowLoadingDone() {
                    @Override
                    public void done(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                        dialog = baseNiceDialog;
                        TextView textView = viewHolder.getView(R.id.nicedialog_loading_textView);
                        textView.setText("保存中...");
                        planBean.setUser(BmobManageUser.getCurrentUser());
                        bmp.savePlan(planBean, imagePaths, ControlWrite.this);
                    }
                });
    }

    @Override
    public void saveSuccess(PlanBean planBean, List<String> imageUrls) {
        TablePlan tablePlan = new TablePlan();
        tablePlan.setObjectID(planBean.getObjectId());
        tablePlan.setUserID(BmobManageUser.getCurrentUser().getUsername());
        tablePlan.setTitle(planBean.getTitle());
        tablePlan.setContent(planBean.getContent());
        tablePlan.setBeginTime(TimeFormatUtil.bmobDateToDate(planBean.getBeginTime().getDate()));
        tablePlan.setEndTime(TimeFormatUtil.bmobDateToDate(planBean.getEndTime().getDate()));
        tablePlan.setLimit(planBean.getLimit());
        tablePlan.setStatue(planBean.getStatue());
        tablePlan.setCreateTime(TimeFormatUtil.bmobDateToDate(planBean.getCreatedAt()));
        tablePlan.setLikes(planBean.getStars());
        saveLocalPlan(tablePlan,imageUrls);
    }

    @Override
    public void saveFail(BmobException e) {
        DialogLoading.dismissLoadingDialog(handler, dialog, "", DialogLoading.MSG_FAIL);
        BmobExceptionUtil.dealWithException(mContext, e);
    }

    @Override
    public void saveImageFail(String s) {
        DialogLoading.dismissLoadingDialog(handler, dialog, "", DialogLoading.MSG_FAIL);
        MyToastUtil.showToast(s);
    }

    public void saveLocalPlan(final TablePlan tablePlan,final List<String> imageUrls) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                saveDate(tablePlan,imageUrls);
            }
        };
        JoinToThreadPool.joinToCache(runnable);
    }

    private void saveDate(TablePlan tablePlan,List<String> imageUrls) {
        if (myImagePaths.size() > 0) {
            List<String> newImagePath = new ArrayList<>();
            int n = 0;
            for (String s : myImagePaths) {
                newImagePath.add(ManageFile.copyImageToPlan(s, PictureUtil.getPicNameFromUrl(imageUrls.get(n))));
                n++;
            }
            tablePlan.setImagePath(lmip.saveImagePath(newImagePath));
        }
        lmp.savePlan(tablePlan);
        ((FragmentActivity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DialogLoading.dismissLoadingDialog(handler, dialog, "计划提交成功", DialogLoading.MSG_SUCCESS);
                ((Activity) mContext).finish();
            }
        });
    }

}
