package com.duaruang.pnmportal.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.duaruang.pnmportal.R;
import com.duaruang.pnmportal.config.Config;
import com.duaruang.pnmportal.data.LoginSSOResponse;
import com.duaruang.pnmportal.data.Pegawai;
import com.duaruang.pnmportal.firebase.AppFirebaseIDService;
import com.duaruang.pnmportal.helper.VolleyErrorHelper;
import com.duaruang.pnmportal.preference.AppPreference;
import com.duaruang.pnmportal.rest.ApiConstant;
import com.duaruang.pnmportal.rest.RestHelper;
import com.duaruang.pnmportal.rest.RestService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity {

    private final String LOG_TAG = LoginActivity.class.getSimpleName();
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private AppPreference appPreference = AppPreference.getInstance();
    private Call<LoginSSOResponse> callLoginSSO;
    private RestService serviceSSO = RestHelper.getInstanceSSO().getRestService();
    @BindView(R.id.input_username) EditText _usernameText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_login) Button _loginButton;
    @BindView(R.id.forgot_password)
    TextView _forgotPasswordText;

    //boolean variable to check user is logged in or not
    //initially it is false
    private boolean loggedIn = false;

    @BindView(R.id.login_progress)
    ProgressBar pb;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        pb.setVisibility(View.INVISIBLE);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hideSoftKeyboard(LoginActivity.this, v);
                doLoginSSO();
//                loginmockup();
            }
        });

        _forgotPasswordText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the forgot password activity
                Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(intent);
//                Toast.makeText(getApplicationContext(), "Forgot password?", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void hideSoftKeyboard (Activity activity, View view)
    {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //If we will get true
        if(loggedIn){
            //We will start the Profile Activity
            Intent intent = new Intent(LoginActivity.this, MainDrawerActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    public void doLoginSSO() {
        if (!validate()) {
            onLoginFailed();
            return;
        }
        pb.setVisibility(View.VISIBLE);

        _loginButton.setEnabled(false);

        final String username = _usernameText.getText().toString();
        final String password = _passwordText.getText().toString();
        callLoginSSO = serviceSSO.loginSSO(ApiConstant.LOGIN_SSO, username, password, "PKM");
        callLoginSSO.enqueue(new Callback<LoginSSOResponse>() {
            @Override
            public void onResponse(Call<LoginSSOResponse> call, retrofit2.Response<LoginSSOResponse> response) {
                Log.d(LOG_TAG, "doLoginSSO.onResponse " + (response != null ? response.body():""));

                    if (response != null && response.body() != null) {
                        String errMsg = (response.body().getLogin() != null && response.body().getLogin().size() > 0 && response.body().getLogin().get(0) != null) ? response.body().getLogin().get(0).getMessage() : "";
                        if (response.body().isSuccessResponse() && response.body().getUserLoggedin() != null) {
                            appPreference.setUserLoggedIn(response.body().getUserLoggedin());
                            loggedIn = true;
                            //Starting profile activity
                            onLoginSuccess();
                            AppFirebaseIDService.resendFcmId();
                            Intent intent = new Intent(LoginActivity.this, MainDrawerActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Throwable tt = new Throwable("Login SSO Failed!\n" + errMsg);
                            onLoginFailed();
                            Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Throwable tt = new Throwable("Login SSO Failed!");
                        onLoginFailed();
                        Toast.makeText(LoginActivity.this, "Login SSO Failed!", Toast.LENGTH_LONG).show();
                        //  Crashlytics.logException(tt);
                    }
            }

            @Override
            public void onFailure(Call<LoginSSOResponse> call, Throwable t) {
                Log.d(LOG_TAG, "doLoginSSO.onFailure " + (t != null ? t.getMessage() : ""));
                onLoginFailed();
                Toast.makeText(LoginActivity.this, "Network Timeout, coba lagi!", Toast.LENGTH_LONG).show();
            }
        });
    }

    /*
    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }
        pb.setVisibility(View.VISIBLE);

        _loginButton.setEnabled(false);


        final String username = _usernameText.getText().toString();
        final String password = _passwordText.getText().toString();
        final String email = null;
        final String nik = null;
        final String nama = null;
        final String posisi = null;

        // TODO: Implement your own authentication logic here.

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = Config.KEY_URL_LOGIN+"?user="+username+"&pass="+password+"&app_code=PKM";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response: ", response);
                        String message = null;
                        String data = null;
                        JSONObject dataObj = null;
                        JSONArray dataArray;

                        try {
                            //Do it with this it will work
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject != null) {

                                JSONArray loginArray = jsonObject.getJSONArray("login");
                                for (int i = 0; i < loginArray.length(); i++) {
                                    JSONObject loginObj = loginArray.getJSONObject(i);

                                    data = loginObj.getString("data");

                                    message = loginObj.getString("message");
                                    Log.d("Message: ", message);
                                    Log.d("Pegawai: ", data);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            onLoginFailed();
                        }

                        if (message !=null) {
                            //If we are getting success from server
                            if (message.equalsIgnoreCase(Config.LOGIN_SUCCESS)) {


                                try {
                                    dataArray = new JSONArray(data);
                                    for (int i = 0; i < dataArray.length(); i++) {
                                        dataObj = dataArray.getJSONObject(i);
                                    }

                                    if (dataObj != null) {

                                        Log.d("Pegawai nama: ", dataObj.getString(Pegawai.TAG_NAMA));
                                        //Adding values to editor
                                        //appPreference.setUserLoggedIn(response);

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                                onLoginSuccess();

                                //Starting profile activity
                                Intent intent = new Intent(LoginActivity.this, MainDrawerActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                //If the server response is not success
                                //Displaying an error message on toast
                                Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_LONG).show();
                                onLoginFailed();
                            }
                        }


                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyErrorHelper.getMessage(error, getApplicationContext());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(LoginActivity.this, "Network Timeout, coba lagi!", Toast.LENGTH_LONG).show();
                }
                onLoginFailed();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                //Adding parameters to request

                //returning parameter
                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String,String> headers = new HashMap<String, String>();
                // add headers <key,value>
                String credentials = "event:event";
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(),
                        Base64.DEFAULT);
                headers.put("Authorization", auth);
                return headers;


            }
        };

        queue.add(stringRequest);
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        pb.setVisibility(View.VISIBLE);
//        finish();
    }

    public void onLoginFailed() {
//        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        pb.setVisibility(View.INVISIBLE);
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String username = _usernameText.getText().toString();
        String password = _passwordText.getText().toString();

//        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            _usernameText.setError("enter a valid email address");
//            valid = false;
//        } else {
//            _usernameText.setError(null);
//        }

        if (username.isEmpty() || username.length() < 4 || username.length() > 100) {
            _usernameText.setError("between 4 and 100 alphanumeric characters");
            valid = false;
        } else {
            _usernameText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 100) {
            _passwordText.setError("between 4 and 100 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}

