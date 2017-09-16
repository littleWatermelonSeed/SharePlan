package com.sayhellototheworld.littlewatermelon.shareplan.view.centerplaza_view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sayhellototheworld.littlewatermelon.shareplan.R;
import com.sayhellototheworld.littlewatermelon.shareplan.customwidget.LiTopBar;
import com.sayhellototheworld.littlewatermelon.shareplan.view.base_activity.BaseSlideBcakStatusActivity;

public class WriteActivity extends BaseSlideBcakStatusActivity implements View.OnClickListener{

    private LiTopBar mLiTopBar;
    private EditText editText_title;
    private EditText editText_content;
    private TextView textView_beginTime;
    private TextView textView_endTime;
    private TextView textView_place;
    private TextView textView_permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        init();
    }

    private void init(){
        initWidget();
    }

    private void initWidget(){
        mLiTopBar = (LiTopBar)findViewById(R.id.activity_write_topbar);
        editText_title = (EditText)findViewById(R.id.activity_write_editTitle);
        editText_content = (EditText)findViewById(R.id.activity_write_editContent);
        textView_beginTime = (TextView)findViewById(R.id.activity_write_beginTime);
        textView_beginTime.setOnClickListener(this);
        textView_endTime = (TextView)findViewById(R.id.activity_write_endTime);
        textView_endTime.setOnClickListener(this);
        textView_place = (TextView)findViewById(R.id.activity_write_place);
        textView_place.setOnClickListener(this);
        textView_permission = (TextView)findViewById(R.id.activity_write_permission);
        textView_permission.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_write_beginTime:
                break;
            case R.id.activity_write_endTime:
                break;
            case R.id.activity_write_place:
                break;
            case R.id.activity_write_permission:
                break;
        }
    }

    public static void startWriteActivity(Context context){
        ((Activity)context).startActivity(new Intent(context,WriteActivity.class));
    }

}
