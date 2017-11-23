package com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by 123 on 2017/10/2.
 */

public class CommentBean extends BmobObject{

    private MyUserBean user;
    private PlanBean plan;
    private String content;
    private String replyUserID;
    private Boolean read;
    private Boolean replyUserRead;

    public Boolean getReplyUserRead() {
        return replyUserRead;
    }

    public void setReplyUserRead(Boolean replyUserRead) {
        this.replyUserRead = replyUserRead;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public MyUserBean getUser() {
        return user;
    }

    public void setUser(MyUserBean user) {
        this.user = user;
    }

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

    public String getReplyUserID() {
        return replyUserID;
    }

    public void setReplyUserID(String replyUserID) {
        this.replyUserID = replyUserID;
    }
}
