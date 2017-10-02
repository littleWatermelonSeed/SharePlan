package com.sayhellototheworld.littlewatermelon.shareplan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sayhellototheworld.littlewatermelon.shareplan.R;
import com.sayhellototheworld.littlewatermelon.shareplan.model.localDB.table.TablePlan;
import com.sayhellototheworld.littlewatermelon.shareplan.util.TimeFormatUtil;
import com.sayhellototheworld.littlewatermelon.shareplan.view.function_view.PlanDetailsActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by 123 on 2017/9/25.
 */

public class PlanListAdapter extends BaseAdapter{

    private Context mContext;
    private LayoutInflater mInflater;
    private List<TablePlan> mTablePlan;

    private ContentClick listener;

    public PlanListAdapter(Context context, List<TablePlan> tablePlan) {
        mContext = context;
        mTablePlan = tablePlan;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mTablePlan.size();
    }

    @Override
    public Object getItem(int position) {
        return mTablePlan.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_listview_plan, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
            AutoUtils.autoSize(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Date beginDate = mTablePlan.get(position).getBeginTime();
        Date endDate = mTablePlan.get(position).getEndTime();
        viewHolder.textView_createTime.setText(TimeFormatUtil.DateToRealTime(mTablePlan.get(position).getCreateTime()));
        viewHolder.textView_beginTime.setText(TimeFormatUtil.DateToStringTime(beginDate));
        viewHolder.textView_endTime.setText(TimeFormatUtil.DateToStringTime(endDate));
        viewHolder.textView_title.setText(mTablePlan.get(position).getTitle());
        viewHolder.textView_content.setText(mTablePlan.get(position).getContent());
        if (mTablePlan.get(position).getStatue() != 1 && !TimeFormatUtil.compareDate(endDate, new Date())) {
            viewHolder.textView_statue.setText("未完成");
            viewHolder.textView_statue.setTextColor(mContext.getResources().getColor(R.color.plan_text_statue_unfinished));
        } else if (mTablePlan.get(position).getStatue() == 0) {
            viewHolder.textView_statue.setText("进行中");
            viewHolder.textView_statue.setTextColor(mContext.getResources().getColor(R.color.plan_text_statue_ing));
        } else if (mTablePlan.get(position).getStatue() == 1) {
            viewHolder.textView_statue.setText("已完成");
            viewHolder.textView_statue.setTextColor(mContext.getResources().getColor(R.color.plan_text_statue_finished));
        }
        listener = new ContentClick(position);
        viewHolder.textView_content.setOnClickListener(listener);
        return convertView;
    }

    class ContentClick implements View.OnClickListener{

        private int position;

        public ContentClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            PlanDetailsActivity.startPlanDetailsActivity(mContext,mTablePlan.get(position).getObjectID());
        }
    }

    class ViewHolder {
        private View convertView;
        public TextView textView_beginTime;
        public TextView textView_endTime;
        public TextView textView_statue;
        public TextView textView_title;
        public TextView textView_content;
        public TextView textView_createTime;

        public ViewHolder(View convertView) {
            this.convertView = convertView;
            init();
        }

        private void init() {
            textView_beginTime = (TextView) convertView.findViewById(R.id.adapter_listView_plan_beginTime);
            textView_endTime = (TextView) convertView.findViewById(R.id.adapter_listView_plan_endTime);
            textView_statue = (TextView) convertView.findViewById(R.id.adapter_listView_plan_statue);
            textView_title = (TextView) convertView.findViewById(R.id.adapter_listView_plan_title);
            textView_content = (TextView) convertView.findViewById(R.id.adapter_listView_plan_content);
            textView_createTime = (TextView) convertView.findViewById(R.id.adapter_listView_plan_createTime);
        }

    }

}
