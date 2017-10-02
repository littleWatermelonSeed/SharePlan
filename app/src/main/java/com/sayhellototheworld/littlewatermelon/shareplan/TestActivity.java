package com.sayhellototheworld.littlewatermelon.shareplan;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.sayhellototheworld.littlewatermelon.shareplan.util.SysUtil;
import com.sayhellototheworld.littlewatermelon.shareplan.view.base_activity.BaseStatusActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class TestActivity extends BaseStatusActivity implements View.OnClickListener {

    private CircleImageView mCircleImageView;
    private Button button_shangchuan;
    private Button button_xiazai;

    private String objectID;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        init();
    }

    private void init() {
        mCircleImageView = (CircleImageView) findViewById(R.id.activity_test_headPortrait);
        button_shangchuan = (Button) findViewById(R.id.activity_test_shangchuan);
        button_shangchuan.setOnClickListener(this);
        button_xiazai = (Button) findViewById(R.id.activity_test_xiazai);
        button_xiazai.setOnClickListener(this);
        baseActivityManager.addActivityToUserMap(this,getClass().getSimpleName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_test_shangchuan:
                shangchuan2();
                break;
            case R.id.activity_test_xiazai:
                xiazai2();
                break;
        }
    }

    private void shangchuan2(){
        Log.i("niyuanjie","颜色URL为：" + SysUtil.getResourceUri(R.color.gray));
    }

    private void xiazai2(){
//        Log.i("niyuanjie","当前activity名字：" + RealTimeData.getInstance().getTopActivityName());
    }


}
