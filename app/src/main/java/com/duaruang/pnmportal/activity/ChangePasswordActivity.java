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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChangePasswordActivity extends BaseActivity {

    @BindView(R.id.input_password_lama)
    EditText inputPasswordLama;
    @BindView(R.id.input_password_baru_0)
    EditText inputPasswordBaru0;
    @BindView(R.id.input_password_baru_1)
    EditText inputPasswordBaru1;
    @BindView(R.id.btn_submit_password)
    AppCompatButton btn_submit;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_change_password;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
        ActionBar ab = getSupportActionBar();
        if (ab != null)
            getSupportActionBar().setTitle("Ganti Password");

        btn_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hideSoftKeyboard(ChangePasswordActivity.this, v);
                updatePassword(inputPasswordLama.getText().toString(), inputPasswordBaru0.getText().toString());
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

    public boolean validate() {
        boolean valid = true;
        boolean check = false;

        String passwordLama = inputPasswordLama.getText().toString();
        String passwordBaru0 = inputPasswordBaru0.getText().toString();
        String passwordBaru1 = inputPasswordBaru1.getText().toString();

        if (passwordLama.isEmpty() || passwordLama.length() < 4 || passwordLama.length() > 20) {
            inputPasswordLama.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            inputPasswordLama.setError(null);
        }

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

        if (!check) {
            if (passwordBaru0.isEmpty() || passwordBaru0.length() < 4 || passwordBaru0.length() > 20) {
                inputPasswordBaru0.setError("between 4 and 10 alphanumeric characters");
                valid = false;
                check = false;
            } else {
                check = true;
                inputPasswordBaru0.setError(null);
            }
        }

        if (check) {
            if (passwordBaru1.isEmpty() || passwordBaru1.length() < 4 || passwordBaru1.length() > 20) {
                inputPasswordBaru1.setError("between 4 and 10 alphanumeric characters");
                valid = false;
                check = false;
            } else {
                check = true;
                inputPasswordBaru1.setError(null);
            }
        }

        if (check) {
            if (!passwordBaru0.equals(passwordBaru1)) {
                inputPasswordBaru0.setError("Password baru tidak sama");
                inputPasswordBaru1.setError("Password baru tidak sama");
                valid = false;
            } else {
                inputPasswordBaru0.setError(null);
                inputPasswordBaru1.setError(null);
            }
        }
        return valid;
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

    private void updatePassword(final String oldPassword, final String newPassword) {
        if (!validate()) {
            onSubmitFailed();
            return;
        }

        btn_submit.setEnabled(false);

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = Config.KEY_URL_UPDATE_PASSWORD;
        SharedPreferences sharedPreferences = this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String _username = sharedPreferences.getString(Pegawai.TAG_USERNAME,"Username");

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
                            if (message.equalsIgnoreCase(Config.MESSAGE_SUCCESS_UPDATE_PASSWORD)) {
                                onSubmitSuccess();
                                Toast.makeText(getApplicationContext(), "Change password SUCCESS", Toast.LENGTH_LONG).show();
                            } else {
                                onSubmitFailed();
                                Toast.makeText(getApplicationContext(), "Submit Error, please re-check password", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            //If the server response is not success
                            //Displaying an error message on toast
                            Toast.makeText(ChangePasswordActivity.this, "No Data", Toast.LENGTH_LONG).show();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyErrorHelper.getMessage(error, ChangePasswordActivity.this);
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                //Adding parameters to request
                params.put("Username", _username);
                params.put("Password_Lama", oldPassword);
                params.put("Password_Baru_0", newPassword);
                params.put("Password_Baru_1", newPassword);
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
