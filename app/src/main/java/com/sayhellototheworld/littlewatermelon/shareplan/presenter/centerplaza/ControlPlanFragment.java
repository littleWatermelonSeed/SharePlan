package com.sayhellototheworld.littlewatermelon.shareplan.presenter.centerplaza;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.sayhellototheworld.littlewatermelon.shareplan.adapter.PlanListAdapter;
import com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.bean.PlanBean;
import com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.data_manager.BmobManagePlan;
import com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.shareplan.model.localDB.data_manager.LocalManagePlan;
import com.sayhellototheworld.littlewatermelon.shareplan.model.localDB.table.TablePlan;
import com.sayhellototheworld.littlewatermelon.shareplan.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.shareplan.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.shareplan.util.NetWorkUtil;
import com.sayhellototheworld.littlewatermelon.shareplan.view.centerplaza_view.centerplaza_fragment.PlanFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by 123 on 2017/9/25.
 */

public class ControlPlanFragment implements OnLoadmoreListener, OnRefreshListener {

    private Context mContext;
    private ListView listView;
    private TextView textView_empty;
    private SmartRefreshLayout mRefreshLayout;
    private static PlanFragment mPlanFragment;

    private static boolean PFshow = false;
    private MyUserBean curUser;
    private PlanListAdapter mAdapter;
    private List<TablePlan> showTablePlanList;
    private List<TablePlan> localObjectIDs;
    private List<String> objectIDs;
    private int localIndex = 0;
    private int bmobIndex = 0;
    private int planIndex = 0;
    private boolean loading = true;
    private boolean firstNetwork = true;
    private boolean firstNoNetwork = true;

    private LocalManagePlan lmp;
    private BmobManagePlan bmp;

    public ControlPlanFragment(Context context, PlanFragment planFragment, TextView textView_empty,
                               ListView listView, SmartRefreshLayout refreshLayout) {
        mContext = context;
        mPlanFragment = planFragment;
        this.listView = listView;
        this.textView_empty = textView_empty;
        mRefreshLayout = refreshLayout;

        localObjectIDs = new ArrayList<>();
        showTablePlanList = new ArrayList<>();
        objectIDs = new ArrayList<>();
        PFshow = true;
        curUser = BmobManageUser.getCurrentUser();

        lmp = new LocalManagePlan();
        bmp = new BmobManagePlan(mContext);

        mAdapter = new PlanListAdapter(mContext, showTablePlanList);
        listView.setAdapter(mAdapter);
    }

    public void syncPlan() {
        if (NetWorkUtil.isNetworkAvailable(mContext)) {
            networkDo();
        } else {
            noNetworkDo();
        }
    }

    private void noNetworkDo() {
        MyToastUtil.showToast("没网咯，只能加载本地数据咯~");
        if(firstNoNetwork){
            objectIDs.clear();
            showTablePlanList.clear();
            firstNoNetwork = false;
            localIndex = 0;
            planIndex = 0;
            bmobIndex = 0;
        }
        firstNetwork = true;
        localObjectIDs = lmp.queryAllObjectID(localIndex, BmobManageUser.getCurrentUser().getUsername());
        for (TablePlan t : localObjectIDs) {
            objectIDs.add(t.getObjectID());
        }
        showPlanList(loading);
    }

    private void networkDo() {
        if (firstNetwork) {
            objectIDs.clear();
            showTablePlanList.clear();
            localIndex = 0;
            planIndex = 0;
            bmobIndex = 0;
            firstNetwork = false;
        }
        firstNoNetwork = true;
        bmp.queryAllObjectID(planIndex, new FindListener<PlanBean>() {
            @Override
            public void done(final List<PlanBean> list,final BmobException e) {
                if (e == null){
                    queryOidSuccess(list);
                }else {
                    BmobException(e);
                }
            }
        });
    }

