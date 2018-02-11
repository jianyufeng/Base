package com.android.base.network;

import com.fpi.mobile.network.response.CommonResult;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by 12697 on 2017/12/27.
 */

public interface SectionListInterface {

    @POST("mobile/mobile/service/dwsqRequestSectionList.do")
    Call<CommonResult> getSectionList(@Query("type") String type
            , @Query("time") String time
            , @Query("dataType") int dataType);
}
