package com.android.base.network;


import com.fpi.mobile.network.response.CommonResult;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by 12697 on 2017/12/22.
 */

public interface AlarmInterface {
    /**
     * 报警列表
     *
     * @param userId
     * @param warningLevel
     * @param area
     * @param time
     * @return
     */
    @POST("mobile/mobile/service/dwsqRequestWarninglist.do")
    Call<CommonResult> getAlarmListInfo(@Query("userId") String userId,
                                        @Query("warningLevel") String warningLevel,
                                        @Query("area") String area,
                                        @Query("time") String time,
                                        @Query("dataType") int dataType);

    /**
     * 报警详情
     *
     * @param siteId
     * @param time
     * @param userId
     * @return
     */
    @POST("mobile/mobile/service/dwsqRequestWarningDetail.do")
    Call<CommonResult> getAlarmDetail(@Query("siteId") String siteId,
                                      @Query("time") String time,
                                      @Query("userId") String userId,
                                      @Query("dataType") int dataType);
}
