package com.sayhellototheworld.littlewatermelon.shareplan.model.localDB.table;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 123 on 2017/9/22.
 */

public class TablePlanProgress extends DataSupport {

    private String objectID;
    private Date createTime;
    private String content;
    private TablePlan mTablePlan;
    private List<TableProgressImagePath> mImagePaths = new ArrayList<>();

    public TablePlan getTablePlan() {
        return mTablePlan;
    }

    public void setTablePlan(TablePlan tablePlan) {
        mTablePlan = tablePlan;
    }

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<TableProgressImagePath> getImagePaths() {
        return DataSupport.where("tableplanprogress_id = ?", String.valueOf(this.getBaseObjId())).find(TableProgressImagePath.class);
    }

    public void setImagePaths(List<TableProgressImagePath> imagePaths) {
        mImagePaths = imagePaths;
    }
}
