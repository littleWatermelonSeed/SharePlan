package com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by 123 on 2017/9/23.
 */

public class ProgressBean extends BmobObject{

    private PlanBean plan;
    private String content;
    private List<String> imageUrls;

    public PlanBean getPlan() {
        return plan;
    }

    public void setPlan(PlanBean plan) {
        this.plan = plan;
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
