package com.android.base.network;

import com.fpi.mobile.network.response.CommonResult;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @version V1.0
 * @Description:首页相关网络接口 Created by 16896 on 2017/12/15.
 */

public interface HomeInterface {
    //获取水质达标信息
    @POST("mobile/mobile/service/dwsqRequestWaterStandard.do")
    Call<CommonResult> getWaterStandardInfo(@Query("time") String time, @Query("userId") String userId, @Query("dataType") int dataType);

    //获取地图展示信息
    @POST("mobile/mobile/service/dwsqRequestMapSiteInfo.do")
    Call<CommonResult> getMapSiteInfo(@Query("time") String time, @Query("userId") String userId, @Query("dataType") int dataType);
}
