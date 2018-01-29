package com.duaruang.pnmportal.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Base64;
import android.util.Log;
import android.view.View;

import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.duaruang.pnmportal.R;

import com.duaruang.pnmportal.adapter.MaterialAdapter;
import com.duaruang.pnmportal.config.Config;

import com.duaruang.pnmportal.data.Material;
import com.duaruang.pnmportal.helper.DividerItemDecoration;
import com.duaruang.pnmportal.helper.VolleyErrorHelper;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.volley.VolleyLog.TAG;

public class EventMateriActivity extends BaseActivity {

    // ArrayList for Listview
    private List<Material> materialList = new ArrayList<Material>();

    public MaterialAdapter mAdapter;

    String idevent;

    @BindView(R.id.recyclerview_materi)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_judul_materi_download)
    TextView tvJudulMateriDownload;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_event_materi;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_event_materi);
        ButterKnife.bind(this);

        if (ab != null)
            ab.setTitle("Event Materi");

        idevent = getIntent().getStringExtra("idevent");
        tvJudulMateriDownload.setText(getIntent().getStringExtra("nama_event"));

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // call the adapter with argument list of items and context.
//        faqList();

        mAdapter = new MaterialAdapter(materialList,this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        loadMateri(idevent);
        checkStoragePermission();

    }

    private void checkStoragePermission(){
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");

            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission

        }
    }

    private void loadMateri(String idevent) {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Config.KEY_URL_EVENT_MATERIAL+idevent;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response: ", response);
                        String message = null;
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
                                        JSONArray newsArray = dataObj.getJSONArray("material");
                                        for (int i = 0; i < newsArray.length(); i++) {
                                            JSONObject materialObj = newsArray.getJSONObject(i);

//                                            Log.d("title "+i+":", newsObj.getString("title"));

                                            Material material = new Material(
                                                    materialObj.getString("id_event"),
                                                    materialObj.getString("nama_file"),
                                                    materialObj.getString("tipe_file")
                                            );

                                            materialList.add(material);
                                        }

                                        mAdapter = new MaterialAdapter(materialList, EventMateriActivity.this);
                                        mRecyclerView.setAdapter(mAdapter);
                                        mRecyclerView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                            }
                                        });

                                    }



                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }



                            } else {
                                //If the server response is not success
                                //Displaying an error message on toast
                                Toast.makeText(getApplicationContext(), "Tidak Ada Material Baru", Toast.LENGTH_LONG).show();
                            }
                        }


                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyErrorHelper.getMessage(error, getApplicationContext());
                Toast.makeText(getApplicationContext(), "Tidak Ada Material Baru", Toast.LENGTH_LONG).show();
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

}
