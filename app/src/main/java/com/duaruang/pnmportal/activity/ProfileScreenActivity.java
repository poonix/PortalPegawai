package com.duaruang.pnmportal.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.duaruang.pnmportal.R;
import com.duaruang.pnmportal.config.Config;
import com.duaruang.pnmportal.data.Pegawai;

import net.glxn.qrgen.android.QRCode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileScreenActivity extends AppCompatActivity {

    @BindView(R.id.profile_toolbar) Toolbar toolbar;
    @BindView(R.id.user_profile_name)
    TextView userProfileName;
    @BindView(R.id.user_profile_email)
    TextView userProfileEmail;

    @BindView(R.id.tv_nik)
    TextView tvNik;
    //@BindView(R.id.tv_idsdm)
    //TextView tvIdsdm;
    @BindView(R.id.tv_posisi)
    TextView tvPosisi;

//    @BindView(R.id.imageBarcode)
//    ImageView imageBarcode;

    final int MY_PERMISSIONS_REQUEST_CAMERA = 101;

    Bitmap idsdmQR;
    String idsdm,nip,nama,posisi,unit_kerja,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Fetching data from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userProfileName.setText(sharedPreferences.getString(Pegawai.TAG_NAMA,"John Doe"));
        userProfileEmail.setText(sharedPreferences.getString(Pegawai.TAG_EMAIL,"user@email.com"));
        //tvIdsdm.setText(sharedPreferences.getString(Pegawai.TAG_IDSDM,"B0000000000000000000000"));
        tvNik.setText(sharedPreferences.getString(Pegawai.TAG_NIK,"11111111"));
        tvPosisi.setText(sharedPreferences.getString(Pegawai.TAG_POSISI_NAMA,"karyawan"));

        idsdm = sharedPreferences.getString(Pegawai.TAG_IDSDM,"B0000000000000000000000");
        nip = sharedPreferences.getString(Pegawai.TAG_NIK,"11111111");
        nama = sharedPreferences.getString(Pegawai.TAG_NAMA,"John Doe");
        posisi = sharedPreferences.getString(Pegawai.TAG_POSISI_NAMA,"karyawan");
        unit_kerja = sharedPreferences.getString(Pegawai.TAG_UNIT,"IT Dev");
        email = sharedPreferences.getString(Pegawai.TAG_EMAIL,"user@email.com");


        Bitmap idsdmQRreal = QRCode.from(idsdm).bitmap();
//        imageBarcode.setImageBitmap(getResizedBitmap(idsdmQRreal, 512, 512));
        idsdmQR = getResizedBitmap(idsdmQRreal, 1024, 1024);
    }

    @OnClick(R.id.btn_qr2)
    public void barcodeScanner(){

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);

            // MY_PERMISSIONS_REQUEST_CAMERA is an
            // app-defined int constant. The callback method gets the
            // result of the request.

        } else {
            Intent intent = new Intent(ProfileScreenActivity.this, BarcodeScanner.class);
            intent.putExtra("idsdm", idsdm);
            intent.putExtra("nip", nip);
            intent.putExtra("nama", nama);
            intent.putExtra("posisi", posisi);
            intent.putExtra("unit_kerja", unit_kerja);
            intent.putExtra("email", email);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // camera-related task you need to do.
                    Intent intent = new Intent(ProfileScreenActivity.this, BarcodeScanner.class);
                    intent.putExtra("idsdm", idsdm);
                    intent.putExtra("nip", nip);
                    intent.putExtra("nama", nama);
                    intent.putExtra("posisi", posisi);
                    intent.putExtra("unit_kerja", unit_kerja);
                    intent.putExtra("email", email);
                    startActivity(intent);

                } else {
                    Log.d("Camera: ", "Permission needed!");

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Please allow camera permission first!", Toast.LENGTH_LONG);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void onButtonShowPopupQrClick(View view) {

        // get a reference to the already created main layout
        ConstraintLayout mainLayout = (ConstraintLayout) findViewById(R.id.activity_profile_screen_layout);

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);

        ImageView popupBarcode = popupView.findViewById(R.id.popupImageBarcode);
        popupBarcode.setImageBitmap(idsdmQR);


        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        popupWindow.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);



        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        if (bm != null && !bm.isRecycled()) {
            bm.recycle();
            bm = null;
        }
        return resizedBitmap;
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
