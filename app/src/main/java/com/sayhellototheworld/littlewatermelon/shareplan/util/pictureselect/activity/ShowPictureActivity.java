package com.sayhellototheworld.littlewatermelon.shareplan.util.pictureselect.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sayhellototheworld.littlewatermelon.shareplan.R;
import com.sayhellototheworld.littlewatermelon.shareplan.util.pictureselect.Folder;
import com.sayhellototheworld.littlewatermelon.shareplan.util.pictureselect.GetImageFromSDcard;
import com.sayhellototheworld.littlewatermelon.shareplan.util.pictureselect.GridAdapter;
import com.sayhellototheworld.littlewatermelon.shareplan.util.pictureselect.Image;
import com.sayhellototheworld.littlewatermelon.shareplan.util.pictureselect.PictureDataManager;
import com.sayhellototheworld.littlewatermelon.shareplan.view.base_activity.BaseNoStatusActivity;
import com.sayhellototheworld.littlewatermelon.shareplan.view.base_activity.MyActivityManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShowPictureActivity extends BaseNoStatusActivity {

    private RelativeLayout bottomLayout;
    private LinearLayout backLayout;
    private TextView textView_cancle;
    private TextView textView_preview;
    private TextView textView_choose;
    private GridView gridView;

    private TextClickListener listener;
    private PictureDataManager manager;
    private GridAdapter adapter;

    private boolean chooseAndBack = false;
    private int folderPosition;
    private Context interContext;
    private int target;

    public final static int SHOWPICTURE_REQUESTCODE = 0;
    public final static int SHOWPICTURE_RESULT = 1;
    public final static int TARGET_HEAD = 2;
    public final static int TARGET_PLAN = 3;
    public final static int TARGET_BACKGROUND = 4;
    public final static int RESULT_STATE_OK = 5;
    public final static int RESULT_STATE_NO = 6;
    public final static String RESULT_STATE_KEY = "resultState";
    public final static String POSITION_KEY = "position";
    public final static String RESULT_DATA_KEY = "result_imagePath";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_picture);
        init();
    }

    private void init() {
        manager = PictureDataManager.getManagerInstance();
        getTarget();
        getWidget();
        setWidgetListener();
        setGridViewAdapter();
        MyActivityManager.getDestoryed().addActivityToList(this);
        baseActivityManager.addActivityToUserMap(this,getClass().getSimpleName());
    }

    private void getTarget(){
        Intent intent = getIntent();
        target = intent.getIntExtra("target",-1);
    }

    private void getWidget() {
        bottomLayout = (RelativeLayout)findViewById(R.id.activity_show_picture_bottombar);
        backLayout = (LinearLayout) findViewById(R.id.activity_show_picture_backLayout);
        textView_cancle = (TextView) findViewById(R.id.activity_show_picture_cancle);
        textView_preview = (TextView) findViewById(R.id.activity_show_picture_preview);
        textView_choose = (TextView) findViewById(R.id.activity_show_picture_choose);
        gridView = (GridView) findViewById(R.id.activity_show_picture_gridView);
    }

    private void setWidgetListener() {
        listener = new TextClickListener();
        backLayout.setOnClickListener(listener);
        textView_cancle.setOnClickListener(listener);
        textView_preview.setOnClickListener(listener);
        textView_choose.setOnClickListener(listener);
    }

    private void setGridViewAdapter(){
        if (target == TARGET_HEAD){
            adapter = new GridAdapter(this,textView_choose,0,GridAdapter.STYLE_HEADPORTRAIT,1);
            bottomLayout.setVisibility(View.GONE);
        }else if (target == TARGET_PLAN){
            adapter = new GridAdapter(this,textView_choose,0,GridAdapter.STYLE_PLAN,5);
        }else if (target == TARGET_BACKGROUND){
            adapter = new GridAdapter(this,textView_choose,0,GridAdapter.STYLE_BACKGROUND,1);
            bottomLayout.setVisibility(View.GONE);
        }
        gridView.setAdapter(adapter);
    }

    public static void startShowPictureActivityForResult(final Context context, final int target ) {
        GetImageFromSDcard.loadImageForSDCard(context, new GetImageFromSDcard.ImageCallBack() {
            @Override
            public void onSuccess(List<Folder> folderList, List<Image> images) {
                PictureDataManager manager = PictureDataManager.getManagerInstance();
                manager.setFolderList(folderList);
                Intent intent = new Intent(context, ShowPictureActivity.class);
                intent.putExtra("target",target);
                if(target == TARGET_HEAD || target == TARGET_BACKGROUND){
                    context.startActivity(intent);
                }else if (target == TARGET_PLAN){
                    ((FragmentActivity) context).startActivityForResult(intent, SHOWPICTURE_REQUESTCODE);
                }
            }
        });
    }

    public static void startShowPictureActivityFromFolderList(Context context,int folderPosition) {
        Intent intent = new Intent();
        intent.putExtra(POSITION_KEY,folderPosition);
        ((Activity)context).setResult(RESULT_OK,intent);
        ((Activity)context).finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            doCancle();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == FolderListviewActivity.FOLDER_REQUESTCODE){
            folderPosition = data.getIntExtra(POSITION_KEY,-1);
            if(folderPosition == -1){
                return;
            }else if(folderPosition == -2){
                doCancle();
                return;
            }
            if (target == TARGET_HEAD){
                adapter = new GridAdapter(this,textView_choose,folderPosition,GridAdapter.STYLE_HEADPORTRAIT,1);
            }else if (target == TARGET_PLAN){
                adapter = new GridAdapter(this,textView_choose,folderPosition,GridAdapter.STYLE_PLAN);
            }else if (target == TARGET_BACKGROUND){
                adapter = new GridAdapter(this,textView_choose,folderPosition,GridAdapter.STYLE_BACKGROUND,1);
            }
            gridView.setAdapter(adapter);
        }else if (requestCode == ClipHeadPortraitActivity.REQUEST_CODE){
            if(resultCode == ClipHeadPortraitActivity.RESULT_CODE_FINISH){
                finish();
            }
        }
    }

    class TextClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.activity_show_picture_backLayout) {
                doBack();
            } else if (i == R.id.activity_show_picture_cancle) {
                doCancle();
            } else if (i == R.id.activity_show_picture_choose) {
                doChoose();
            } else if (i == R.id.activity_show_picture_preview) {
                doPreview();
            }
        }
    }

    private void doBack() {
        FolderListviewActivity.startFolderListviewActivityForResult(this);
    }

    private void doCancle() {
        clearPictureDataManager();
        finish();
    }

    private void doChoose() {

        if(manager.getCallbackImageSize() == 0){
            Toast.makeText(this,"你还没有选择图片",Toast.LENGTH_SHORT).show();
            return;
        }

        chooseAndBack = true;
        manager.setCallback(chooseAndBack);

        List<Image> images = manager.getCallbackImageList();
        List<Image> reslut = new ArrayList<Image>();
        for(Image image:images){
            reslut.add(image);
        }

        Intent intent = new Intent();
        intent.putExtra(RESULT_DATA_KEY, (Serializable) reslut);
        setResult(RESULT_OK,intent);
        clearPictureDataManager();
        finish();
    }

    private void doPreview() {

    }

    private void clearPictureDataManager(){
        if (manager != null){
            manager.clear();
            manager = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (target == TARGET_HEAD || target == TARGET_BACKGROUND){
            clearPictureDataManager();
        }
    }

}