    private void queryOidSuccess(final List<PlanBean> list) {
        final List<String> count = new ArrayList<>();
        for (final PlanBean p:list){
            String oID = p.getObjectId();
            if (!lmp.existPlan(oID)){
                bmp.queryPlanByID(oID, new QueryListener<PlanBean>() {
                    @Override
                    public void done(PlanBean planBean,final BmobException e) {
                        if (e == null){
                            TablePlan tablePlan = bmobToLocalPlan(planBean);
                            showTablePlanList.add(tablePlan);
                            lmp.savePlan(tablePlan);
                            bmobIndex++;
                            count.add("a");
                            if (count.size() == list.size()){
                                showPlanList(loading);
                                count.clear();
                                firstNetwork = false;
                            }
                        }else {
                            BmobException(e);
                        }
                    }
                });
            }else {
                objectIDs.add(oID);
                count.add("a");
                if (count.size() == list.size()){
                    showPlanList(loading);
                    count.clear();
                    firstNetwork = false;
                }
            }
        }

        if (list.size() <= 0){
            MyToastUtil.showToast("到底啦,刷也没有啦~");
            showPlanList(loading);
        }
    }

    private void showPlanList(final boolean loading) {
        if (objectIDs.size() <= 0 && showTablePlanList.size() <= 0) {
            textView_empty.setText(PlanFragment.LIST_EMPTY);
            textView_empty.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            finishRefreshLayout(true);
        } else {
            textView_empty.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            String[] s = new String[objectIDs.size() - localIndex];
            int n = 0;
            for (int i = localIndex; i < objectIDs.size(); i++) {
                localIndex++;
                s[n] = objectIDs.get(i);
                n++;
            }
            planIndex = localIndex + bmobIndex;
            List<TablePlan> t = lmp.queryPlanByObjectID(s);
            showTablePlanList.addAll(t);
            sortShowPlanList(showTablePlanList);
            mAdapter.notifyDataSetChanged();
            if (!loading) {
                listView.setSelection(0);
            } else {
                listView.setSelection(showTablePlanList.size());
            }
            finishRefreshLayout(true);
        }
    }

    private void sortShowPlanList(List<TablePlan> showTablePlanList) {
        Collections.sort(showTablePlanList, new Comparator<TablePlan>() {
            @Override
            public int compare(TablePlan o1, TablePlan o2) {
                if (o1.getCreateTime().before(o2.getCreateTime()))
                    return 1;
                return -1;
            }
        });
    }

    @Override
    public void onLoadmore(final RefreshLayout refreshlayout) {
        loading = true;
        syncPlan();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        loading = false;
        firstNetwork = true;
        firstNoNetwork = true;
        syncPlan();
    }

    private void finishRefreshLayout(boolean success) {
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.finishRefresh(success);
        }
        if (mRefreshLayout.isLoading()) {
            mRefreshLayout.finishLoadmore(success);
        }
    }

    private void BmobException(final BmobException e) {
        ((FragmentActivity)mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                finishRefreshLayout(false);
                BmobExceptionUtil.dealWithException(mContext, e);
            }
        });
    }

    private TablePlan bmobToLocalPlan(PlanBean p) {
        TablePlan tablePlan = new TablePlan();
        tablePlan.setObjectID(p.getObjectId());
        tablePlan.setBeginTime(p.getBeginTime());
        tablePlan.setContent(p.getContent());
        tablePlan.setCreateTime(p.getCreateTime());
        tablePlan.setEndTime(p.getEndTime());
        tablePlan.setLimit(p.getLimit());
        tablePlan.setStatue(p.getStatue());
        tablePlan.setTitle(p.getTitle());
        tablePlan.setUserID(BmobManageUser.getCurrentUser().getUsername());
        return tablePlan;
    }

    public static boolean syncPlanFragment() {
        if (!PlanFragment.PFshow) {
            return false;
        }
        MyUserBean userBean = BmobManageUser.getCurrentUser();
        mPlanFragment.showPlan(userBean);
        return true;
    }

}
