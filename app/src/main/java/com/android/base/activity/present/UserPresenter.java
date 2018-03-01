package com.android.base.activity.present;


import com.android.base.R;
import com.android.base.activity.login.bean.ModelUserInfo;
import com.android.base.app.MainApplication;
import com.android.base.network.UserInterface;
import com.android.base.util.Encrypt;
import com.fpi.mobile.network.BaseNetworkInterface;
import com.fpi.mobile.network.BasePresenter;
import com.fpi.mobile.network.RetrofitManager;
import com.fpi.mobile.network.response.CommonResult;
import com.fpi.mobile.network.response.ResponseHandler;
import com.fpi.mobile.utils.StringTool;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserPresenter implements BasePresenter<BaseNetworkInterface> {
    private BaseNetworkInterface baseInterface;
    private UserInterface userInterface;

    public UserPresenter(BaseNetworkInterface baseInterface) {
        attachView(baseInterface);
        userInterface = RetrofitManager.getRetrofit().create(UserInterface.class);
    }

    public void login(String userName, String password, String imei) {
        if (StringTool.isEmpty(imei)){
            imei = "1";
        }
        baseInterface.loadding();
//        Call<CommonResult> call = userInterface.userLogin(userName, Encrypt.md5(password), imei,"1");//"1":标识需要通知
        Call<CommonResult> call = userInterface.userLogin(userName, Encrypt.md5(password), imei,"android");//"1":标识需要通知
        call.enqueue(new Callback<CommonResult>() {
            @Override
            public void onResponse(Call<CommonResult> call, Response<CommonResult> response) {
                baseInterface.loaddingFinish();
                if (ResponseHandler.getInstance().handleResponse(response, true)) {
                    baseInterface.requestSuccess(response.body().getObject(ModelUserInfo.class));
                }else {
                    baseInterface.requestSuccess(MainApplication.getInstance().getString(R.string.login_failure)); //登陆失败
                }
            }

            @Override
            public void onFailure(Call<CommonResult> call, Throwable t) {
                baseInterface.loaddingFinish();
                baseInterface.requestError("network error");
            }
        });
    }
    public void requestLookSiteDetail(String userId, String imei) {
        if (StringTool.isEmpty(imei)){
            imei = "1";
        }
        baseInterface.loadding();
        Call<CommonResult> call = userInterface.getLookSiteDetail(userId,imei);
        call.enqueue(new Callback<CommonResult>() {
            @Override
            public void onResponse(Call<CommonResult> call, Response<CommonResult> response) {
                baseInterface.loaddingFinish();
                if (ResponseHandler.getInstance().handleResponse(response, true)) {
                    baseInterface.requestSuccess(response.body().getObject(Boolean.class));
                }
            }

            @Override
            public void onFailure(Call<CommonResult> call, Throwable t) {
                baseInterface.loaddingFinish();
                baseInterface.requestError("network error");
            }
        });
    }

    @Override
    public void attachView(BaseNetworkInterface mvpView) {
        this.baseInterface = mvpView;
    }

    @Override
    public void detachView() {
        this.baseInterface = null;
    }

}
