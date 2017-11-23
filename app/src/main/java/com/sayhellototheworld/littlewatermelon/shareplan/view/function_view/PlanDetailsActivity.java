package com.sayhellototheworld.littlewatermelon.shareplan.view.function_view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sayhellototheworld.littlewatermelon.shareplan.R;
import com.sayhellototheworld.littlewatermelon.shareplan.adapter.PlanImageAdapter;
import com.sayhellototheworld.littlewatermelon.shareplan.customwidget.MyGridView;
import com.sayhellototheworld.littlewatermelon.shareplan.customwidget.MyListView;
import com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.bean.MyUserBean;
import com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.shareplan.model.localDB.table.TablePlan;
import com.sayhellototheworld.littlewatermelon.shareplan.model.local_file.ManageFile;
import com.sayhellototheworld.littlewatermelon.shareplan.my_interface.base_interface.BaseActivityDo;
import com.sayhellototheworld.littlewatermelon.shareplan.my_interface.function_interface.PlanDetailsInitDo;
import com.sayhellototheworld.littlewatermelon.shareplan.presenter.function.ControlPlanDetails;
import com.sayhellototheworld.littlewatermelon.shareplan.util.PictureUtil;
import com.sayhellototheworld.littlewatermelon.shareplan.util.TimeFormatUtil;
import com.sayhellototheworld.littlewatermelon.shareplan.view.base_activity.BaseSlideBcakStatusActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlanDetailsActivity extends BaseSlideBcakStatusActivity implements BaseActivityDo,View.OnClickListener,PlanDetailsInitDo {

    private LinearLayout detailsBack;
    private TextView detailsBackText;
    private ImageView detailsMore;
    private ImageView detailsLimitIcon;
    private TextView detailsLimit;
    private TextView detailsTitle;
    private CircleImageView detailsHeadPortrait;
    private TextView detailsName;
    private TextView detailsCreateTime;
    private TextView detailsContent;
    private MyGridView detailsImage;
    private TextView detailsStatue;
    private RelativeLayout detailsProgress;
    private TextView detailsComment;
    private TextView detailsLikes;
    private MyListView detailsCommentList;
    private SmartRefreshLayout refreshLayout;

    private String objectID;
    private String planTitle;
    private List<String> imageUrls;
    private PlanImageAdapter mAdapter;
    private int planStatus;

    private ControlPlanDetails cpd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_details);
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
        detailsBack = (LinearLayout) findViewById(R.id.activity_plan_details_back);
        detailsBack.setOnClickListener(this);
        detailsBackText = (TextView) findViewById(R.id.activity_plan_details_back_text);
        detailsMore = (ImageView) findViewById(R.id.activity_plan_details_more);
        detailsMore.setOnClickListener(this);
        detailsLimitIcon = (ImageView) findViewById(R.id.activity_plan_details_limit_icon);
        detailsLimit = (TextView) findViewById(R.id.activity_plan_details_limit);
        detailsTitle = (TextView) findViewById(R.id.activity_plan_details_title);
        detailsHeadPortrait = (CircleImageView) findViewById(R.id.activity_plan_details_head_portrait);
        detailsName = (TextView) findViewById(R.id.activity_plan_details_name);
        detailsCreateTime = (TextView) findViewById(R.id.activity_plan_details_create_time);
        detailsContent = (TextView) findViewById(R.id.activity_plan_details_content);
        detailsImage = (MyGridView) findViewById(R.id.activity_plan_details_image);
        detailsStatue = (TextView) findViewById(R.id.activity_plan_details_statue);
        detailsProgress = (RelativeLayout) findViewById(R.id.activity_plan_details_progress);
        detailsProgress.setOnClickListener(this);
        detailsComment = (TextView) findViewById(R.id.activity_plan_details_comment);
        detailsLikes = (TextView) findViewById(R.id.activity_plan_details_likes);
        detailsCommentList = (MyListView) findViewById(R.id.activity_plan_details_comment_list);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.activity_plan_details_refreshLayout);
    }

    @Override
    public void initParam() {
        Intent intent = getIntent();
        imageUrls = new ArrayList<>();
        mAdapter = new PlanImageAdapter(this,imageUrls,PlanImageAdapter.TYPE_READ_PLAN);
        detailsImage.setAdapter(mAdapter);
        objectID = intent.getStringExtra("objectID");
        refreshLayout.setDisableContentWhenRefresh(true);
        refreshLayout.setDisableContentWhenLoading(true);

        baseActivityManager.addActivityToUserMap(this,getClass().getSimpleName());
        cpd = new ControlPlanDetails(this,objectID,refreshLayout,this);
    }

    @Override
    public void initShow() {

    }

    public static void startPlanDetailsActivity(Context context, String objectID){
        Intent intent = new Intent(context,PlanDetailsActivity.class);
        intent.putExtra("objectID",objectID);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_plan_details_back:
                finish();
                break;
            case R.id.activity_plan_details_more:
                break;
            case R.id.activity_plan_details_progress:
                PlanProgressActivity.startPlanProgressActivity(this,objectID,planTitle,planStatus);
                break;
        }
    }

    @Override
    public void initShow(TablePlan tablePlan) {
        if (tablePlan.isLimit()){
            detailsLimitIcon.setImageResource(R.drawable.limit_open);
            detailsLimit.setText("公开");
        }else {
            detailsLimitIcon.setImageResource(R.drawable.limit_close);
            detailsLimit.setText("仅自己可见");
        }
        planTitle = tablePlan.getTitle();
        detailsTitle.setText(tablePlan.getTitle());
        detailsName.setText(BmobManageUser.getCurrentUser().getNickName());
        detailsCreateTime.setText(TimeFormatUtil.DateToRealTime(tablePlan.getCreateTime()));
        detailsContent.setText(tablePlan.getContent());
        if (tablePlan.getStatue() != 1 && !TimeFormatUtil.compareDate(tablePlan.getEndTime(), new Date())) {
            detailsStatue.setText("未完成");
            detailsStatue.setTextColor(getResources().getColor(R.color.plan_text_statue_unfinished));
            planStatus = -1;
        } else if (tablePlan.getStatue() == 0) {
            detailsStatue.setText("进行中");
            detailsStatue.setTextColor(getResources().getColor(R.color.plan_text_statue_ing));
            planStatus = 0;
        } else if (tablePlan.getStatue() == 1) {
            detailsStatue.setText("已完成");
            detailsStatue.setTextColor(getResources().getColor(R.color.plan_text_statue_finished));
            planStatus = 1;
        }
        MyUserBean userBean = BmobManageUser.getCurrentUser();
        if (userBean.getHeadPortrait() == null || userBean.getHeadPortrait().getUrl() == null) {
            Glide.with(this)
                    .load(R.drawable.head_log1)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .dontAnimate()
                    .into(detailsHeadPortrait);
        } else {
            Glide.with(this)
                    .load(ManageFile.getHeadPortrait(PictureUtil.getPicNameFromUrl(userBean.getHeadPortrait().getUrl())))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .dontAnimate()
                    .error(R.drawable.head_log1)
                    .into(detailsHeadPortrait);
        }
        detailsLikes.setText(tablePlan.getLikes() + "");
    }

    @Override
    public void showComment() {

    }

    @Override
    public void showImage(List<String> imageUrls) {
        this.imageUrls.addAll(imageUrls);
        mAdapter.notifyDataSetChanged();
    }


}
