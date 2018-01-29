package com.duaruang.pnmportal.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.duaruang.pnmportal.R;
import com.duaruang.pnmportal.adapter.FeedbackAdapter;
import com.duaruang.pnmportal.config.Config;
import com.duaruang.pnmportal.helper.VolleyErrorHelper;
import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BarcodeScanner extends BaseActivity implements ZXingScannerView.ResultHandler{
    private static final String TAG = "BarcodeMain";
    private ZXingScannerView mScannerView;

    @BindView(R.id.progressBarHolder)
    FrameLayout pbHolder;

    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_barcode_scanner;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

        ActionBar ab = getSupportActionBar();
        if (ab !=null)
            ab.setTitle("Scan Event Barcode");

        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(BarcodeScanner.this);
        contentFrame.addView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void onStop() {
        super.onStop();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(final Result rawResult) {
        if (rawResult == null) {
            Toast.makeText(getApplicationContext(), "Pemindai barcode gagal. Ulangi lagi!", Toast.LENGTH_LONG).show();
            finish();
        } else {
            // Do something with the result here
            Log.v(TAG, rawResult.getText()); // Prints scan results
            Log.v(TAG, rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

            try {
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                r.play();
            } catch (Exception e) {
            }
            mScannerView.stopCamera();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    sendAbsen(rawResult.getText());
//
//                    AlertDialog.Builder builder = new AlertDialog.Builder(BarcodeScanner.this)
//                            .setTitle("Absen ke Event")
////                        .setMessage(rawResult.getText())
//                            .setMessage("Anda ingin absen ke event ini?")
//                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    //If you would like to resume scanning, call this method below:
//                                    mScannerView.resumeCameraPreview(BarcodeScanner.this);
//                                    sendAbsen(rawResult.getText());
//                                    dialog.dismiss();
//                                }
//                            })
//                            .setCancelable(false);
//                    builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            finish();
//                        }
//                    });
//                    builder.show();
                }
            });
        }
    }

    private void sendAbsen(final String _idevent){

        inAnimation = new AlphaAnimation(0f, 1f);
        inAnimation.setDuration(200);
        pbHolder.setAnimation(inAnimation);
        pbHolder.setVisibility(View.VISIBLE);

        final String idsdm = getIntent().getStringExtra("idsdm");
        final String nip = getIntent().getStringExtra("nip");
        final String nama = getIntent().getStringExtra("nama");
        final String posisi = getIntent().getStringExtra("posisi");
        final String unit_kerja = getIntent().getStringExtra("unit_kerja");
        final String email = getIntent().getStringExtra("email");
        String idevent = _idevent;

        JSONArray obj = new JSONArray();

        try {
            JSONObject jsonobject_one = new JSONObject();

            jsonobject_one.put("idsdm", idsdm);
            jsonobject_one.put("nip", nip);
            jsonobject_one.put("nama", nama);
            jsonobject_one.put("posisi", posisi);
            jsonobject_one.put("unit_kerja", unit_kerja);
            jsonobject_one.put("email", email);
            jsonobject_one.put("is_active","active");

            obj.put(jsonobject_one);

        }catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject jsonBody = new JSONObject();
        try {

            JSONObject jsonobject = new JSONObject();
            jsonobject.put("user", obj);
            jsonobject.put("idevent", idevent);

            jsonBody = jsonobject;
            Log.d("eventObj: ", jsonobject.toString());


        } catch (JSONException e){

        }

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = Config.KEY_URL_POST_ATTENDANCE;

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
                            outAnimation = new AlphaAnimation(1f, 0f);
                            outAnimation.setDuration(200);
                            pbHolder.setAnimation(outAnimation);
                            pbHolder.setVisibility(View.GONE);

                            if (status.equalsIgnoreCase(Config.SUCCESS)) {
                                Intent intent = new Intent(BarcodeScanner.this, AbsensiActivity.class);
                                intent.putExtra("idevent", _idevent);
                                intent.putExtra("idsdm", idsdm);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "Absen sukses!", Toast.LENGTH_LONG).show();
                                BarcodeScanner.this.finish();
                                mScannerView.stopCamera();
                                //Creating a shared preference
//                                SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                            } else {
                                //If the server response is not success
                                //Displaying an error message on toast
//                                mScannerView.resumeCameraPreview(BarcodeScanner.this);
                                Toast.makeText(getApplicationContext(), "Tidak dapat mengirim Absensi! Cek barcode event kembali", Toast.LENGTH_LONG).show();
                                BarcodeScanner.this.finish();
                            }
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyErrorHelper.getMessage(error, getApplicationContext());
                Toast.makeText(getApplicationContext(), "Server error! Silahkan cek barcode event kembali.", Toast.LENGTH_LONG).show();
                BarcodeScanner.this.finish();
            }
        }){

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

    @Override
    public void onBackPressed() {
        mScannerView.stopCamera();           // Stop camera
        super.onBackPressed();
    }
}
