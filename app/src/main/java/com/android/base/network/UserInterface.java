package com.android.base.network;


import com.fpi.mobile.network.response.CommonResult;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author qiang.chen
 * @version V1.0
 * @Description:用户相关网络接口
 * @date 2017/1/12 11:15
 */
public interface UserInterface {
    //登录
    @POST("mobile/mobile/service/dwsqRequestLogin.do")
    Call<CommonResult> userLogin(@Query("username") String userName, @Query("password") String password
            , @Query("imei") String imei, @Query("equipType") String equipType);// 1推送 0不推送
//            , @Query("imei") String imei, @Query("isSendar") String isSendar);// 1推送 0不推送

    //未读消息已查看请求
    @POST("mobile/mobile/service/dwsqRequestLookSiteDetail.do")
    Call<CommonResult> getLookSiteDetail(@Query("userId") String userId,
                                         @Query("imei") String imei);

}
