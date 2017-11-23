package com.sayhellototheworld.littlewatermelon.shareplan.adapter.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by 123 on 2017/10/11.
 */

public class HomePlanBean implements Parcelable{

    private String userName;
    private String userHeadPortraitUrl;
    private String userObjectID;
    private String planObjectID;
    private String title;
    private String createTime;
    private String content;
    private String beginTime;
    private String endTime;
    private String location;
    private int stars;
    private int statue;
    private List<String> imageUrls;
    private int commentNum;

    public String getPlanObjectID() {
        return planObjectID;
    }

    public void setPlanObjectID(String planObjectID) {
        this.planObjectID = planObjectID;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserHeadPortraitUrl() {
        return userHeadPortraitUrl;
    }

    public void setUserHeadPortraitUrl(String userHeadPortraitUrl) {
        this.userHeadPortraitUrl = userHeadPortraitUrl;
    }

    public String getUserObjectID() {
        return userObjectID;
    }

    public void setUserObjectID(String userObjectID) {
        this.userObjectID = userObjectID;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getStatue() {
        return statue;
    }

    public void setStatue(int statue) {
        this.statue = statue;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(userHeadPortraitUrl);
        dest.writeString(userObjectID);
        dest.writeString(planObjectID);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(createTime);
        dest.writeString(beginTime);
        dest.writeString(endTime);
        dest.writeString(location);
        dest.writeInt(stars);
        dest.writeInt(statue);
        dest.writeInt(commentNum);
        dest.writeStringList(imageUrls);
    }

    public static final Creator<HomePlanBean> CREATOR = new Creator<HomePlanBean>() {
        @Override
        public HomePlanBean createFromParcel(Parcel in) {
            HomePlanBean homePlanBean = new HomePlanBean();
            homePlanBean.setUserName(in.readString());
            homePlanBean.setUserHeadPortraitUrl(in.readString());
            homePlanBean.setUserObjectID(in.readString());
            homePlanBean.setTitle(in.readString());
            homePlanBean.setPlanObjectID(in.readString());
            homePlanBean.setContent(in.readString());
            homePlanBean.setCreateTime(in.readString());
            homePlanBean.setBeginTime(in.readString());
            homePlanBean.setEndTime(in.readString());
            homePlanBean.setLocation(in.readString());
            homePlanBean.setStars(in.readInt());
            homePlanBean.setStatue(in.readInt());
            homePlanBean.setCommentNum(in.readInt());
            homePlanBean.setImageUrls(in.readArrayList(String.class.getClassLoader()));
            return homePlanBean;
        }

        @Override
        public HomePlanBean[] newArray(int size) {
            return new HomePlanBean[size];
        }
    };

}
