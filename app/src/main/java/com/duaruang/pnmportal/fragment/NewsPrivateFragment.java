package com.duaruang.pnmportal.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.duaruang.pnmportal.adapter.NewsAdapter;
import com.duaruang.pnmportal.config.Config;
import com.duaruang.pnmportal.data.News;
import com.duaruang.pnmportal.data.Pegawai;
import com.duaruang.pnmportal.data.Usergroup;
import com.duaruang.pnmportal.helper.DividerItemDecoration;
import com.duaruang.pnmportal.helper.VolleyErrorHelper;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NewsPrivateFragment extends Fragment {
    private SharedPreferences sharedPreferences;
    public NewsPrivateFragment(){}

    @BindView(R.id.inputSearchNews) EditText inputSearch;
    @BindView(R.id.recyclerview_news) RecyclerView mainNewsRecyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar pb;
    @BindView(R.id.error)
    TextView tvError;


    private NewsAdapter mAdapter;
    private Unbinder unbinder;

    List<News> newsList;
    List<Usergroup> positionMod;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news_general, container, false);
        unbinder = ButterKnife.bind(this, view);

        sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        Drawable icon_search = new IconicsDrawable(getContext()).icon(FontAwesome.Icon.faw_search).color(Color.GRAY).sizeDp(16);
        inputSearch.setError(null);
        inputSearch.setCompoundDrawablesWithIntrinsicBounds(null, null, icon_search, null);

        mainNewsRecyclerView.setHasFixedSize(true);
        mainNewsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mainNewsRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        return view;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        newsList = new ArrayList<>();

        getPosition();
        addTextListener();
    }

    private void getPosition() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String getPositionSession= sharedPreferences.getString(Pegawai.TAG_POSISI_NAMA,"default");
        String url = Config.KEY_URL_GET_POSITION+getPositionSession.replace(" ","_");

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
                            tvError.setText("Terjadi Kesalahan");
                            pb.setVisibility(View.GONE);
                        }

                        if (message !=null) {
                            //If we are getting success from server
                            if (message.equalsIgnoreCase(Config.DATA_FOUND)) {

                                try {
                                    if (dataObj != null) {
                                        JSONArray newsArray = dataObj.getJSONArray("usergroup");
                                        for (int i = 0; i < newsArray.length(); i++) {
                                            JSONObject newsObj = newsArray.getJSONObject(i);

                                            Usergroup usg = new Usergroup(
                                                    newsObj.getString("usergroup"),
                                                    newsObj.getString("position"),
                                                    newsObj.getInt("id")
                                            );

                                            loadNews(usg.getId());
                                        }

                                    }



                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    pb.setVisibility(View.GONE);
                                }



                            } else {
                                //If the server response is not success
                                //Displaying an error message on toast
                                tvError.setText("Tidak ada data berita");
                                pb.setVisibility(View.GONE);
                            }
                        }


                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyErrorHelper.getMessage(error, getActivity());
                tvError.setText("error api");

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

    private void loadNews(int id) {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String url = Config.KEY_URL_NEWS_PRIVATE+id;

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
                                        JSONArray newsArray = dataObj.getJSONArray("news");
                                        for (int i = 0; i < newsArray.length(); i++) {
                                            JSONObject newsObj = newsArray.getJSONObject(i);

                                            Log.d("title "+i+":", newsObj.getString("title"));


                                            News news = new News(
                                                    newsObj.getString("title"),
                                                    newsObj.getString("description"),
                                                    newsObj.getString("picture"),
                                                    newsObj.getString("url_download"),
                                                    newsObj.getString("created_date"),
                                                    newsObj.getString("modified_date")
                                            );

                                            newsList.add(news);
                                        }

                                        mAdapter = new NewsAdapter(newsList, getActivity());
                                        mainNewsRecyclerView.setAdapter(mAdapter);
                                        mainNewsRecyclerView.setVisibility(View.VISIBLE);
                                        pb.setVisibility(View.GONE);

                                    }



                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    pb.setVisibility(View.GONE);
                                }



                            } else {
                                //If the server response is not success
                                //Displaying an error message on toast
                                pb.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "Tidak ada data berita", Toast.LENGTH_LONG).show();
                            }
                        }


                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyErrorHelper.getMessage(error, getActivity());
                tvError.setText("Tidak ada data");

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



    public void addTextListener(){

        inputSearch.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence query, int start, int before, int count) {

                query = query.toString().toLowerCase();

                final List<News> filteredList = new ArrayList<>();

                for (int i = 0; i < newsList.size(); i++) {

                    final String text = newsList.get(i).getTitle().toLowerCase();
                    if (text.contains(query)) {

                        filteredList.add(newsList.get(i));
                    }
                }

                mainNewsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                mAdapter = new NewsAdapter(filteredList, getContext());
                mainNewsRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();  // data set changed
            }
        });
    }
}
