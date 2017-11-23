package com.sayhellototheworld.littlewatermelon.shareplan.presenter.function;

import android.content.Context;

import com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.bean.PlanBean;
import com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.data_manager.BmobManagePlan;
import com.sayhellototheworld.littlewatermelon.shareplan.model.localDB.data_manager.LocalManagePlan;
import com.sayhellototheworld.littlewatermelon.shareplan.model.localDB.table.TableImagePath;
import com.sayhellototheworld.littlewatermelon.shareplan.model.localDB.table.TablePlan;
import com.sayhellototheworld.littlewatermelon.shareplan.model.local_file.GetFile;
import com.sayhellototheworld.littlewatermelon.shareplan.my_interface.function_interface.PlanDetailsInitDo;
import com.sayhellototheworld.littlewatermelon.shareplan.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.shareplan.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.shareplan.util.NetWorkUtil;
import com.sayhellototheworld.littlewatermelon.shareplan.util.PictureUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by 123 on 2017/10/1.
 */

public class ControlPlanDetails extends QueryListener<PlanBean> implements OnLoadmoreListener, OnRefreshListener{

    private Context mContext;
    private String planObjectID;
    private SmartRefreshLayout mRefreshLayout;
    private PlanDetailsInitDo mInitShow;

    private LocalManagePlan lmp;
    private BmobManagePlan bmp;
    private TablePlan mTablePlan;

    private boolean firstShow = true;

    public ControlPlanDetails(Context context, String planObjectID,
                              SmartRefreshLayout refreshLayout, PlanDetailsInitDo initShow) {
        mContext = context;
        this.planObjectID = planObjectID;
        mRefreshLayout = refreshLayout;
        mInitShow = initShow;

        lmp = new LocalManagePlan();
        bmp = new BmobManagePlan(mContext);
        mTablePlan = lmp.queryPlanByObjectID(planObjectID);
        mRefreshLayout.setOnLoadmoreListener(this);
        mRefreshLayout.setOnRefreshListener(this);

        mInitShow.initShow(mTablePlan);
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
            MyToastUtil.showToast("没网啦，只能加载本地数据咯");
        }else {
            bmp.queryPlanByID(planObjectID, this);
        }
    }

    @Override
    public void done(PlanBean planBean, BmobException e) {
        if (e == null){
            List<String> imageUrls = planBean.getImageUrls();
            List<String> adapterUrls = new ArrayList<>();
            if (imageUrls != null){
                for (String s:imageUrls){
                    File image = new File(GetFile.getExternalPlanImageFile(), PictureUtil.getPicNameFromUrl(s) + ".png");
                    if (image.exists()){
                        adapterUrls.add(image.getAbsolutePath());
                    }else {
                        adapterUrls.add(s);
                        bmp.downLoadPlanImage(s,mTablePlan);
                    }
                }
                mInitShow.showImage(adapterUrls);
            }
        }else {
            List<TableImagePath> imagePaths = mTablePlan.getImagePath();
            List<String> imageUrls = new ArrayList<>();
            for (TableImagePath t:imagePaths){
                imageUrls.add(t.getImagePath());
            }
            mInitShow.showImage(imageUrls);
            MyToastUtil.showToast("出错啦，只能加载本地数据咯");
            BmobExceptionUtil.dealWithException(mContext,e,"查询plan完");
        }
    }

    private void more(){

    }

}
