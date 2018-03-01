package com.android.base.activity.login.bean;

import com.fpi.mobile.bean.ModelUserBase;

/**
 * 人员信息实体
 * Created by 15161 on 2018/2/8.
 */

public class ModelUserInfo extends ModelUserBase {
    private String userId;
    //    //密码
//    private String password;
//    //用户名
//    private String userName;
//    //昵称
//    private String nickName;
//    //部门
//    private String department;
//    //头像地址
//    private String icon;
//    //电话
//    private String mobile;
//    //手机
//    private String phone;
//    //职级
//    private String position;
    // im账号
    private String imUserName;
    //im密码
    private String imPassword;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImUserName() {
        return imUserName;
    }

    public void setImUserName(String imUserName) {
        this.imUserName = imUserName;
    }

    public String getImPassword() {
        return imPassword;
    }

    public void setImPassword(String imPassword) {
        this.imPassword = imPassword;
    }
}
