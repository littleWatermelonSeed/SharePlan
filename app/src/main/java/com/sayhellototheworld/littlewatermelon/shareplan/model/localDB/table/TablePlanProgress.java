package com.sayhellototheworld.littlewatermelon.shareplan.model.localDB.table;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 123 on 2017/9/22.
 */

public class TablePlanProgress extends DataSupport {

    private Date createTime;
    private String content;
    private List<TableImagePath> mImagePaths = new ArrayList<>();

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

    public List<TableImagePath> getImagePaths() {
        return mImagePaths;
    }

    public void setImagePaths(List<TableImagePath> imagePaths) {
        mImagePaths = imagePaths;
    }
}
