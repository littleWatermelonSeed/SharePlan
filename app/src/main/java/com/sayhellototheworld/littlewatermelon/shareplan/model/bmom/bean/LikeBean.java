package com.sayhellototheworld.littlewatermelon.shareplan.model.bmom.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by 123 on 2017/10/11.
 */

public class LikeBean extends BmobObject {

    private MyUserBean user;
    private PlanBean plan;

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
}
