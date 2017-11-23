package com.sayhellototheworld.littlewatermelon.shareplan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sayhellototheworld.littlewatermelon.shareplan.R;
import com.sayhellototheworld.littlewatermelon.shareplan.customwidget.MyGridView;
import com.sayhellototheworld.littlewatermelon.shareplan.model.localDB.table.TablePlanProgress;
import com.sayhellototheworld.littlewatermelon.shareplan.util.TimeFormatUtil;

import java.util.List;

/**
 * Created by 123 on 2017/10/6.
 */

public class PlanProgressAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<TablePlanProgress> progress;
    private List<List<String>> imageUrls;

    public PlanProgressAdapter(Context context, List<TablePlanProgress> progress,List<List<String>> imageUrls) {
        mContext = context;
        this.progress = progress;
        this.imageUrls = imageUrls;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return progress.size();
    }

    @Override
    public Object getItem(int position) {
        return progress.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.adapter_listview_progress,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.textView_createTime.setText(TimeFormatUtil.DateToRealTime(progress.get(position).getCreateTime()));
        viewHolder.textView_content.setText(progress.get(position).getContent());

        viewHolder.gridView.setAdapter(null);
        PlanImageAdapter adapter = new PlanImageAdapter(mContext,imageUrls.get(position),PlanImageAdapter.TYPE_READ_PLAN);
        viewHolder.gridView.setAdapter(adapter);
        return convertView;
    }

    class ViewHolder{

        public TextView textView_createTime;
        public TextView textView_content;
        public MyGridView gridView;

        public ViewHolder(View convertView) {
            textView_createTime = (TextView)convertView.findViewById(R.id.adapter_listView_progress_createTime);
            textView_content = (TextView)convertView.findViewById(R.id.adapter_listView_progress_content);
            gridView = (MyGridView)convertView.findViewById(R.id.adapter_listView_progress_images);
        }
    }

}
