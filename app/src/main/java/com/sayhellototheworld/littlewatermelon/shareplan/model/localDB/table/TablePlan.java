package com.sayhellototheworld.littlewatermelon.shareplan.model.localDB.table;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 123 on 2017/9/22.
 */

public class TablePlan extends DataSupport {

    private String userID;
    private String objectID;
    private String title;
    private String content;
    private Date beginTime;
    private Date endTime;
    private Date createTime;
    private boolean limit;
    private String location;
    private int statue;
    private int likes;
    private List<TableImagePath> imagePath = new ArrayList<>();
    private List<TablePlanProgress> progress = new ArrayList<>();

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getStatue() {
        return statue;
    }

    public void setStatue(int statue) {
        this.statue = statue;
    }

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public boolean isLimit() {
        return limit;
    }

    public void setLimit(boolean limit) {
        this.limit = limit;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<TableImagePath> getImagePath() {
        return DataSupport.where("tableplan_id = ?", String.valueOf(this.getBaseObjId())).find(TableImagePath.class);
    }

    public void setImagePath(List<TableImagePath> imagePath) {
        this.imagePath = imagePath;
    }

    public List<TablePlanProgress> getProgress() {
        return DataSupport.where("tableplan_id = ?", String.valueOf(this.getBaseObjId())).order("createTime asc").find(TablePlanProgress.class);
    }

    public void setProgress(List<TablePlanProgress> progress) {
        this.progress = progress;
    }

    public long getBaseID(){
        return getBaseObjId();
    }

}
