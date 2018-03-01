package com.android.base.activity.login.p;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.android.base.activity.login.bean.ModelUserInfo;
import com.android.base.activity.login.biz.UserRequest;
import com.android.base.activity.login.v.ILoginView;
import com.android.base.app.MainApplication;
import com.fpi.mobile.network.BaseNetworkInterface;
import com.fpi.mobile.utils.StringTool;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.utils.EaseCommonUtils;

/**
 * Created by 16896 on 2018/3/1.
 */

public class LoginPresent implements BaseNetworkInterface {
    private ILoginView loginView;
    private UserRequest userRequest;
    private Handler mHandler = new Handler();
    private Context context;

    public LoginPresent(ILoginView viewListener) {
        this.loginView = viewListener;
        this.userRequest = new UserRequest(this);
        context = MainApplication.getInstance().getApplicationContext();
    }

    public void login() {
        if (!EaseCommonUtils.isNetWorkConnected(context)) { //网络检查
            loginView.alert("无网络");
            return;
        }
//        userRequest.login(loginView.getUserName(), loginView.getPassword(),"1");
        String name = loginView.getUserName();
        String psw = loginView.getPassword();
        if (StringTool.isEmpty(name) || StringTool.isEmpty(psw)) {
            loginView.alert("账号或密码为空");
            return;
        }
        //login
        EMClient.getInstance().login(name, psw, new EMCallBack() {
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                //设置推送昵称
                boolean updatenick = EMClient.getInstance().pushManager().updatePushNickname(
                        loginView.getUserName());
                if (!updatenick) {
                    Log.e("LoginActivity", "update current user nick fail");
                }
                loginView.goMainTabActivity();
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String error) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        loginView.alert("登录失败");
                    }
                });
            }
        });

    }

    @Override
    public void loadding() {
        loginView.showLoading();
    }

    @Override
    public void loaddingFinish() {
        loginView.dismissLoading();
    }

    @Override
    public void requestSuccess(Object object) {
        // 1 登陆成功  返回数据
        // 2  保存登陆信息--免登录
        // 3 跳转到主页面 ----- *有未读报警信息提示
        if (object instanceof ModelUserInfo) {
            ModelUserInfo modelUser = (ModelUserInfo) object;
            if (modelUser != null) {
                //假如登陆成功  保存历史账号
                modelUser.setPassword(loginView.getPassword());
                MainApplication.getInstance().setCurrUser(modelUser);
                loginView.goMainTabActivity();
            }
        } else {
            loginView.alert("登录失败");
        }
    }

    @Override
    public void requestError(String s) {

    }
}
