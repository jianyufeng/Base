package com.android.base.network;

import com.fpi.mobile.network.response.CommonResult;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by 16896 on 2017/12/26.
 */

public interface MineInterface {
    //登出
    @POST("mobile/mobile/service/dwsqRequestloginout.do")
    Call<CommonResult> logout(@Query("type") String type, @Query("userId") String userId, @Query("imei") String imei);
}
