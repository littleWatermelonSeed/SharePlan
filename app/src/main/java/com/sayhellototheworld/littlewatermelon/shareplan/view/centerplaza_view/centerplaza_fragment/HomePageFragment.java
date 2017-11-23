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
import com.sayhellototheworld.littlewatermelon.shareplan.my_interface.base_interface.BaseActivityDo;
import com.sayhellototheworld.littlewatermelon.shareplan.presenter.centerplaza.ControHomeFragment;
import com.sayhellototheworld.littlewatermelon.shareplan.util.StatusBarUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

public class HomePageFragment extends Fragment implements BaseActivityDo{

    private View mView;
    private LinearLayout parentLayout;
    private SmartRefreshLayout mRefreshLayout;
    private ListView listView;
    private TextView textView_empty;

    private ControHomeFragment chf;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home_page, container, false);
        init();
        return mView;
    }

    @Override
    public void init(){
        initWidget();
        initParam();
        initShow();
    }

    @Override
    public void initWidget(){
        parentLayout = (LinearLayout)mView.findViewById(R.id.fragment_home_page_parent);
        mRefreshLayout = (SmartRefreshLayout)mView.findViewById(R.id.fragment_home_refreshLayout);
        mRefreshLayout.setEnableAutoLoadmore(false);
        listView = (ListView)mView.findViewById(R.id.fragment_home_listView);
        textView_empty = (TextView)mView.findViewById(R.id.fragment_home_empty_listView);
    }

    @Override
    public void initParam() {
        chf = new ControHomeFragment(getActivity(),this,mRefreshLayout,listView);
    }

    @Override
    public void initShow() {
        StatusBarUtils.setLayoutMargin(getActivity(),parentLayout);
    }

    public void listEmpty(boolean empty){
        if (empty){
            textView_empty.setVisibility(View.VISIBLE);
            textView_empty.setText("服务器没有数据哟~");
        }else {
            textView_empty.setVisibility(View.GONE);
        }
    }

}
