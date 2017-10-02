package com.sayhellototheworld.littlewatermelon.shareplan.presenter.function;

import android.content.Context;

import com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.bean.PlanBean;
import com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.data_manager.BmobManagePlan;
import com.sayhellototheworld.littlewatermelon.shareplan.model.localDB.data_manager.LocalManagePlan;
import com.sayhellototheworld.littlewatermelon.shareplan.model.localDB.table.TableImagePath;
import com.sayhellototheworld.littlewatermelon.shareplan.model.localDB.table.TablePlan;
import com.sayhellototheworld.littlewatermelon.shareplan.my_interface.function_interface.PlanDetailsInitShow;
import com.sayhellototheworld.littlewatermelon.shareplan.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.shareplan.util.NetWorkUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by 123 on 2017/10/1.
 */

public class ControlPlanDetails extends QueryListener<PlanBean> implements OnLoadmoreListener, OnRefreshListener {

    private Context mContext;
    private String planObjectID;
    private SmartRefreshLayout mRefreshLayout;
    private PlanDetailsInitShow mInitShow;

    private LocalManagePlan lmp;
    private BmobManagePlan bmp;
    private TablePlan mTablePlan;

    private boolean firstShow = true;

    public ControlPlanDetails(Context context, String planObjectID,
                              SmartRefreshLayout refreshLayout, PlanDetailsInitShow initShow) {
        mContext = context;
        this.planObjectID = planObjectID;
        mRefreshLayout = refreshLayout;
        mInitShow = initShow;

        lmp = new LocalManagePlan();
        bmp = new BmobManagePlan(mContext);
        mTablePlan = lmp.queryPlanByObjectID(planObjectID);
        mRefreshLayout.setOnLoadmoreListener(this);
        mRefreshLayout.setOnRefreshListener(this);

        mRefreshLayout.autoRefresh();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {

    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        if (firstShow){
            firstShow();
            refreshlayout.finishRefresh(true);
        }else {

        }
    }

    private void firstShow(){
        firstShow = false;
        mInitShow.initShow(mTablePlan);
        showImage();
    }

    private void showImage(){
        if(!NetWorkUtil.isNetworkAvailable(mContext)){
            List<TableImagePath> imagePaths = mTablePlan.getImagePath();
            List<String> imageUrls = new ArrayList<>();
            for (TableImagePath t:imagePaths){
                imageUrls.add(t.getImagePath());
            }
            mInitShow.showImage(imageUrls);
        }else {
            bmp.queryPlanByID(planObjectID, this);
        }
    }

    @Override
    public void done(PlanBean planBean, BmobException e) {
        if (e == null){
            List<String> imageUrls = planBean.getImageUrls();
            if (imageUrls != null){
                mInitShow.showImage(imageUrls);
            }
        }else {
            BmobExceptionUtil.dealWithException(mContext,e);
        }
    }

}
