package com.duaruang.pnmportal.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import com.duaruang.pnmportal.adapter.FaqAdapter;
import com.duaruang.pnmportal.config.Config;
import com.duaruang.pnmportal.data.Absensi;
import com.duaruang.pnmportal.data.Faq;
import com.duaruang.pnmportal.helper.DividerItemDecoration;
import com.duaruang.pnmportal.helper.VolleyErrorHelper;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FaqActivity extends BaseActivity {
    // List view
    private ListView lv;

    // Listview Adapter
    ArrayAdapter<String> adapter;

    // Search EditText
    @BindView(R.id.inputSearchFaq)
    EditText inputSearch;

    private RecyclerView mRecyclerView;


    // ArrayList for Listview
    private List<Faq> faqList = new ArrayList<Faq>();

    public FaqAdapter mAdapter;
    @BindView(R.id.progress_bar)
    ProgressBar pb;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_faq;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
        if (ab !=null) {
            getSupportActionBar().setTitle("FAQ");
        }
//
        Drawable icon_search = new IconicsDrawable(this).icon(FontAwesome.Icon.faw_search).color(Color.GRAY).sizeDp(16);
        inputSearch.setError(null);
        inputSearch.setCompoundDrawablesWithIntrinsicBounds(null, null, icon_search, null);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_faq);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // call the adapter with argument list of items and context.
        loadFaq();
        mAdapter = new FaqAdapter(faqList,this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

//        addTextListener();

    }

    private void loadFaq() {
        String idsdm = getIntent().getStringExtra("idsdm");
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Config.KEY_URL_FAQ;

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
                                        JSONArray newsArray = dataObj.getJSONArray("faq");
                                        for (int i = 0; i < newsArray.length(); i++) {
                                            JSONObject newsObj = newsArray.getJSONObject(i);

//                                            Log.d("title "+i+":", newsObj.getString("title"));

                                            Faq faq = new Faq(
                                                    newsObj.getString("question"),
                                                    newsObj.getString("answer"),
                                                    newsObj.getString("created_date"),
                                                    newsObj.getString("modified_date")
                                            );

                                            faqList.add(faq);
                                        }

                                        mAdapter = new FaqAdapter(faqList, FaqActivity.this);
                                        mRecyclerView.setAdapter(mAdapter);
                                        mRecyclerView.setVisibility(View.VISIBLE);
                                        pb.setVisibility(View.GONE);
                                        addTextListener();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            } else {
                                //If the server response is not success
                                //Displaying an error message on toast
                                Toast.makeText(getApplicationContext(), "No New FAQ", Toast.LENGTH_LONG).show();
                            }
                        }


                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyErrorHelper.getMessage(error, getApplicationContext());
                Toast.makeText(getApplicationContext(), "No New FAQ", Toast.LENGTH_LONG).show();
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

    public void addTextListener(){

        inputSearch.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence query, int start, int before, int count) {

                query = query.toString().toLowerCase();

                final List<Faq> filteredList = new ArrayList<>();

                for (int i = 0; i < faqList.size(); i++) {

                    final String text = faqList.get(i).getQuestion().toLowerCase();
                    if (text.contains(query)) {

                        filteredList.add(faqList.get(i));
                    }
                }

                mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                mAdapter = new FaqAdapter(filteredList, FaqActivity.this);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();  // data set changed
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.faq_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle the click on the back arrow click
        switch (item.getItemId()) {
            case R.id.menu_faq3:
                //sort by name

                Collections.sort(faqList, new Comparator<Faq>() {

                /* This comparator will sort Faq objects alphabetically. */

                    @Override
                    public int compare(Faq a1, Faq a2) {

                        // String implements Comparable
                        return (a1.getQuestion()).compareTo(a2.getQuestion());
                    }
                });
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                mAdapter = new FaqAdapter(faqList, FaqActivity.this);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();  // data set changed
                return true;
//            case R.id.menu_faq2:
//                //todo
//                return true;
//            case R.id.menu_faq3:
//                //todo
//                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
