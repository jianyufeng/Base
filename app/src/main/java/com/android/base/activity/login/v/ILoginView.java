package com.android.base.activity.login.v;

/**
 * Created by 16896 on 2018/3/1.
 */

public interface ILoginView {
    //等待加载
    void showLoading();

    String getUserName();

    String getPassword();

    void dismissLoading();

    void goMainTabActivity();

    void alert(String s);
}
