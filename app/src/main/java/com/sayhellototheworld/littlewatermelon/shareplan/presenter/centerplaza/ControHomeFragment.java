package com.sayhellototheworld.littlewatermelon.shareplan.presenter.centerplaza;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import com.sayhellototheworld.littlewatermelon.shareplan.adapter.HomeListAdapter;
import com.sayhellototheworld.littlewatermelon.shareplan.adapter.bean.HomePlanBean;
import com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.bean.PlanBean;
import com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.data_manager.BmobManageLike;
import com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.data_manager.BmobManagePlan;
import com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.data_manager.BmobMangeComment;
import com.sayhellototheworld.littlewatermelon.shareplan.my_interface.centerplaza_interface.CommentCountQueryListener;
import com.sayhellototheworld.littlewatermelon.shareplan.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.shareplan.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.shareplan.util.TimeFormatUtil;
import com.sayhellototheworld.littlewatermelon.shareplan.view.centerplaza_view.centerplaza_fragment.HomePageFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by 123 on 2017/10/11.
 */

public class ControHomeFragment extends FindListener<PlanBean>  implements OnLoadmoreListener, OnRefreshListener,CommentCountQueryListener{

    private Context mContext;
    private HomePageFragment hpf;
    private SmartRefreshLayout mRefreshLayout;
    private ListView  listView;

    private BmobManagePlan bmp;
    private BmobMangeComment bmc;
    private BmobManageLike bml;
    private boolean firstShow = true;
    private boolean refresh = true;
    private boolean loading = false;
    private int dataIndex = 0;
    private static int queryNum = 0;
    private Date lastDate;

    BmobQuery<PlanBean> planBeen;

    private List<HomePlanBean> dataPlan;
    private HomeListAdapter adapter;

    public ControHomeFragment(Context context, HomePageFragment hpf,SmartRefreshLayout refreshLayout, ListView listView) {
        mContext = context;
        this.hpf = hpf;
        mRefreshLayout = refreshLayout;
        this.listView = listView;
        bmp =  new BmobManagePlan(mContext);
        bmc = new BmobMangeComment(mContext);
        bml = new BmobManageLike(mContext);

        dataPlan = new ArrayList<>();
        adapter = new HomeListAdapter(mContext,this,dataPlan);
        listView.setAdapter(adapter);

        planBeen = new BmobQuery<>();
        planBeen.addWhereEqualTo("limit",true);
        planBeen.order("-createdAt");
        planBeen.include("user");

        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadmoreListener(this);
        mRefreshLayout.autoRefresh();
    }

    private void showList(){
        if (dataPlan.size() == 0){
            hpf.listEmpty(true);
        }else {
            hpf.listEmpty(false);
        }
        sortShowPlanList(dataPlan);
        adapter.notifyDataSetChanged();
        finishSmart(true);
    }

    private void finishSmart(boolean success){
        loading = false;
        if (refresh){
            mRefreshLayout.finishRefresh(success);
        }else {
            mRefreshLayout.finishLoadmore(success);
        }
    }

    private void sortShowPlanList(List<HomePlanBean> showTablePlanList) {
        Collections.sort(showTablePlanList, new Comparator<HomePlanBean>() {
            @Override
            public int compare(HomePlanBean o1, HomePlanBean o2) {
                if (TimeFormatUtil.bmobDateToDate(o1.getCreateTime()).before(TimeFormatUtil.bmobDateToDate(o2.getCreateTime())))
                    return 1;
                return -1;
            }
        });
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        refresh = false;
        loading = true;
        Log.i("niyuanjie","loadmore index = " + dataIndex);
        if (lastDate == null){
            bmp.queryPlanToHome(dataIndex,this);
        }else {
            bmp.queryPlanToHome(dataIndex,lastDate,this);
        }

    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        refresh = true;
        loading = false;
        dataIndex = 0;
        dataPlan.clear();
        bmp.queryPlanToHome(dataIndex,this);
    }

    @Override
    public void querySuccess(PlanBean planBean, int count,int planNum) {
        HomePlanBean h = bmobPlanToDatePlan(planBean,count);
        dataPlan.add(h);
        addQueryNum();
        if (queryNum == planNum){
            lastDate = TimeFormatUtil.bmobDateToDate(planBean.getCreatedAt());
            showList();
            queryNum = 0;
        }
    }

    @Override
    public void queryFail(BmobException e) {
        finishSmart(false);
        BmobExceptionUtil.dealWithException(mContext,e);
    }

    @Override
    public void done(List<PlanBean> list, BmobException e) {
        if (e == null){
            for (PlanBean p:list){
                bmc.queryCommentNum(p,this,list.size());
            }
            if (list.size() == 0){
                MyToastUtil.showToast("已结到底啦，再怎么刷也没有啦");
                showList();
            }
        }else {
            finishSmart(false);
            BmobExceptionUtil.dealWithException(mContext,e);
        }
    }

    private void addQueryNum(){
        queryNum++;
        dataIndex++;
    }

    private HomePlanBean bmobPlanToDatePlan(PlanBean planBean,int commentNum){
        HomePlanBean h = new HomePlanBean();
        h.setUserName(planBean.getUser().getNickName());
        if (planBean.getUser().getHeadPortrait() != null){
            h.setUserHeadPortraitUrl(planBean.getUser().getHeadPortrait().getUrl());
        }else {
            h.setUserHeadPortraitUrl("-");
        }
        h.setUserObjectID(planBean.getUser().getObjectId());
        h.setPlanObjectID(planBean.getObjectId());
        h.setTitle(planBean.getTitle());
        h.setCreateTime(planBean.getCreatedAt());
        h.setContent(planBean.getContent());
        h.setBeginTime(planBean.getBeginTime().getDate());
        h.setEndTime(planBean.getEndTime().getDate());
        h.setStars(planBean.getStars());
        h.setStatue(planBean.getStatue());
        h.setCommentNum(commentNum);
        if (planBean.getImageUrls() != null){
            h.setImageUrls(planBean.getImageUrls());
        }
        return h;
    }


    public void changeLikePlan(String planObjectID,int n){
        bmp.updateLikes(planObjectID,n);
        bml.saveLikeBean(BmobManageUser.getCurrentUser(),planObjectID);
    }

}
