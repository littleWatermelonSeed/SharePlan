package com.sayhellototheworld.littlewatermelon.shareplan.view.centerplaza_view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.sayhellototheworld.littlewatermelon.shareplan.R;
import com.sayhellototheworld.littlewatermelon.shareplan.adapter.PlanImageAdapter;
import com.sayhellototheworld.littlewatermelon.shareplan.customwidget.DialogConfirm;
import com.sayhellototheworld.littlewatermelon.shareplan.customwidget.LiTopBar;
import com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.bean.PlanBean;
import com.sayhellototheworld.littlewatermelon.shareplan.model.localDB.table.TablePlan;
import com.sayhellototheworld.littlewatermelon.shareplan.my_interface.base_interface.BaseActivityDo;
import com.sayhellototheworld.littlewatermelon.shareplan.presenter.centerplaza.ControlWrite;
import com.sayhellototheworld.littlewatermelon.shareplan.util.MyToastUtil;
import com.sayhellototheworld.littlewatermelon.shareplan.util.pictureselect.Image;
import com.sayhellototheworld.littlewatermelon.shareplan.util.pictureselect.activity.ShowPictureActivity;
import com.sayhellototheworld.littlewatermelon.shareplan.view.base_activity.BaseStatusActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WriteActivity extends BaseStatusActivity implements View.OnClickListener,BaseActivityDo{

    private LiTopBar mLiTopBar;
    private EditText editText_title;
    private EditText editText_content;
    private TextView textView_beginTime;
    private TextView textView_endTime;
    private TextView textView_place;
    private TextView textView_permission;
    private GridView gridView;

    private List<String> imagePath;
    private PlanImageAdapter mPlanImageAdapter;
    private ControlWrite mControlWrite;
    private TablePlan mTablePlan;
    private PlanBean mPlanBean;

    private String title = "";
    private String content = "";
    private Date stratTime;
    private Date endTime;
    private boolean permission = true;
    private String loaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        init();
    }

    @Override
    public void init(){
        initWidget();
        initParam();
        initShow();
    }
    @Override
    public void initWidget(){
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
        gridView = (GridView) findViewById(R.id.activity_write_cardView);
        mLiTopBar.setLeftContainerListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogConfirm.newInstance("提示", "你还没有提交计划，确定退出?", new DialogConfirm.CancleAndOkDo() {
                    @Override
                    public void cancle() {

                    }

                    @Override
                    public void ok() {
                        finish();
                    }
                }).setMargin(60)
                        .setOutCancel(false)
                        .show(getSupportFragmentManager());
            }
        });
        mLiTopBar.setRightContainerListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!getValues()){
                    return;
                }
                mControlWrite.submitPlan(mPlanBean,imagePath);
            }
        });
    }

    private boolean getValues(){
        title = editText_title.getText().toString();
        title = title.replaceAll("\\s","");
        if (title.equals("")){
            MyToastUtil.showToast("计划标题不能为空");
            return false;
        }
        content = editText_content.getText().toString();
        content = content.replaceAll("\\s","");
        if (content.equals("")){
            MyToastUtil.showToast("计划内容不能为空");
            return false;
        }
        if (stratTime == null || endTime == null){
            MyToastUtil.showToast("计划的开始时间/结束时间不能为空");
            return false;
        }
        mPlanBean.setTitle(title);
        mPlanBean.setContent(content);
        mPlanBean.setBeginTime(stratTime);
        mPlanBean.setEndTime(endTime);
        mPlanBean.setLimit(permission);
        mPlanBean.setStatue(0);
        mPlanBean.setCreateTime(new Date());
        return true;
    }

    @Override
    public void initParam() {
        imagePath = new ArrayList<>();
        mTablePlan = new TablePlan();
        mPlanBean = new PlanBean();
        mControlWrite = new ControlWrite(this);
        mPlanImageAdapter = new PlanImageAdapter(this,imagePath,PlanImageAdapter.TYPE_WRITE_PLAN);
        baseActivityManager.addActivityToUserMap(this,getClass().getSimpleName());
    }

    @Override
    public void initShow() {
        gridView.setAdapter(mPlanImageAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_write_beginTime:
                getBeginTime();
                break;
            case R.id.activity_write_endTime:
                getEndTime();
                break;
            case R.id.activity_write_place:
                break;
            case R.id.activity_write_permission:
                setPermisssion();
                break;
        }
    }

    private void setPermisssion(){
        if (permission){
            permission = false;
            textView_permission.setBackgroundResource(R.drawable.radius_background_buttongreen1);
            textView_permission.setText("不公开");
        }else {
            permission = true;
            textView_permission.setBackgroundResource(R.drawable.radius_background_buttongreen4);
            textView_permission.setText("公开");
        }
    }

    private void getBeginTime(){
        TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                stratTime = date;
                if (endTime != null && stratTime.after(endTime)){
                    textView_endTime.setText("结束时间：");
                }
                String time;
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                time = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
                textView_beginTime.setText("开始时间：" + "  " + time);
            }
        }).setType(new boolean[]{true, true, true, false, false, false}).build();
        pvTime.setDate(Calendar.getInstance());
        pvTime.show();
    }

    private void getEndTime(){
        if (stratTime == null){
            MyToastUtil.showToast("请先设置开始时间");
            return;
        }
        TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                endTime = date;
                if (endTime.before(stratTime)){
                    textView_endTime.setText("结束时间：");
                    MyToastUtil.showToast("结束时间必须在开始时间之后，请重新设置");
                    return;
                }
                String time;
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                time = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
                textView_endTime.setText("结束时间：" + "  " + time);
            }
        }).setType(new boolean[]{true, true, true, false, false, false}).build();
        pvTime.setDate(Calendar.getInstance());
        pvTime.show();
    }

    public static void startWriteActivity(Context context){
        ((Activity)context).startActivity(new Intent(context,WriteActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ShowPictureActivity.SHOWPICTURE_REQUESTCODE){
            if (resultCode == RESULT_OK){
                List<Image> images = (List<Image>) data.getSerializableExtra(ShowPictureActivity.RESULT_DATA_KEY);
                for (int i = 0;i < images.size();i++){
                    if (imagePath.size() >= 5){
                        break;
                    }
                    imagePath.add(images.get(i).getPath());
                }
                mPlanImageAdapter.notifyDataSetChanged();
            }
        }
    }



}
