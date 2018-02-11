package com.android.base.network;

import com.fpi.mobile.network.response.CommonResult;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @version V1.0
 * @Description:获取日数据已有数据日期 Created by 16896 on 2017/12/15.
 */

public interface HasDaysInterface {
    //获取日数据已有数据日期
    @POST("mobile/mobile/service/dwsqRequestHasDataTime.do")
    Call<CommonResult> requestHasDataTime(@Query("userId") String userId, @Query("year") String year, @Query("month") String month, @Query("dataType") int dataType);
}
