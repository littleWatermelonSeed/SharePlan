package com.sayhellototheworld.littlewatermelon.shareplan;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.bean.CommentBean;
import com.sayhellototheworld.littlewatermelon.shareplan.util.BmobExceptionUtil;
import com.sayhellototheworld.littlewatermelon.shareplan.view.base_activity.BaseStatusActivity;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
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
        BmobQuery<CommentBean> query = new BmobQuery<>();
        query.findObjects(new FindListener<CommentBean>() {
            @Override
            public void done(List<CommentBean> list, BmobException e) {
                if (e == null){
                    Log.i("niyuanjie","查询成功 长度为：" + list.size());
                }else {
                    Log.i("niyuanjie","查询失败");
                    BmobExceptionUtil.dealWithException(SPApplication.getAppContext(),e);
                }
            }
        });
    }

    private void xiazai2(){
//        Log.i("niyuanjie","当前activity名字：" + RealTimeData.getInstance().getTopActivityName());
    }


}
