package com.duaruang.pnmportal.activity;


import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.duaruang.pnmportal.R;
import com.duaruang.pnmportal.adapter.FeedbackAdapter;
import com.duaruang.pnmportal.adapter.NewsAdapter;
import com.duaruang.pnmportal.config.Config;
import com.duaruang.pnmportal.data.Feedback;
import com.duaruang.pnmportal.data.News;
import com.duaruang.pnmportal.data.Question;
import com.duaruang.pnmportal.helper.VolleyErrorHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeedbackActivity extends BaseActivity {
    ListView simpleList;
//    String[] questions = {"Test Question 1", "Test Question 2", "Test Question 3", "Test Question 4", "Test Question 5"};
    List<Feedback> questionList;
    @BindView(R.id.btn_submit)
    AppCompatButton submit;

    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    @BindView(R.id.progressBarHolder)
    FrameLayout pbHolder;

    @BindView(R.id.progress_bar)
    ProgressBar pb;
    @BindView(R.id.tvSudahFeedback)
    TextView tvSudahFeedback;
    @BindView(R.id.feedback_layout)
    RelativeLayout fbLayout;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
//        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Feedback");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        questionList = new ArrayList<>();

        // get the string array from string.xml file
        //        questions = getResources().getStringArray(R.array.questions);
        // get the reference of ListView and Button
        simpleList = (ListView) findViewById(R.id.simpleListView);

        // set the adapter to fill the data in the ListView
//        FeedbackAdapter customAdapter = new FeedbackAdapter(getApplicationContext(), questions);
//        simpleList.setAdapter(customAdapter);
        // perform setOnClickListerner event on Button
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "";
                // get the value of selected answers from custom adapter
                for (int i = 0; i < FeedbackAdapter.selectedAnswers.size(); i++) {
                    message = message + "\n" + (i + 1) + " " + FeedbackAdapter.selectedAnswers.get(i);
                }

                if (isListContainCondition(FeedbackAdapter.radioGroupCheck)){
                    //is checked
                    message = message + "\n" + "Sudah terisi semua";
                    sendFeedback();
                } else {
                    //not checked
                    message = message + "\n" + "Belum terisi semua, cek kembali";
                }

                // display the message on screen with the help of Toast.
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
//                sendFeedback();
            }
        });
        loadFeedback(getIntent().getStringExtra("idevent"), getIntent().getStringExtra("idsdm"));
    }

    private static boolean isListContainCondition(List<Boolean> arraylist) {
        for (Boolean str : arraylist) {
            if (!str) {
                return false;
            }
        }
        return true;
    }

    private void sendFeedback(){

        submit.setEnabled(false);
        inAnimation = new AlphaAnimation(0f, 1f);
        inAnimation.setDuration(200);
        pbHolder.setAnimation(inAnimation);
        pbHolder.setVisibility(View.VISIBLE);

        String idsdm = getIntent().getStringExtra("idsdm");
        String idevent = getIntent().getStringExtra("idevent");

        JSONArray obj = new JSONArray();

        for (int i = 0; i < FeedbackAdapter.selectedAnswers.size(); i++) {
//            message = message + "\n" + (i + 1) + " " + FeedbackAdapter.selectedAnswers.get(i);

            try {

                JSONObject jsonobject_one = new JSONObject();

                jsonobject_one.put( "id", Integer.toString(i+1));
                jsonobject_one.put("answer", FeedbackAdapter.selectedAnswers.get(i));

                obj.put(jsonobject_one);

            }catch (JSONException e) {
                e.printStackTrace();
            }
        }

        JSONObject jsonBody = new JSONObject();
        try {
//            JSONObject jsonobject_two = new JSONObject();
//            jsonobject_two.put("idevent", idevent);
//
//            JSONObject jsonobject_three = new JSONObject();
//            jsonobject_three.put("idsdm", idsdm);
//
//            JSONObject jsonobject_four = new JSONObject();
//            jsonobject_four.put("iduser", "1");


            JSONObject jsonobject = new JSONObject();
            jsonobject.put("feedback", obj);
            jsonobject.put("idevent", idevent);
            jsonobject.put("idsdm", idsdm);
            jsonobject.put("iduser", "1");

            jsonBody = jsonobject;
            Log.d("feedbackObj: ", jsonobject.toString());


        } catch (JSONException e){

        }

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = Config.KEY_URL_POST_FEEDBACK;

//        final String mRequestBody = jsonBody.toString();

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response: ", response.toString());
                        String message = null;
                        String status = null;

                        try {
                            //Do it with this it will work
//                            JSONObject jsonObject = new JSONObject(response);
                            if (response != null) {
                                message = response.getString("message");
                                status = response.getString("status");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();

                        }

                        if (status !=null) {
                            //If we are getting success from server
                            if (status.equalsIgnoreCase(Config.SUCCESS)) {

                                outAnimation = new AlphaAnimation(1f, 0f);
                                outAnimation.setDuration(200);
                                pbHolder.setAnimation(outAnimation);
                                pbHolder.setVisibility(View.GONE);

                                //Creating a shared preference
//                                SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                                Toast.makeText(getApplicationContext(), "Feedback sukses terkirim!", Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                //If the server response is not success
                                //Displaying an error message on toast
                                Toast.makeText(getApplicationContext(), "Tidak dapat mengirim feedback, coba lagi", Toast.LENGTH_LONG).show();
                            }
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyErrorHelper.getMessage(error, getApplicationContext());
            }
        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> params = new HashMap<String, String>();
//                //Adding parameters to request
//
//                //returning parameter
//                return params;
//            }

//            @Override
//            public String getBodyContentType() {
//                return String.format("application/json; charset=utf-8");
//            }

//            @Override
//            public byte[] getBody() throws AuthFailureError {
//                try {
//                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
//                } catch (UnsupportedEncodingException uee) {
//                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
//                    return null;
//                }
//            }
//
//            @Override
//            protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                String responseString = "";
//                if (response != null) {
//
//                    responseString = String.valueOf(response.statusCode);
//
//                }
//                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
//            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String,String> headers = new HashMap<String, String>();
                // add headers <key,value>
                String credentials = "username:password";
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(),
                        Base64.DEFAULT);
                headers.put("Authorization", auth);
                headers.put("Content-Type","application/json; charset=utf-8");
                return headers;

            }
        };

        queue.add(stringRequest);

    }

    private void loadFeedback(String idevent, String idsdm) {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = Config.KEY_URL_GET_FEEDBACK+idevent+"&idsdm="+idsdm;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response: ", response);
                        String message = null;
                        String flag_submitted = null;
                        JSONObject dataObj = null;

                        try {
                            //Do it with this it will work
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject != null) {

                                message = jsonObject.getString("message");
                                dataObj = jsonObject;

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();

                        }

                        if (message !=null) {
                            //If we are getting success from server
                            if (message.equalsIgnoreCase(Config.DATA_FOUND)) {

                                try {
                                    if (dataObj != null) {
                                        flag_submitted = dataObj.getString("flag_submitted");
                                        Log.d("flag submitted: ", flag_submitted);

                                        if (Integer.parseInt(flag_submitted) == 1){
//                                            submit.setEnabled(false);
//                                            submit.setText("Sudah pernah submit");
                                            submit.setVisibility(View.GONE);
                                            Toast.makeText(getApplicationContext(), "Sudah pernah submit feedback", Toast.LENGTH_SHORT).show();
//                                            simpleList.setVisibility(View.GONE);
//                                            tvSudahFeedback.setVisibility(View.VISIBLE);
//                                            return;
                                        }

                                        JSONArray newsArray = dataObj.getJSONArray("feedback");
                                        for (int i = 0; i < newsArray.length(); i++) {
                                            JSONObject newsObj = newsArray.getJSONObject(i);

                                            Log.d("question "+i+":", newsObj.getString("question"));

                                            Feedback q = new Feedback(
                                                    newsObj.getString("question"),
                                                    newsObj.getString("id_event"),
                                                    newsObj.getString("idsdm"),
                                                    newsObj.getString("answer")
                                            );

                                            questionList.add(q);

                                        }
                                        FeedbackAdapter customAdapter = new FeedbackAdapter(getApplicationContext(),
                                                questionList, Integer.parseInt(flag_submitted));
                                        simpleList.setAdapter(customAdapter);


                                        fbLayout.setVisibility(View.VISIBLE);
                                        pb.setVisibility(View.GONE);

                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }



                            } else {
                                //If the server response is not success
                                //Displaying an error message on toast
                                Toast.makeText(getApplicationContext(), "No New Feedback", Toast.LENGTH_LONG).show();
                            }
                        }


                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyErrorHelper.getMessage(error, getApplicationContext());
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
