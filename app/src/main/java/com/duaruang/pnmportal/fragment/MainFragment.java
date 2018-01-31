package com.duaruang.pnmportal.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.duaruang.pnmportal.R;
import com.duaruang.pnmportal.adapter.MainNewsAdapter;
import com.duaruang.pnmportal.config.Config;
import com.duaruang.pnmportal.data.News;
import com.duaruang.pnmportal.data.Pegawai;
import com.duaruang.pnmportal.helper.VolleyErrorHelper;
import com.duaruang.pnmportal.preference.AppPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private AppPreference appPreference = AppPreference.getInstance();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String realname;

    private OnFragmentInteractionListener mListener;

    private CoordinatorLayout.Behavior behavior;

    @BindView(R.id.tv_welcome_user)
    TextView tv_welcome_user;
    @BindView(R.id.tv_tanggal)
    TextView tv_tanggal;
    @BindView(R.id.image_slider) SliderLayout mDemoSlider;

    private Unbinder unbinder;

    @BindView(R.id.mainFragmentRecyclerView)
    RecyclerView mainNewsRecyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar pb;

    @BindView(R.id.custom_indicator)
    PagerIndicator ci;

    MainNewsAdapter adapter;
    List<News> newsList;

    public MainFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, view);

        // Inflate the layout for this fragment
        return view;

    }

    @Override
    public void onResume() {
        //onResume happens after onStart and onActivityCreate
//        loadNews();
        super.onResume() ;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(getActivity(),slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {

        //Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String str) {
        if (mListener != null) {
            mListener.onFragmentInteraction(str);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String str);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        //Fetching email from shared preferences
        //SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if(appPreference.getUserSSOLoggedIn() == null){
            realname = null;
        }
        else{
            realname = appPreference.getUserSSOLoggedIn().getNama();
        }

        Toast.makeText(getActivity(), "Welcome, "+ realname,
                Toast.LENGTH_SHORT).show();

        String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        tv_tanggal.setText(date);
        tv_welcome_user.setText("Welcome, "+realname+" !");



        mainNewsRecyclerView.setHasFixedSize(true);
        mainNewsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        mainNewsRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        newsList = new ArrayList<>();

        View view = getView();
        if (view != null) {
            loadBanner();
            loadNews();
        }
//        ListView l = (ListView)findViewById(R.id.transformers);
//        l.setAdapter(new TransformerAdapter(this));
//        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                mDemoSlider.setPresetTransformer(((TextView) view).getText().toString());
//                Toast.makeText(MainActivity.this, ((TextView) view).getText().toString(), Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    private void loadBanner(){
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String url = Config.KEY_URL_HOMEPAGE_BANNER;

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
                                //Creating a shared preference
//                                SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                                //Creating editor to store values to shared preferences
//                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                try {
                                    if (dataObj != null) {
                                        JSONArray newsArray = dataObj.getJSONArray("banner");
                                        HashMap<String, String> url_maps = new HashMap<String, String>();
                                        for (int i = 0; i < newsArray.length(); i++) {
                                            JSONObject newsObj = newsArray.getJSONObject(i);

//                                            Log.d("banner " + i + ":", newsObj.getString("item_name"));
                                            url_maps.put(newsObj.getString("description"),
                                                    Config.KEY_URL_ASSET_CONTENT + newsObj.getString("item_name"));
                                        }

                                        if (mDemoSlider != null) {

                                            for (String name : url_maps.keySet()) {
//                                                TextSliderView textSliderView = new TextSliderView(getActivity());
//                                                // initialize a SliderLayout
//                                                textSliderView
//                                                        .description(name)
//                                                        .image(url_maps.get(name))
//                                                        .setScaleType(BaseSliderView.ScaleType.Fit)
//                                                        .setOnSliderClickListener(MainFragment.this);

//                                                //add your extra information
//                                                textSliderView.bundle(new Bundle());
//                                                textSliderView.getBundle()
//                                                        .putString("extra", name);


                                                DefaultSliderView defaultSliderView = new DefaultSliderView(getActivity());

                                                defaultSliderView
                                                        .description(name)
                                                        .image(url_maps.get(name))
                                                        .setScaleType(BaseSliderView.ScaleType.Fit)
                                                        .setOnSliderClickListener(MainFragment.this);
                                                //add your extra information
                                                defaultSliderView.bundle(new Bundle());
                                                defaultSliderView.getBundle()
                                                        .putString("extra", name);


                                                mDemoSlider.addSlider(defaultSliderView);
                                            }
                                            mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
//                                            mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                                            mDemoSlider.setCustomIndicator(ci);
                                            mDemoSlider.setCustomAnimation(new DescriptionAnimation());
                                            mDemoSlider.setDuration(4000);
                                            mDemoSlider.addOnPageChangeListener(MainFragment.this);

                                        } else {
                                            Log.d("mDemoSlider","null");

                                        }


                                        adapter = new MainNewsAdapter(newsList, getContext());
                                        mainNewsRecyclerView.setAdapter(adapter);
                                        mainNewsRecyclerView.setVisibility(View.VISIBLE);
                                        pb.setVisibility(View.GONE);

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            } else {
                                //If the server response is not success
                                //Displaying an error message on toast
                                Toast.makeText(getActivity(), "Tidak ada data banner", Toast.LENGTH_LONG).show();
                            }
                        }


                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyErrorHelper.getMessage(error, getActivity());
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

    private void loadNews() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String url = Config.KEY_URL_NEWS_GENERAL;

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
                                //Creating a shared preference
//                                SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                                //Creating editor to store values to shared preferences
//                                SharedPreferences.Editor editor = sharedPreferences.edit();

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

                                        adapter = new MainNewsAdapter(newsList, getContext());
                                        mainNewsRecyclerView.setAdapter(adapter);
                                        mainNewsRecyclerView.setVisibility(View.VISIBLE);
                                        pb.setVisibility(View.GONE);

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (NullPointerException e){
                                    e.printStackTrace();
                                }

                            } else {
                                //If the server response is not success
                                //Displaying an error message on toast
                                Toast.makeText(getActivity(), "No New News", Toast.LENGTH_LONG).show();
                            }
                        }


                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyErrorHelper.getMessage(error, getActivity());
                Toast.makeText(getActivity(), "No New News", Toast.LENGTH_LONG).show();
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
