package com.sayhellototheworld.littlewatermelon.shareplan.view.centerplaza_view.centerplaza_fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sayhellototheworld.littlewatermelon.shareplan.R;
import com.sayhellototheworld.littlewatermelon.shareplan.util.SysUtils;

public class HomePageFragment extends Fragment {

    private View mView;
    private LinearLayout parentLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home_page, container, false);
        init();
        return mView;
    }

    private void init(){
        initWidget();
        initLayoutMargin();
    }

    private void initWidget(){
        parentLayout = (LinearLayout)mView.findViewById(R.id.fragment_home_page_parent);
    }

    private void initLayoutMargin(){
        SysUtils.setLayoutMargin(getActivity(),parentLayout);
    }



}