package com.duaruang.pnmportal.rest;

import com.duaruang.pnmportal.data.BaseResponse;
import com.duaruang.pnmportal.data.FirebaseIDRequest;
import com.duaruang.pnmportal.data.LoginSSOResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by indri on 1/30/18.
 */

public interface RestService {


    @GET
        //(ApiConstant.LOGIN_SSO)
    Call<LoginSSOResponse> loginSSO(
            @Url String url,
            @Query("user") String user,
            @Query("pass") String password,
            @Query("app_code") String appCode
    );

    @POST
            //(ApiConstant.FIREBASE_ID)
    Call<BaseResponse> sendFcmId(
            @Url String url,
            @Body FirebaseIDRequest body
    );
}
