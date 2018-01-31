package com.duaruang.pnmportal.rest;

import android.text.TextUtils;

import com.duaruang.pnmportal.BuildConfig;
import com.duaruang.pnmportal.preference.AppPreference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by indri on 1/30/18.
 */

public class RestHelper {
    private static final long CONNECTION_TIMEOUT = 30 * 1000;
    private static final long READ_TIMEOUT = 30 * 1000;

    private static final Object LOCK = new Object();
    private static RestHelper sInstanceNoAuth;
    private static RestHelper sInstance;
    private static RestHelper sInstanceSSO;

    private static AppPreference preference = AppPreference.getInstance();
    private RestService service;

    public static RestHelper getInstanceSSO() {
        synchronized (LOCK) {
            if (sInstanceSSO == null) {
                sInstanceSSO = new RestHelper(preference.getApiHostSso(), ApiConstant.USER_AUTH_SSO, ApiConstant.PASSWORD_AUTH_SSO);
            }
        }
        return sInstanceSSO;
    }

    public static RestHelper getInstance() {
        synchronized (LOCK) {
            if (sInstance == null) {
                sInstance = new RestHelper(preference.getApiHostSso(), ApiConstant.USER_AUTH, ApiConstant.PASSWORD_AUTH);
            }
        }
        return sInstance;
    }


    public static void setHostSSO(String host) {
        synchronized (LOCK) {
            String apiHost = host + "/WebService/SSO_Mobile/";
            sInstanceSSO = new RestHelper(apiHost, ApiConstant.USER_AUTH_SSO, ApiConstant.PASSWORD_AUTH_SSO);
            preference.setApiHostSso(apiHost);
        }
    }


    private RestHelper(String baseUrl, String usernameAuth, String passwordAuth) {
        Retrofit retrofit = buildRetrofit(baseUrl, usernameAuth, passwordAuth);
        service = retrofit.create(RestService.class);
    }

    protected Retrofit buildRetrofit(String baseUrl, String usernameAuth, String passwordAuth) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(buildHttpClient(usernameAuth, passwordAuth))
                .addConverterFactory(GsonConverterFactory.create(buildGson()))
                .build();
    }

    private Gson buildGson() {
        return new GsonBuilder()
                .setLenient()
                .serializeNulls()
                .create();
    }

    private OkHttpClient buildHttpClient(String usernameAuth, String passwordAuth) {
        HttpLoggingInterceptor loggingInterceptornterceptor = new HttpLoggingInterceptor();
        loggingInterceptornterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        //loggingInterceptornterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        HeaderInterceptor headerInterceptor = new HeaderInterceptor();
        if (!TextUtils.isEmpty(usernameAuth) && !TextUtils.isEmpty(passwordAuth)) {
            headerInterceptor.setBasicAuth(usernameAuth, passwordAuth);
        }
        headerInterceptor.addHeader("Content-Type", "application/json");
        headerInterceptor.addHeader("Accept", "application/json");

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptornterceptor)
                .addInterceptor(headerInterceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS);


        return httpClient.build();
    }

    public RestService getRestService() {
        return this.service;
    }


    public static class HeaderInterceptor implements Interceptor {

        private String username, password;
        private HashMap<String, String> headerMap = new HashMap<>();

        public HeaderInterceptor() {

        }

        public void setBasicAuth(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public void addHeader(String key, String value) {
            headerMap.put(key, value);
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();

            Request.Builder requestBuilder = original.newBuilder();
            if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                requestBuilder.addHeader("Authorization", Credentials.basic(username, password));
            }

            for (String key : headerMap.keySet()) {
                requestBuilder.addHeader(key, headerMap.get(key));
            }

            Request request = requestBuilder.build();

            //Response response = chain.proceed(request);
            //Log.d("Retrofit@Response --> ", response.body().string());
            //return response;

            return chain.proceed(request);
        }
    }
}
