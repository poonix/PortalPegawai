package com.duaruang.pnmportal.firebase;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.duaruang.pnmportal.config.Config;
import com.duaruang.pnmportal.data.BaseResponse;
import com.duaruang.pnmportal.data.FirebaseIDRequest;
import com.duaruang.pnmportal.preference.AppPreference;
import com.duaruang.pnmportal.rest.RestHelper;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by user on 11/21/2017.
 */

public class AppFirebaseIDService extends FirebaseInstanceIdService {
    private static final String TAG = AppFirebaseIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        Log.d(TAG, "onTokenRefresh");
        FirebaseInstanceId instanceId = FirebaseInstanceId.getInstance();
        if (instanceId != null) {
            String fcmId = instanceId.getToken();
            AppPreference.getInstance().setFcmId(fcmId);
            sendFirebaseIdToServer(fcmId);
        }
    }

    /*
    private void sendFirebaseIdToServer1(final String fcmId) {

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = Config.KEY_URL_POST_FCM;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response: ", response);

                        String message = null, status = null;

                        try {
                            //Do it with this it will work
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject != null) {

                                status = jsonObject.getString("status");
                                message = jsonObject.getString("message");
                                Log.d("Status: ", status);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                        if (message !=null) {
                            //If we are getting success from server
                            if (status.equalsIgnoreCase(Config.MESSAGE_SUCCESS_FCM)) {
                                Log.d("Berhasil ", status);
                            }

                        } else {
                            //If the server response is not success
                            //Displaying an error message on toast
                            //Toast.makeText(ForgotPasswordActivity.this, "No Data", Toast.LENGTH_LONG).show();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //VolleyErrorHelper.getMessage(error, ForgotPasswordActivity.this);
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                //Adding parameters to request
                params.put("idusergroup", "2");
                params.put("fcm", fcmId);
                //returning parameter
                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String,String> headers = new HashMap<String, String>();
                // add headers <key,value>
                String credentials = "username:password";
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(),
                        Base64.DEFAULT);
                headers.put("Authorization", auth);
                return headers;

            }
        };

        queue.add(stringRequest);
    }*/

    private static void sendFirebaseIdToServer(final String fcmId) {
        final AppPreference preference = AppPreference.getInstance();
        if (preference.getUserSSOLoggedIn() == null) {
            Log.e(TAG, "sendFirebaseIdToServer failed. User not login yet ");
            AppPreference.getInstance().setFcmNeedToResend(true);
            return;
        }

        String idSdm = preference.getUserSSOLoggedIn().getIdsdm();
        FirebaseIDRequest request = new FirebaseIDRequest(idSdm, fcmId);
        Call<BaseResponse> call = RestHelper.getInstanceSSO().getRestService().sendFcmId(request);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, retrofit2.Response<BaseResponse> response) {
                Log.d(TAG, "sendFirebaseIdToServer.onResponse : " + response.body());
                if (response != null && response.body() != null && response.body().isSuccessResponse()) {
                    preference.setFcmNeedToResend(false);
                } else {
                    preference.setFcmNeedToResend(true);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Log.d(TAG, "sendFirebaseIdToServer.onFailure : " + t.getMessage());
                preference.setFcmNeedToResend(true);
            }
        });
    }

    private static String getDeviceFcmId() {
        final AppPreference preference = AppPreference.getInstance();
        FirebaseInstanceId instance = FirebaseInstanceId.getInstance();
        String olFcmId = preference.getInstance().getFcmId();
        if (instance != null) {
            String newFcmId = instance.getToken();
            if (TextUtils.isEmpty(olFcmId) || (!TextUtils.isEmpty(newFcmId) && !newFcmId.equals(olFcmId))) {
                preference.setFcmId(newFcmId);
                preference.setFcmNeedToResend(true);
                return newFcmId;
            }

            return olFcmId;
        }

        return olFcmId;
    }

    public static void resendFcmId() {
        final AppPreference preference = AppPreference.getInstance();
        String fcmId = getDeviceFcmId();
        if (!TextUtils.isEmpty(fcmId) && preference.getFcmNeedToResend()) {
            sendFirebaseIdToServer(fcmId);
        }
    }


}
