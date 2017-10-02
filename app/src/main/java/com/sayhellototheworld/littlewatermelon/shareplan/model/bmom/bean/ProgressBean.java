package com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.bean;

import java.util.Date;
import java.util.List;

/**
 * Created by 123 on 2017/9/23.
 */

public class ProgressBean {

    private PlanBean plan;
    private Date createTime;
    private String content;
    private List<String> imageUrls;

    public PlanBean getPlan() {
        return plan;
    }

    public void setPlan(PlanBean plan) {
        this.plan = plan;
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

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
