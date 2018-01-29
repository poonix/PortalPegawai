package com.duaruang.pnmportal.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
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
import com.duaruang.pnmportal.config.Config;
import com.duaruang.pnmportal.data.Absensi;
import com.duaruang.pnmportal.data.Location;
import com.duaruang.pnmportal.helper.VolleyErrorHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocationActivity extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap mMap;


    private String lat,lng;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_location;
    }

    SupportMapFragment mapFragment;
    String nama_tempat, lokasi_kerja;

    @BindView(R.id.nama_event)
    TextView namaEvent;
    @BindView(R.id.lokasi_kerja)
    TextView lokasiKerja;
    @BindView(R.id.nama_tempat)
    TextView namaTempat;

    String idevent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_location);
        ButterKnife.bind(this);
//        setSupportActionBar(toolbar);
        if (ab != null)
            ab.setTitle("Location");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        idevent = getIntent().getStringExtra("idevent");
        loadLocation(idevent);

    }

    @OnClick(R.id.btn_open_map)
    public void OpenMap(){
        String strUri = "http://maps.google.com/maps?q=loc:" + lat + "," + lng + " (" + lokasi_kerja + ")";
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));

        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

        startActivity(intent);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng eventLocation = new LatLng(Double.parseDouble(lat),Double.parseDouble(lng));
        mMap.addMarker(new MarkerOptions().position(eventLocation).title("Location"));
//        if (ActivityCompat.checkSelfPermission(this,
//                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(this,
//                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
        try {
            float zoomLevel = 16.0f; //This goes up to 21
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eventLocation, zoomLevel));
        } catch (IllegalArgumentException e){
            Log.e("Error: ",""+e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadLocation(String _idevent) {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = Config.KEY_URL_EVENT_LOCATION+_idevent;

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
                                        JSONArray newsArray = dataObj.getJSONArray("location");
                                        for (int i = 0; i < newsArray.length(); i++) {
                                            JSONObject locObj = newsArray.getJSONObject(i);

//                                            Log.d("title "+i+":", newsObj.getString("title"));



                                            Location location = new Location(
                                                    locObj.getString("id_event"),
                                                    locObj.getString("nama_event"),
                                                    locObj.getString("lokasi_kerja"),
                                                    locObj.getString("nama_tempat"),
                                                    locObj.getString("latitude"),
                                                    locObj.getString("longitude"),
                                                    locObj.getString("kategori_tempat")
                                            );

                                            mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                                    .findFragmentById(R.id.map);
                                            mapFragment.getMapAsync(LocationActivity.this);
//                                            lat = "-6.1753871";
//                                            lng = "106.8249641";
                                            lat = locObj.getString("latitude");
                                            lng = locObj.getString("longitude");
                                            nama_tempat = locObj.getString("nama_tempat");
                                            namaEvent.setText(locObj.getString("nama_event"));
                                            lokasiKerja.setText(locObj.getString("lokasi_kerja"));
                                            namaTempat.setText(nama_tempat);
                                            lokasi_kerja = locObj.getString("lokasi_kerja");

                                        }

                                    }



                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }



                            } else {
                                //If the server response is not success
                                //Displaying an error message on toast
                                Toast.makeText(getApplicationContext(), "No New Location", Toast.LENGTH_LONG).show();
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
}
