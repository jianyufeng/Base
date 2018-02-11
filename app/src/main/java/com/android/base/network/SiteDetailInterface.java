package com.android.base.network;

import com.fpi.mobile.network.response.CommonResult;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @version V1.0
 * @Description:断面详情相关网络接口 Created by 16896 on 2017/12/15.
 */

public interface SiteDetailInterface {
    //获取水质达标信息
    @POST("mobile/mobile/service/dwsqRequestSiteDetails.do")
    Call<CommonResult> getSiteDetails(@Query("siteId") String siteId, @Query("userId") String userId, @Query("time") String time
            , @Query("dataType") int dataType);


    @POST("mobile/mobile/service/dwsqRequestSitefactorTrend.do")
    Call<CommonResult> getSitefactorTrend(@Query("siteId") String siteId,
                                          @Query("time") String time,
                                          @Query("factorId") String factorId,
                                          @Query("stateId") String stateId,
                                          @Query("dataType") int dataType);


}
