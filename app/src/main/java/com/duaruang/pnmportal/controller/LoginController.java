package com.duaruang.pnmportal.controller;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.duaruang.pnmportal.activity.LoginActivity;
import com.duaruang.pnmportal.activity.MainDrawerActivity;
import com.duaruang.pnmportal.callback.LoginCallback;
import com.duaruang.pnmportal.data.LoginSSOResponse;
import com.duaruang.pnmportal.preference.AppPreference;
import com.duaruang.pnmportal.rest.ApiConstant;
import com.duaruang.pnmportal.rest.RestHelper;
import com.duaruang.pnmportal.rest.RestService;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by indri on 1/30/18.
 */

public class LoginController {
    private final String LOG_TAG = LoginController.class.getSimpleName();

    private AppPreference appPreference = AppPreference.getInstance();

    private LoginCallback loginCallback;

    private RestService serviceSSO = RestHelper.getInstanceSSO().getRestService();

    private Call<LoginSSOResponse> callLoginSSO;

    public LoginController(LoginCallback callback) {
        this.loginCallback = callback;
    }

    public void doLoginSSO(String username, String password) {
        callLoginSSO = serviceSSO.loginSSO(ApiConstant.LOGIN_SSO, username, password, "PKM");
        callLoginSSO.enqueue(new Callback<LoginSSOResponse>() {
            @Override
            public void onResponse(Call<LoginSSOResponse> call, retrofit2.Response<LoginSSOResponse> response) {
                Log.d(LOG_TAG, "doLoginSSO.onResponse " + (response != null ? response.body():""));

                if (response != null && response.body() != null) {
                    String errMsg = (response.body().getLogin() != null && response.body().getLogin().size() > 0 && response.body().getLogin().get(0) != null) ? response.body().getLogin().get(0).getMessage() : "";
                    if (response.body().isSuccessResponse() && response.body().getUserLoggedin() != null) {
                        appPreference.setUserLoggedIn(response.body().getUserLoggedin());
                        loginCallback.onLoginSSOSuccess(response.body().getUserLoggedin());
                    } else {
                        Throwable tt = new Throwable("Login SSO Failed!\n" + errMsg);
                        loginCallback.onLoginSSOFailed(tt);
                    }
                } else {
                    Throwable tt = new Throwable("Login SSO Failed!");
                    loginCallback.onLoginSSOFailed(tt);
                    //  Crashlytics.logException(tt);
                }
            }

            @Override
            public void onFailure(Call<LoginSSOResponse> call, Throwable t) {
                Log.d(LOG_TAG, "doLoginSSO.onFailure " + (t != null ? t.getMessage() : ""));
                if (loginCallback != null) {
                    //loginCallback.onLoginSSOFailed(new Throwable("Login SSO Failed!\n" + (t != null ? t.getMessage() : "")));
                    Throwable tt = new Throwable("Login SSO Failed!\n" + (t != null ? t.getMessage() : ""));
                    loginCallback.onLoginSSOFailed(tt);
                    // Crashlytics.logException(tt);
                }
            }
        });
    }

    public void cancel() {
        if (callLoginSSO != null)
            callLoginSSO.cancel();
    }
}
