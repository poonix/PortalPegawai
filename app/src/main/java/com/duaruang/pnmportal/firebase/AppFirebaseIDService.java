package com.duaruang.pnmportal.firebase;

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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
            Log.d("FCM", "Instance ID: " + FirebaseInstanceId.getInstance().getToken());

            String fcmId = instanceId.getToken();
            sendFirebaseIdToServer(fcmId);
        }
    }

    private void sendFirebaseIdToServer(final String fcmId) {

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
    }


}
