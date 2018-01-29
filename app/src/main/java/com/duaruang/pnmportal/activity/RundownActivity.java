package com.duaruang.pnmportal.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.duaruang.pnmportal.adapter.RundownAdapter;
import com.duaruang.pnmportal.config.Config;
import com.duaruang.pnmportal.data.Absensi;
import com.duaruang.pnmportal.data.Rundown;
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

public class RundownActivity extends BaseActivity {

    private RecyclerView mRecyclerView;

    @BindView(R.id.tv_Judul_Rundown)
    TextView tvJudulRundown;

    // ArrayList for Listview
    private List<Rundown> rundownList = new ArrayList<Rundown>();

    public RundownAdapter mAdapter;

    String idevent;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_event_rundown;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_event_rundown);
        ButterKnife.bind(this);
//        setSupportActionBar(toolbar);

        if (ab !=null) {
            getSupportActionBar().setTitle("Rundown");
//            ab.setDisplayHomeAsUpEnabled(true);
        }

        tvJudulRundown.setText(getIntent().getStringExtra("nama_event"));

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_rundown);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(RundownActivity.this, LinearLayoutManager.VERTICAL));

        idevent = getIntent().getStringExtra("idevent");
        loadRundown(idevent);
    }

    private void loadRundown(String _idevent) {
        RequestQueue queue = Volley.newRequestQueue(this);

//        String url = Config.KEY_URL_GET_RUNDOWN+"AW38M7ALZB1486918800"; //test
        String url = Config.KEY_URL_GET_RUNDOWN+_idevent;

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
                                        JSONArray rundownArray = dataObj.getJSONArray("material");
                                        for (int i = 0; i < rundownArray.length(); i++) {
                                            JSONObject rundownObj = rundownArray.getJSONObject(i);

//                                            Log.d("title "+i+":", newsObj.getString("title"));

                                            Rundown rundown = new Rundown(
                                                    rundownObj.getString("id_event"),
                                                    rundownObj.getString("nama_event"),
                                                    rundownObj.getString("tanggal"),
                                                    rundownObj.getString("waktu"),
                                                    rundownObj.getString("nama_kegiatan"),
                                                    rundownObj.getString("pic"),
                                                    rundownObj.getString("keterangan")
                                            );

                                            rundownList.add(rundown);
                                        }

                                        mAdapter = new RundownAdapter(rundownList, RundownActivity.this);
                                        mRecyclerView.setAdapter(mAdapter);
                                    }



                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }



                            } else {
                                //If the server response is not success
                                //Displaying an error message on toast
                                Toast.makeText(getApplicationContext(), "Tidak Ada Rundown Baru", Toast.LENGTH_LONG).show();
                            }
                        }


                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyErrorHelper.getMessage(error, getApplicationContext());
                Toast.makeText(getApplicationContext(), "Tidak Ada Rundown Baru", Toast.LENGTH_LONG).show();
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
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        //handle the click on the back arrow click
//        switch (item.getItemId()) {
////            case R.id.menu_faq1:
////                //todo
////                return true;
////            case R.id.menu_faq2:
////                //todo
////                return true;
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
