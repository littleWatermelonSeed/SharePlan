package com.sayhellototheworld.littlewatermelon.shareplan.view.centerplaza_view.centerplaza_fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sayhellototheworld.littlewatermelon.shareplan.R;

public class UserFragment extends Fragment {

    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_user, null);
        return mView;
    }


}
