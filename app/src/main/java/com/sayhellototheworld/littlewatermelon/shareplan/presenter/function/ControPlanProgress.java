package com.sayhellototheworld.littlewatermelon.shareplan.presenter.function;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.ViewHolder;
import com.sayhellototheworld.littlewatermelon.shareplan.R;
import com.sayhellototheworld.littlewatermelon.shareplan.adapter.PlanImageAdapter;
import com.sayhellototheworld.littlewatermelon.shareplan.adapter.PlanProgressAdapter;
import com.sayhellototheworld.littlewatermelon.shareplan.customwidget.DialogConfirm;
import com.sayhellototheworld.littlewatermelon.shareplan.customwidget.DialogLoading;
import com.sayhellototheworld.littlewatermelon.shareplan.customwidget.MyGridView;
import com.sayhellototheworld.littlewatermelon.shareplan.customwidget.MyPopupWindow;
import com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.bean.PlanBean;
import com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.bean.ProgressBean;
import com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.data_manager.BmobManagePlan;
import com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.data_manager.BmobManageProgress;
import com.sayhellototheworld.littlewatermelon.shareplan.model.localDB.data_manager.LocalManagePlan;
import com.sayhellototheworld.littlewatermelon.shareplan.model.localDB.data_manager.LocalManageProgress;
import com.sayhellototheworld.littlewatermelon.shareplan.model.localDB.data_manager.LocalManageProgressImagePath;
import com.sayhellototheworld.littlewatermelon.shareplan.model.localDB.table.TablePlan;
import com.sayhellototheworld.littlewatermelon.shareplan.model.localDB.table.TablePlanProgress;
import com.sayhellototheworld.littlewatermelon.shareplan.model.localDB.table.TableProgressImagePath;
import com.sayhellototheworld.littlewatermelon.shareplan.model.local_file.GetFile;
import com.sayhellototheworld.littlewatermelon.shareplan.model.local_file.ManageFile;
import com.sayhellototheworld.littlewatermelon.shareplan.model.thread_manager.JoinToThreadPool;
import com.sayhellototheworld.littlewatermelon.shareplan.my_interface.function_interface.QueryProgressDo;
import com.sayhellototheworld.littlewatermelon.shareplan.my_interface.function_interface.SavePlanProgressDo;
import com.sayhellototheworld.littlewatermelon.shareplan.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.shareplan.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.shareplan.util.NetWorkUtil;
import com.sayhellototheworld.littlewatermelon.shareplan.util.PictureUtil;
import com.sayhellototheworld.littlewatermelon.shareplan.util.SysUtil;
import com.sayhellototheworld.littlewatermelon.shareplan.util.TimeFormatUtil;
import com.sayhellototheworld.littlewatermelon.shareplan.view.function_view.PlanProgressActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zcw.togglebutton.ToggleButton;
import com.zhy.autolayout.utils.AutoUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by 123 on 2017/10/4.
 */

