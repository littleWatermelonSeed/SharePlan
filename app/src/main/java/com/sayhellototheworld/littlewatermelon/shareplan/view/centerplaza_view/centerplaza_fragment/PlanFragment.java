package com.sayhellototheworld.littlewatermelon.shareplan.view.centerplaza_view.centerplaza_fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sayhellototheworld.littlewatermelon.shareplan.R;
import com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.shareplan.presenter.centerplaza.ControlPlanFragment;
import com.sayhellototheworld.littlewatermelon.shareplan.util.StatusBarUtils;
import com.sayhellototheworld.littlewatermelon.shareplan.view.centerplaza_view.WriteActivity;
import com.sayhellototheworld.littlewatermelon.shareplan.view.user_view.LoginActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

public class PlanFragment extends Fragment implements View.OnClickListener{

    private View mView;
    private LinearLayout parentLayout;
    private SmartRefreshLayout mRefreshLayout;
    private ListView listView;
    private TextView textView_empty;

    private ControlPlanFragment cpf;
    public static boolean PFshow = false;

    public final static String LIST_EMPTY = "你还没有计划哟，点击马上计划计划>>";
    public final static String NO_LOGIN = "你还没有登录,点击马上去登录>>";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_plan, container, false);
        init();
        return mView;
    }

    private void init(){
        initWidget();
        initParams();
        initShow();
    }

    private void initWidget(){
        parentLayout = (LinearLayout) mView.findViewById(R.id.fragment_plan_parent);
        listView = (ListView) mView.findViewById(R.id.fragment_plan_listView);
        textView_empty = (TextView)mView.findViewById(R.id.fragment_plan_empty_listView);
        mRefreshLayout = (SmartRefreshLayout)mView.findViewById(R.id.fragment_plan_refreshLayout);
        mRefreshLayout.setEnableAutoLoadmore(false);
        textView_empty.setOnClickListener(this);
    }

    private void initParams(){
        PFshow = true;
        mRefreshLayout.setDisableContentWhenRefresh(true);
        mRefreshLayout.setDisableContentWhenLoading(true);
        cpf = new ControlPlanFragment(getActivity(),this,textView_empty,listView,mRefreshLayout);
    }

    private void initShow(){
        StatusBarUtils.setLayoutMargin(getActivity(),parentLayout);
        showPlan(BmobManageUser.getCurrentUser());
    }

    public void showPlan(MyUserBean myUserBean){
        if (myUserBean == null){
            mRefreshLayout.setEnableLoadmore(false);
            mRefreshLayout.setEnableRefresh(false);
            textView_empty.setText(NO_LOGIN);
            textView_empty.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }else {
            mRefreshLayout.setEnableLoadmore(true);
            mRefreshLayout.setEnableRefresh(true);
            textView_empty.setText(LIST_EMPTY);
            textView_empty.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            mRefreshLayout.setOnLoadmoreListener(cpf);
            mRefreshLayout.setOnRefreshListener(cpf);
            mRefreshLayout.autoRefresh();
        }
    }

    @Override
    public void onClick(View v) {
        if (BmobManageUser.getCurrentUser() == null){
            LoginActivity.startLoginActivity(getActivity());
        }else {
            WriteActivity.startWriteActivity(getActivity());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PFshow = false;
    }

}
