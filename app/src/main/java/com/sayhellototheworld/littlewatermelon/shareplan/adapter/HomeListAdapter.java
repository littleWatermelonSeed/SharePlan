package com.sayhellototheworld.littlewatermelon.shareplan.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sayhellototheworld.littlewatermelon.shareplan.R;
import com.sayhellototheworld.littlewatermelon.shareplan.adapter.bean.HomePlanBean;
import com.sayhellototheworld.littlewatermelon.shareplan.customwidget.DialogConfirm;
import com.sayhellototheworld.littlewatermelon.shareplan.customwidget.MyGridView;
import com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.data_manager.BmobManageUser;
import com.sayhellototheworld.littlewatermelon.shareplan.presenter.centerplaza.ControHomeFragment;
import com.sayhellototheworld.littlewatermelon.shareplan.util.TimeFormatUtil;
import com.sayhellototheworld.littlewatermelon.shareplan.view.function_view.WriteCommentActivity;
import com.sayhellototheworld.littlewatermelon.shareplan.view.user_view.LoginActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 123 on 2017/10/11.
 */

public class HomeListAdapter extends BaseAdapter{

    private Context context;
    private ControHomeFragment chf;
    private LayoutInflater inflater;
    private List<HomePlanBean> mPlanBeen;
    private List<String> likesID;
    private ClickListener mListener;

    public HomeListAdapter(Context context,ControHomeFragment chf, List<HomePlanBean> planBeen) {
        this.context = context;
        this.chf = chf;
        mPlanBeen = planBeen;
        likesID = new ArrayList<>();
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mPlanBeen.size();
    }

    @Override
    public Object getItem(int position) {
        return mPlanBeen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.adapter_listview_home,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
            AutoUtils.autoSize(convertView);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(mPlanBeen.get(position).getTitle());
        viewHolder.content.setText(mPlanBeen.get(position).getContent());
        viewHolder.createTime.setText(TimeFormatUtil.DateToRealTime(TimeFormatUtil.bmobDateToDate(mPlanBeen.get(position).getCreateTime())));
        viewHolder.name.setText(mPlanBeen.get(position).getUserName());
        viewHolder.likeNum.setText(mPlanBeen.get(position).getStars() + "");
        viewHolder.commentNum.setText(mPlanBeen.get(position).getCommentNum() + "");

        mListener = new ClickListener(position,viewHolder.likeText,viewHolder.likeIcon,viewHolder.likeNum);
        viewHolder.comment.setOnClickListener(mListener);
        viewHolder.like.setOnClickListener(mListener);

        if (!mPlanBeen.get(position).getUserHeadPortraitUrl().equals("-")){
            Glide.with(context)
                    .load(mPlanBeen.get(position).getUserHeadPortraitUrl())
                    .dontAnimate()
                    .into(viewHolder.headPortrait);
        }else {
            Glide.with(context)
                    .load(R.drawable.head_log1)
                    .dontAnimate()
                    .into(viewHolder.headPortrait);
        }
        viewHolder.image.setAdapter(null);
        if (mPlanBeen.get(position).getImageUrls() != null){
            PlanImageAdapter adapter = new PlanImageAdapter(context,mPlanBeen.get(position).getImageUrls(),PlanImageAdapter.TYPE_READ_PLAN);
            viewHolder.image.setAdapter(adapter);
        }
        return convertView;
    }

    class ClickListener implements View.OnClickListener{

        private int position;
        private TextView likeText;
        private ImageView likeIcon;
        private TextView likeNum;

        public ClickListener(int position, TextView likeText, ImageView likeIcon, TextView likeNum) {
            this.position = position;
            this.likeText = likeText;
            this.likeIcon = likeIcon;
            this.likeNum = likeNum;
        }

        @Override
        public void onClick(View v) {
            if (BmobManageUser.getCurrentUser() == null){
                DialogConfirm.newInstance("提示", "你还没有登录，是否马上前去登录？", new DialogConfirm.CancleAndOkDo() {
                    @Override
                    public void cancle() {

                    }

                    @Override
                    public void ok() {
                        LoginActivity.startLoginActivity(context);
                    }
                }).setMargin(60)
                        .setOutCancel(false)
                        .show(((FragmentActivity)context).getSupportFragmentManager());
                return;
            }
            switch (v.getId()){
                case R.id.adapter_listView_home_like:
                    if (likesID.contains(mPlanBeen.get(position).getPlanObjectID())){
                        likesID.remove(mPlanBeen.get(position).getPlanObjectID());
                        chf.changeLikePlan(mPlanBeen.get(position).getPlanObjectID(),-1);
                        likeIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.like));
                        likeText.setTextColor(context.getResources().getColor(R.color.black1));
                        likeNum.setTextColor(context.getResources().getColor(R.color.black1));
                        likeNum.setText(Integer.parseInt(likeNum.getText().toString()) - 1 + "");
                    }else {
                        likesID.add(mPlanBeen.get(position).getPlanObjectID());
                        chf.changeLikePlan(mPlanBeen.get(position).getPlanObjectID(),1);
                        likeIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.liked));
                        likeText.setTextColor(context.getResources().getColor(R.color.liked));
                        likeNum.setTextColor(context.getResources().getColor(R.color.liked));
                        likeNum.setText(Integer.parseInt(likeNum.getText().toString()) + 1 + "");
                    }
                    break;
                case R.id.adapter_listView_home_comment:
                    WriteCommentActivity.startWriteCommentActivity(context,mPlanBeen.get(position).getPlanObjectID());
                    break;
            }
        }
    }

    class ViewHolder{
        private View convertView;
        private TextView title;
        private CircleImageView headPortrait;
        private TextView name;
        private TextView createTime;
        private TextView content;
        private MyGridView image;
        private LinearLayout comment;
        private TextView commentNum;
        private LinearLayout like;
        private ImageView likeIcon;
        private TextView likeText;
        private TextView likeNum;

        public ViewHolder(View convertView) {
            this.convertView = convertView;
            title = (TextView)convertView.findViewById(R.id.adapter_listView_home_title);
            headPortrait = (CircleImageView) convertView.findViewById(R.id.adapter_listView_home_head_portrait);
            name = (TextView) convertView.findViewById(R.id.adapter_listView_home_name);
            createTime = (TextView) convertView.findViewById(R.id.adapter_listView_home_create_time);
            content = (TextView) convertView.findViewById(R.id.adapter_listView_home_content);
            image = (MyGridView) convertView.findViewById(R.id.adapter_listView_home_image);
            comment = (LinearLayout) convertView.findViewById(R.id.adapter_listView_home_comment);
            commentNum = (TextView) convertView.findViewById(R.id.adapter_listView_home_commentNum);
            like = (LinearLayout) convertView.findViewById(R.id.adapter_listView_home_like);
            likeIcon = (ImageView) convertView.findViewById(R.id.adapter_listView_home_like_icon);
            likeText = (TextView) convertView.findViewById(R.id.adapter_listView_home_like_text);
            likeNum = (TextView) convertView.findViewById(R.id.adapter_listView_home_likeNum);
        }
    }

}