public class ControPlanProgress implements OnLoadmoreListener, OnRefreshListener,
        View.OnClickListener,SavePlanProgressDo,ToggleButton.OnToggleChanged,QueryProgressDo{

    private Context mContext;
    private PlanProgressActivity mProgressActivity;
    private SmartRefreshLayout mRefreshLayout;
    private ListView listView;
    private String planObjectID;
    private int status;

    private MyPopupWindow popupWindow;
    private PlanImageAdapter imageAdapter;
    private TextView cancle;
    private TextView submit;
    private EditText content;
    private ToggleButton mToggleButton;
    private MyGridView gridView;
    private List<String> imagePaths;

    private BmobManagePlan bmp;
    private BmobManageProgress bmpro;
    private LocalManageProgressImagePath lmpip;
    private LocalManageProgress lmpro;
    private LocalManagePlan lmp;
    private PlanBean mPlanBean;
    private TablePlan tablePlan;
    private List<TablePlanProgress> dataProgress;
    private List<List<String>> dataImageUrls;
    private PlanProgressAdapter mAdapter;

    private BaseNiceDialog dialog;
    private final Handler handler;

    private boolean firstShow = true;
    private boolean refresh = true;
    private boolean loading = false;

    private int planStatus = -1;
    private int dataIndex = 0;

    public ControPlanProgress(Context context, SmartRefreshLayout refreshLayout,
                              ListView listView,List<String> imagePaths,String planObjectID,
                              int status,PlanProgressActivity mProgressActivity) {
        mContext = context;
        mRefreshLayout = refreshLayout;
        this.listView = listView;
        this.imagePaths = imagePaths;
        this.planObjectID = planObjectID;
        this.status = status;
        this.mProgressActivity = mProgressActivity;

        bmp = new BmobManagePlan(mContext);
        bmpro = new BmobManageProgress(mContext);
        lmpip = new LocalManageProgressImagePath();
        lmpro = new LocalManageProgress();
        lmp = new LocalManagePlan();
        tablePlan = lmp.queryPlanByObjectID(planObjectID);
        mRefreshLayout.autoRefresh();
        mRefreshLayout.setEnableAutoLoadmore(false);

        dataProgress = new ArrayList<>();
        dataImageUrls = new ArrayList<>();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.arg1 == DialogLoading.MSG_FAIL) {
                    dialog.dismiss();
                    MyToastUtil.showToast("计划进度提交失败");
                } else if (msg.arg1 == DialogLoading.MSG_SUCCESS) {
                    dialog.dismiss();
                    MyToastUtil.showToast("计划进度提交成功");
                    ((Activity) mContext).finish();
                }
            }
        };
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        refresh = false;
        loading = true;
        bmpro.queryProgress(dataIndex,mPlanBean,this);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        refresh = true;
        loading = false;
        dataIndex = 0;
        dataProgress.clear();
        dataImageUrls.clear();
        if (!NetWorkUtil.isNetworkAvailable(mContext)){
            notNetRefreshDo();
        }else {
            if (firstShow){
                firstShow();
            }else {
                bmpro.queryProgress(dataIndex,mPlanBean,this);
            }
        }
    }

    private void notNetRefreshDo(){
        MyToastUtil.showToast("没网了，只能加载本地数据咯");
        TablePlan t = lmp.queryPlanByObjectID(planObjectID);
        dataProgress = t.getProgress();
        for (TablePlanProgress p:dataProgress){
            List<String> urls = new ArrayList<>();
            for (TableProgressImagePath im:p.getImagePaths()){
                urls.add(im.getImagePath());
            }
            dataImageUrls.add(urls);
        }
        showList();
    }

    private void showList(){
        if (dataProgress.size() <= 0){
            mProgressActivity.listEmpty(true);
        }else {
            mProgressActivity.listEmpty(false);
        }

        if (firstShow){
            firstShow = false;
        }

        mAdapter = new PlanProgressAdapter(mContext,dataProgress,dataImageUrls);
        listView.setAdapter(mAdapter);

        if (refresh) {
            listView.setSelection(0);
        } else {
            listView.setSelection(dataProgress.size());
        }

        finishSmart(true);
    }

    private void firstShow() {
        bmp.queryPlanByID(planObjectID, new QueryListener<PlanBean>() {
            @Override
            public void done(PlanBean planBean, BmobException e) {
                if (e == null){
                    mPlanBean = planBean;
                    bmpro.queryProgress(dataIndex,mPlanBean,ControPlanProgress.this);
                }else {
                    finishSmart(false);
                    BmobExceptionUtil.dealWithException(mContext,e);
                }
            }
        });
    }

    @Override
    public void querySuccess(List<ProgressBean> list) {
        if (list.size() == 0){
            MyToastUtil.showToast("到底啦，再刷也没有啦~");
            finishSmart(true);
            return;
        }
        for(ProgressBean p:list){
            dataIndex++;
            List<String> data = new ArrayList<>();
            List<String> urls = p.getImageUrls();
            TablePlanProgress tablePlanProgress;

            if (!lmpro.existProgress(p.getObjectId())){
                tablePlanProgress = new TablePlanProgress();
                tablePlanProgress.setContent(p.getContent());
                tablePlanProgress.setTablePlan(tablePlan);
                tablePlanProgress.setCreateTime(TimeFormatUtil.bmobDateToDate(p.getCreatedAt()));
                tablePlanProgress.setObjectID(p.getObjectId());
                lmpro.savaLocalProgress(tablePlanProgress);
            }else {
                tablePlanProgress = lmpro.queryLocalProgressByObjectID(p.getObjectId());
            }

            if (urls != null){
                for (String s:urls){
                    File image = new File(GetFile.getExternalPlanImageFile(), PictureUtil.getPicNameFromUrl(s) + ".png");
                    if (image.exists()){
                        data.add(image.getAbsolutePath());
                    }else {
                        data.add(s);
                        bmpro.downLoadProgressImage(s,tablePlanProgress);
                    }
                }
            }
            dataImageUrls.add(data);
            dataProgress.add(tablePlanProgress);
        }
        showList();
    }

    @Override
    public void queryFail(BmobException e) {
        finishSmart(false);
        BmobExceptionUtil.dealWithException(mContext,e);
    }



    private void submitProgress(){
        String con = content.getText().toString();
        con = con.replaceAll("\\s","");
        if (con.equals("")){
            MyToastUtil.showToast("内容不能为空");
            return;
        }
        final ProgressBean progressBean = new ProgressBean();
        progressBean.setContent(con);
        progressBean.setPlan(mPlanBean);
        DialogLoading.showLoadingDialog(((FragmentActivity) mContext).getSupportFragmentManager(),
                new DialogLoading.ShowLoadingDone() {
                    @Override
                    public void done(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                        dialog = baseNiceDialog;
                        TextView textView = viewHolder.getView(R.id.nicedialog_loading_textView);
                        textView.setText("提交中...");
                        bmpro.saveProgress(progressBean,imagePaths,ControPlanProgress.this);
                    }
                });
    }

    public void imageUrlsChanged(){
        imageAdapter.notifyDataSetChanged();
    }

    public void writeProgress(){
//        if (loading){
//            return;
//        }
        if (status == 1){
            MyToastUtil.showToast("任务已完成，不能再添加进度了");
            return;
        }else if (status == -1){
            MyToastUtil.showToast("任务未完成，不能再添加进度了");
            return;
        }
        popupWindow = new MyPopupWindow(mContext);
        View popuView = LayoutInflater.from(mContext).inflate(R.layout.popuwindow_write_progress,null,false);
        AutoUtils.autoSize(popuView);

        cancle = (TextView)popuView.findViewById(R.id.popuwindow_write_progress_cancle);
        submit = (TextView)popuView.findViewById(R.id.popuwindow_write_progress_submit);
        content = (EditText) popuView.findViewById(R.id.popuwindow_write_progress_content);
        gridView = (MyGridView)popuView.findViewById(R.id.popuwindow_write_progress_image);
        mToggleButton = (ToggleButton)popuView.findViewById(R.id.popuwindow_write_progress_toggleButton);
        mToggleButton.setOnToggleChanged(this);
        imageAdapter = new PlanImageAdapter(mContext,imagePaths,PlanImageAdapter.TYPE_WRITE_PLAN);
        gridView.setAdapter(imageAdapter);

        popupWindow.setContentView(popuView);
        popupWindow.setWidth(SysUtil.getDisplayWidth()*6/7);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.showAtLocation(((Activity)mContext).getWindow().getDecorView(), Gravity.CENTER,0,0);
        submit.setOnClickListener(this);
        cancle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.popuwindow_write_progress_cancle:
                popupWindow.dismiss();
                imagePaths.clear();
                break;
            case R.id.popuwindow_write_progress_submit:
                submitProgress();
                break;
        }
    }

    @Override
    public void saveSuccess(ProgressBean progressBean, List<String> imagePaths,List<String> urls) {
        if (planStatus == 1){
            PlanBean planBean = new PlanBean();
            planBean.setStatue(planStatus);
            planBean.update(mPlanBean.getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e != null){
                        BmobExceptionUtil.dealWithException(mContext,e);
                    }else {
                        tablePlan.setStatue(planStatus);
                        tablePlan.update(tablePlan.getBaseID());
                    }
                }
            });
        }
        saveLoaclProgress(progressBean,imagePaths,urls);
    }

    @Override
    public void saveFail(BmobException e) {
        DialogLoading.dismissLoadingDialog(handler, dialog, "", DialogLoading.MSG_FAIL);
        BmobExceptionUtil.dealWithException(mContext,e);
    }

    @Override
    public void saveImageFail(String s) {
        DialogLoading.dismissLoadingDialog(handler, dialog, "", DialogLoading.MSG_FAIL);
        MyToastUtil.showToast("图片上传失败,进度提交失败");
    }

    private void saveLoaclProgress(final ProgressBean progressBean,final List<String> imageUrls,final List<String> urls){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                TablePlanProgress planProgress = new TablePlanProgress();
                planProgress.setContent(progressBean.getContent());
                planProgress.setObjectID(progressBean.getObjectId());
                planProgress.setCreateTime(TimeFormatUtil.bmobDateToDate(progressBean.getCreatedAt()));
                if (imageUrls != null && imageUrls.size() > 0){
                    List<String> l = new ArrayList<>();
                    int n = 0;
                    for (String s:imageUrls){
                        l.add(ManageFile.copyImageToPlan(s, PictureUtil.getPicNameFromUrl(urls.get(n))));
                        n++;
                    }
                    planProgress.setImagePaths(lmpip.saveImagePath(l));
                }
                planProgress.setTablePlan(tablePlan);
                lmpro.savaLocalProgress(planProgress);
                ((FragmentActivity)mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogLoading.dismissLoadingDialog(handler, dialog, "计划进度提交成功", DialogLoading.MSG_SUCCESS);
                        popupWindow.dismiss();
                        imageUrls.clear();
                    }
                });
            }
        };
        JoinToThreadPool.joinToCache(runnable);
    }

    private void finishSmart(boolean success){
        loading = false;
        if (refresh){
            mRefreshLayout.finishRefresh(success);
        }else {
            mRefreshLayout.finishLoadmore(success);
        }
    }

    @Override
    public void onToggle(boolean on) {
        if (on){
            DialogConfirm.newInstance("提示", "完成打卡后将不能再添加计划进度,确定完成计划？", new DialogConfirm.CancleAndOkDo() {
                @Override
                public void cancle() {
                    mToggleButton.setToggleOff();
                }

                @Override
                public void ok() {
                    planStatus = 1;
                }
            }).setMargin(60)
                    .setOutCancel(false)
                    .show(((FragmentActivity)mContext).getSupportFragmentManager());
        }else {
            planStatus = -1;
        }
    }

}
