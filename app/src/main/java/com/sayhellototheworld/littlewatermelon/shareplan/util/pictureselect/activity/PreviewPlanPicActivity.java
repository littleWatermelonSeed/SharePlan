package com.sayhellototheworld.littlewatermelon.shareplan.util.pictureselect.activity;

import android.os.Bundle;

import com.sayhellototheworld.littlewatermelon.shareplan.R;
import com.sayhellototheworld.littlewatermelon.shareplan.view.base_activity.BaseNoStatusActivity;

public class PreviewPlanPicActivity extends BaseNoStatusActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_plan_pic);
        init();
    }

    private void init(){
        baseActivityManager.addActivityToUserMap(this,getClass().getSimpleName());
    }

}
