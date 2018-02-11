package com.android.base.network;

import com.fpi.mobile.network.response.CommonResult;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by 16896 on 2017/12/19.
 */

public interface StatisticsInterface {
    //获取字典
    @POST("mobile/mobile/service/dwsqRequestDictionary.do")
    Call<CommonResult> getDictionary(
            @Query("type") String type
            , @Query("userId") String userId);

    //获取根据权限 和时间统计出 各个断面的情况
    @POST("mobile/mobile/service/dwsqRequestStatisticalInfo.do")
    Call<CommonResult> getStatisticalInfo(
            @Query("time") String time
            , @Query("userId") String userId
            , @Query("warningLevel") String warningLevel
            , @Query("standard") String standard
            , @Query("area") String area
            , @Query("dataType") int dataType
    );
}
