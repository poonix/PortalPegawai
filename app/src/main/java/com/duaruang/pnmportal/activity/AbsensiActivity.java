package com.duaruang.pnmportal.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
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
import com.duaruang.pnmportal.adapter.AbsensiAdapter;
import com.duaruang.pnmportal.config.Config;
import com.duaruang.pnmportal.data.Absensi;
import com.duaruang.pnmportal.helper.DividerItemDecoration;
import com.duaruang.pnmportal.helper.VolleyErrorHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AbsensiActivity extends BaseActivity {

    private RecyclerView mRecyclerView;


    // ArrayList for Listview
    private List<Absensi> absensiList = new ArrayList<Absensi>();

    public AbsensiAdapter mAdapter;

    @BindView(R.id.progress_bar)
    ProgressBar pb;
    @BindView(R.id.judul_absensi)
    TextView tvJudulAbsensi;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_absensi;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

        ActionBar ab = getSupportActionBar();
        if (ab !=null) {
            getSupportActionBar().setTitle("Absensi");
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_absensi);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(AbsensiActivity.this, LinearLayoutManager.VERTICAL));
        // call the adapter with argument list of items and context.

        loadAbsensi(getIntent().getStringExtra("idevent"));
    }

    private void loadAbsensi(String _idevent) {
        String idsdm = getIntent().getStringExtra("idsdm");
        RequestQueue queue = Volley.newRequestQueue(this);
        String url;

        if (idsdm != null)
            url = Config.KEY_URL_GET_ATTENDANCE+_idevent+"&idsdm="+idsdm;
        else
            return;

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
                                Log.d("Absensi: ", message);

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
                                        JSONArray newsArray = dataObj.getJSONArray("attendance");
                                        String namaEvent = "Nama Event";
                                        for (int i = 0; i < newsArray.length(); i++) {
                                            JSONObject absensiObj = newsArray.getJSONObject(i);

//                                            Log.d("title "+i+":", newsObj.getString("title"));

                                            Absensi absensi = new Absensi(
                                                    absensiObj.getString("nama_event"),
                                                    absensiObj.getString("id_event"),
                                                    absensiObj.getString("idsdm"),
                                                    absensiObj.getString("created_by"),
                                                    absensiObj.getString("created_date")
                                            );

                                            absensiList.add(absensi);
                                            namaEvent = absensiObj.getString("nama_event");
                                        }

                                        mAdapter = new AbsensiAdapter(absensiList, AbsensiActivity.this);
                                        mRecyclerView.setAdapter(mAdapter);
                                        mRecyclerView.setVisibility(View.VISIBLE);
                                        pb.setVisibility(View.GONE);
                                        tvJudulAbsensi.setText(namaEvent);
                                    }



                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                //If the server response is not success
                                //Displaying an error message on toast
                                Toast.makeText(getApplicationContext(), "Tidak ada data Absensi", Toast.LENGTH_LONG).show();
                                pb.setVisibility(View.GONE);
                            }
                        }


                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyErrorHelper.getMessage(error, AbsensiActivity.this);
                Toast.makeText(getApplicationContext(), "Tidak ada data Absensi", Toast.LENGTH_LONG).show();
                pb.setVisibility(View.GONE);
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

//    public void addTextListener(){
//
//        inputSearch.addTextChangedListener(new TextWatcher() {
//
//            public void afterTextChanged(Editable s) {}
//
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            public void onTextChanged(CharSequence query, int start, int before, int count) {
//
//                query = query.toString().toLowerCase();
//
//                final List<String> filteredList = new ArrayList<>();
//
//                for (int i = 0; i < faqList.size(); i++) {
//
//                    final String text = faqList.get(i).toLowerCase();
//                    if (text.contains(query)) {
//
//                        filteredList.add(faqList.get(i));
//                    }
//                }
//
//                mRecyclerView.setLayoutManager(new LinearLayoutManager(EventMateriActivity.this));
//                mAdapter = new FaqAdapter(filteredList, EventMateriActivity.this);
//                mRecyclerView.setAdapter(mAdapter);
//                mAdapter.notifyDataSetChanged();  // data set changed
//            }
//        });
//    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.faq_menu, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        //handle the click on the back arrow click
//        switch (item.getItemId()) {
//            case R.id.menu_faq1:
//                //todo
//                return true;
//            case R.id.menu_faq2:
//                //todo
//                return true;
//            case R.id.menu_faq3:
//                //todo
//                return true;
//            case android.R.id.home:
//                onBackPressed();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
}
