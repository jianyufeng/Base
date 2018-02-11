package com.android.base.model.user;


import com.fpi.mobile.bean.ModelUserBase;

/**
 * 用户信息
 * Created by 14165 on 2017/5/17.
 */

public class ModelUserInfo extends ModelUserBase {
    private String userId;

    // 账号
    private String account;


    // 0是无， 1是有（报警消息读否）
    private String isWaring;

    // 头像地址
    private String userImage;

    public String getIsSendar() {
        return isSendar;
    }

    public void setIsSendar(String isSendar) {
        this.isSendar = isSendar;
    }

    // 系统最小时间
    private String isSendar;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getIsWaring() {
        return isWaring;
    }

    public void setIsWaring(String isWaring) {
        this.isWaring = isWaring;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}
