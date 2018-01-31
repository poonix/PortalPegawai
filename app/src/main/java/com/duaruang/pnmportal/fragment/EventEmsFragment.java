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
import com.duaruang.pnmportal.adapter.EventAdapter;
import com.duaruang.pnmportal.adapter.EventEmsAdapter;
import com.duaruang.pnmportal.config.Config;
import com.duaruang.pnmportal.data.Event;
import com.duaruang.pnmportal.data.EventEms;
import com.duaruang.pnmportal.data.Pegawai;
import com.duaruang.pnmportal.helper.DividerItemDecoration;
import com.duaruang.pnmportal.helper.VolleyErrorHelper;
import com.duaruang.pnmportal.preference.AppPreference;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;

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
import butterknife.Unbinder;

public class EventEmsFragment extends Fragment {

    public EventEmsFragment(){}
    private AppPreference appPreference = AppPreference.getInstance();
    @BindView(R.id.inputSearchEvent) EditText inputSearch;
    @BindView(R.id.recyclerview_event) RecyclerView mainEventRecyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar pb;
    @BindView(R.id.error)
    TextView tvError;

    private EventEmsAdapter mAdapter;
    private Unbinder unbinder;

    List<EventEms> eventEmsList;

    SharedPreferences sharedPreferences;

    // ArrayList for Listview
//    private List<String> newsList = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_ems, container, false);
        unbinder = ButterKnife.bind(this, view);

        Drawable icon_search = new IconicsDrawable(getContext()).icon(FontAwesome.Icon.faw_search).color(Color.GRAY).sizeDp(16);
        inputSearch.setError(null);
        inputSearch.setCompoundDrawablesWithIntrinsicBounds(null, null, icon_search, null);


        mainEventRecyclerView.setHasFixedSize(true);
        mainEventRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mainEventRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

//        newsList();


//        adapter = new MainNewsAdapter(newsList,getActivity());
//        mRecyclerView.setAdapter(adapter);
//
//        addTextListener();

        return view;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        eventEmsList = new ArrayList<>();
        loadEvent();
        addTextListener();
    }

    private void loadEvent() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        final String idsdm =  appPreference.getUserSSOLoggedIn().getIdsdm();
        String url = Config.KEY_URL_EVENT_EMS + idsdm;
//        String url = Config.KEY_URL_EVENT_EMS + "B0BA88CF0E61B42E50B3BCF968579979";

        if (idsdm == null)
        {
            tvError.setText("No New Event");
            return;
        }
        Log.d("idsdm ems: ", idsdm);

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
                            tvError.setText("No New Event");
                            pb.setVisibility(View.GONE);

                        }

                        if (message !=null) {
                            //If we are getting success from server
                            if (message.equalsIgnoreCase(Config.DATA_FOUND)) {
                                //Creating a shared preference
//                                SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                                //Creating editor to store values to shared preferences
//                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                try {
                                    if (dataObj != null) {
                                        JSONArray newsArray = dataObj.getJSONArray("event");
                                        for (int i = 0; i < newsArray.length(); i++) {
                                            JSONObject newsObj = newsArray.getJSONObject(i);

                                            Log.d("title "+i+":", newsObj.getString("id_event"));


                                            EventEms eventEms = new EventEms(
                                                    newsObj.getString("id_event"),
                                                    newsObj.getString("nomor_memo"),
                                                    newsObj.getString("nama_event"),
                                                    newsObj.getString("topik_event"),
                                                    newsObj.getString("mulai_tanggal_pelaksanaan"),
                                                    newsObj.getString("selesai_tanggal_pelaksanaan"),
                                                    newsObj.getString("kategori_event"),
                                                    newsObj.getString("tipe_pelatihan")
                                            );

                                            eventEmsList.add(eventEms);
                                        }

                                        mAdapter = new EventEmsAdapter(eventEmsList, getContext());
                                        mainEventRecyclerView.setAdapter(mAdapter);
                                        mainEventRecyclerView.setVisibility(View.VISIBLE);
                                        pb.setVisibility(View.GONE);

                                    }



                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }



                            } else {
                                //If the server response is not success
                                //Displaying an error message on toast
                                tvError.setText("Tidak ada data event");
                                pb.setVisibility(View.GONE);
                            }
                        }


                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyErrorHelper.getMessage(error, getActivity());
                tvError.setText("No New Event");
                pb.setVisibility(View.GONE);
//                try {
//                    String responseBody = new String( error.networkResponse.data, "utf-8" );
//                    JSONObject jsonObject = new JSONObject( responseBody );
//                } catch ( JSONException e ) {
//                    //Handle a malformed json response
//                } catch (UnsupportedEncodingException ue){
//
//                }
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

                final List<EventEms> filteredList = new ArrayList<>();

                for (int i = 0; i < eventEmsList.size(); i++) {

                    final String text = eventEmsList.get(i).getNama_event().toLowerCase();
                    if (text.contains(query)) {

                        filteredList.add(eventEmsList.get(i));
                    }
                }

                mainEventRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                mAdapter = new EventEmsAdapter(filteredList, getContext());
                mainEventRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();  // data set changed
            }
        });
    }
}
