package com.duaruang.pnmportal.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.duaruang.pnmportal.R;
import com.duaruang.pnmportal.config.Config;
import com.duaruang.pnmportal.data.Pegawai;
import com.duaruang.pnmportal.helper.VolleyErrorHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForgotPasswordActivity extends BaseActivity {

    @BindView(R.id.input_username)
    EditText inputUsername;
    @BindView(R.id.input_email)
    EditText inputEmail;
    @BindView(R.id.btn_submit_password)
    AppCompatButton btn_submit;

    @BindView(R.id.progressBarHolder)
    FrameLayout pbHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_forgot_password;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
        ActionBar ab = getSupportActionBar();
        if (ab != null)
            getSupportActionBar().setTitle("Forgot Password");

        btn_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hideSoftKeyboard(ForgotPasswordActivity.this, v);
                forgotPassword(inputUsername.getText().toString(), inputEmail.getText().toString());
//                loginmockup();
            }
        });
    }

    public static void hideSoftKeyboard (Activity activity, View view)
    {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    public void onSubmitSuccess() {
        btn_submit.setEnabled(false);
        btn_submit.setText("SUCCESS");
//        pb.setVisibility(View.VISIBLE);
//        finish();
    }

    public void onSubmitFailed() {
//        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
//        pb.setVisibility(View.INVISIBLE);
        btn_submit.setEnabled(true);
    }

//    public boolean validate() {
//        boolean valid = true;
//        boolean check = false;
//
//        String passwordLama = inputPasswordLama.getText().toString();
//        String passwordBaru0 = inputPasswordBaru0.getText().toString();
//        String passwordBaru1 = inputPasswordBaru1.getText().toString();
//
//        if (passwordLama.isEmpty() || passwordLama.length() < 4 || passwordLama.length() > 20) {
//            inputPasswordLama.setError("between 4 and 10 alphanumeric characters");
//            valid = false;
//        } else {
//            inputPasswordLama.setError(null);
//        }

//        if ((passwordBaru0.isEmpty() || passwordBaru0.length() < 4 || passwordBaru0.length() > 20) ||
//                passwordBaru1.isEmpty() || passwordBaru1.length() < 4 || passwordBaru1.length() > 20){
//            inputPasswordBaru0.setError("between 4 and 10 alphanumeric characters");
//            inputPasswordBaru1.setError("between 4 and 10 alphanumeric characters");
//            valid = false;
//        } else if (passwordBaru0 != passwordBaru1) {
//            inputPasswordBaru0.setError("Password baru tidak sama");
//            inputPasswordBaru1.setError("Password baru tidak sama");
//            valid = false;
//        } else {
//            inputPasswordBaru0.setError(null);
//            inputPasswordBaru1.setError(null);
//        }

//        if (!check) {
//            if (passwordBaru0.isEmpty() || passwordBaru0.length() < 4 || passwordBaru0.length() > 20) {
//                inputPasswordBaru0.setError("between 4 and 10 alphanumeric characters");
//                valid = false;
//                check = false;
//            } else {
//                check = true;
//                inputPasswordBaru0.setError(null);
//            }
//        }
//
//        if (check) {
//            if (passwordBaru1.isEmpty() || passwordBaru1.length() < 4 || passwordBaru1.length() > 20) {
//                inputPasswordBaru1.setError("between 4 and 10 alphanumeric characters");
//                valid = false;
//                check = false;
//            } else {
//                check = true;
//                inputPasswordBaru1.setError(null);
//            }
//        }
//
//        if (check) {
//            if (!passwordBaru0.equals(passwordBaru1)) {
//                inputPasswordBaru0.setError("Password baru tidak sama");
//                inputPasswordBaru1.setError("Password baru tidak sama");
//                valid = false;
//            } else {
//                inputPasswordBaru0.setError(null);
//                inputPasswordBaru1.setError(null);
//            }
//        }
//        return valid;
//    }

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

    public boolean emailValidator(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void forgotPassword(final String _username, final String _email) {
        if (!emailValidator(_email)) {
            onSubmitFailed();
            Toast.makeText(getApplicationContext(), "Cek format email Anda.", Toast.LENGTH_LONG).show();
            return;
        }

        inAnimation = new AlphaAnimation(0f, 1f);
        inAnimation.setDuration(200);
        pbHolder.setAnimation(inAnimation);
        pbHolder.setVisibility(View.VISIBLE);

        btn_submit.setEnabled(false);

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = Config.KEY_URL_FORGOT_PASSWORD;

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

                            outAnimation = new AlphaAnimation(1f, 0f);
                            outAnimation.setDuration(200);
                            pbHolder.setAnimation(outAnimation);
                            pbHolder.setVisibility(View.GONE);

                            if (message.equalsIgnoreCase(Config.MESSAGE_SUCCESS_RESET_PASSWORD)) {
                                onSubmitSuccess();
                                Toast.makeText(getApplicationContext(), "Sukses! Detail selanjutnya silahkan cek email", Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                onSubmitFailed();
                                Toast.makeText(getApplicationContext(), "Gagal, periksa username dan email kembali", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            //If the server response is not success
                            //Displaying an error message on toast
                            Toast.makeText(ForgotPasswordActivity.this, "No Data", Toast.LENGTH_LONG).show();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyErrorHelper.getMessage(error, ForgotPasswordActivity.this);
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                //Adding parameters to request
                params.put("USERNAME", _username);
                params.put("EMAIL", _email);
                //returning parameter
                return params;
            }


//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//
//                Map<String,String> headers = new HashMap<String, String>();
//                // add headers <key,value>
//                String credentials = "username:password";
//                String auth = "Basic "
//                        + Base64.encodeToString(credentials.getBytes(),
//                        Base64.DEFAULT);
//                headers.put("Authorization", auth);
//                return headers;
//
//            }
        };

        queue.add(stringRequest);
    }
}
