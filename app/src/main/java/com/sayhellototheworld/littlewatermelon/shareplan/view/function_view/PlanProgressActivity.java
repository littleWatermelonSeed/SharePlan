package com.sayhellototheworld.littlewatermelon.shareplan.view.function_view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sayhellototheworld.littlewatermelon.shareplan.R;
import com.sayhellototheworld.littlewatermelon.shareplan.customwidget.MyPopupWindow;
import com.sayhellototheworld.littlewatermelon.shareplan.my_interface.base_interface.BaseActivityDo;
import com.sayhellototheworld.littlewatermelon.shareplan.presenter.function.ControPlanProgress;
import com.sayhellototheworld.littlewatermelon.shareplan.util.pictureselect.Image;
import com.sayhellototheworld.littlewatermelon.shareplan.util.pictureselect.activity.ShowPictureActivity;
import com.sayhellototheworld.littlewatermelon.shareplan.view.base_activity.BaseSlideBcakStatusActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class PlanProgressActivity extends BaseSlideBcakStatusActivity implements BaseActivityDo,View.OnClickListener {

    private LinearLayout planProgressBack;
    private TextView planProgressStauteText;
    private TextView planProgressTitle;
    private ImageView planProgressWriteProgress;
    private SmartRefreshLayout planProgressRefresh;
    private TextView planProgressListEmpty;
    private ListView planProgressListView;
    private MyPopupWindow popupWindow;

    private String planObjectID;
    private ControPlanProgress cpp;
    private String planTitle;
    private List<String> imagePaths;
    private int planStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_progress);
        init();
    }


    @Override
    public void init() {
        initWidget();
        initParam();
        initShow();
    }

    @Override
    public void initWidget() {
        planProgressBack = (LinearLayout) findViewById(R.id.activity_plan_progress_back);
        planProgressBack.setOnClickListener(this);
        planProgressStauteText = (TextView) findViewById(R.id.activity_plan_progress_statue);
        planProgressTitle = (TextView) findViewById(R.id.activity_plan_progress_title);
        planProgressWriteProgress = (ImageView) findViewById(R.id.activity_plan_progress_write_progress);
        planProgressWriteProgress.setOnClickListener(this);
        planProgressRefresh = (SmartRefreshLayout) findViewById(R.id.activity_plan_progress_refresh);
        planProgressListEmpty = (TextView) findViewById(R.id.activity_plan_progress_listEmpty);
        planProgressListView = (ListView) findViewById(R.id.activity_plan_progress_listView);
    }

    @Override
    public void initParam() {
        Intent intent = getIntent();
        planObjectID = intent.getStringExtra("planObjectID");
        planTitle = intent.getStringExtra("planTitle");
        planStatus = intent.getIntExtra("planStatus",0);
        baseActivityManager.addActivityToUserMap(this,getClass().getSimpleName());
        imagePaths = new ArrayList<>();
        cpp = new ControPlanProgress(this,planProgressRefresh,planProgressListView,imagePaths,planObjectID,planStatus,this);
        planProgressRefresh.setOnRefreshListener(cpp);
        planProgressRefresh.setOnLoadmoreListener(cpp);
    }

    @Override
    public void initShow() {
        planProgressTitle.setText(planTitle);
        if (planStatus == -1 ) {
            planProgressStauteText.setText("未完成");
            planProgressStauteText.setTextColor(getResources().getColor(R.color.plan_text_statue_unfinished));
        } else if (planStatus == 0) {
            planProgressStauteText.setText("进行中");
            planProgressStauteText.setTextColor(getResources().getColor(R.color.plan_text_statue_ing));
        } else if (planStatus == 1) {
            planProgressStauteText.setText("已完成");
            planProgressStauteText.setTextColor(getResources().getColor(R.color.plan_text_statue_finished));
        }
    }

    public static void startPlanProgressActivity(Context context,String planObjectID,String planTitle,int planStatus){
        Intent intent = new Intent(context,PlanProgressActivity.class);
        intent.putExtra("planObjectID",planObjectID);
        intent.putExtra("planTitle",planTitle);
        intent.putExtra("planStatus",planStatus);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_plan_progress_back:
                finish();
                break;
            case R.id.activity_plan_progress_write_progress:
                cpp.writeProgress();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ShowPictureActivity.SHOWPICTURE_REQUESTCODE){
            if (resultCode == RESULT_OK){
                List<Image> images = (List<Image>) data.getSerializableExtra(ShowPictureActivity.RESULT_DATA_KEY);
                for (int i = 0;i < images.size();i++){
                    if (imagePaths.size() >= 5){
                        break;
                    }
                    imagePaths.add(images.get(i).getPath());
                    if (imagePaths.size() > 0){
                        cpp.imageUrlsChanged();
                    }
                }
            }
        }
    }

    public void listEmpty(boolean empty){
        if (empty){
            planProgressListEmpty.setVisibility(View.VISIBLE);
            planProgressListEmpty.setText("计划进度为空");
        }else {
            planProgressListEmpty.setVisibility(View.GONE);
        }
    }

}
