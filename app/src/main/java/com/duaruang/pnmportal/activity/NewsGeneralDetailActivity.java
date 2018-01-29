package com.duaruang.pnmportal.activity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.duaruang.pnmportal.R;
import com.duaruang.pnmportal.config.Config;
import com.duaruang.pnmportal.helper.WrapContentDraweeView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.biubiubiu.justifytext.library.JustifyTextView;

import static com.android.volley.VolleyLog.TAG;

public class NewsGeneralDetailActivity extends BaseActivity {

    Context mcontext;

    @BindView(R.id.news_general_detail_title)
    TextView tvTitle;
    @BindView(R.id.newsdownload)
    ImageView newsDownload;
    @BindView(R.id.news_general_detail_tanggal)
    TextView tvTanggal;
    @BindView(R.id.news_general_detail_description)
    JustifyTextView tvDescription;
    @BindView(R.id.news_general_detail_image)
    WrapContentDraweeView tvPicture;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_news_general_detail;
    }

//    public static String [] menuList={"Absen","Rundown","Materi","Location"};
//    public static int [] iconId={R.drawable.icon_absen,R.drawable.icon_rundown,R.drawable.icon_materi,R.drawable.icon_location};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
        if (ab != null)
            ab.setTitle("News");

        tvTitle.setText(getIntent().getStringExtra("title"));
        tvDescription.setText(getIntent().getStringExtra("description"));

        String dateTime[]=getIntent().getStringExtra("created_date").split(" ");

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
        checkStoragePermission();
        String date = null;
        try {
            Date d = inputFormat.parse(dateTime[0]);
            date = outputFormat.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tvTanggal.setText(date);

        Uri uri = Uri.parse(Config.KEY_URL_ASSET_NEWS + getIntent().getStringExtra("picture"));
        tvPicture.setImageURI(uri);
        String urls = getIntent().getStringExtra("url_download");


        Log.v(TAG,"url download :"+urls);
        if( urls.equals("null")) {
            newsDownload.setVisibility(View.GONE);
        }else {
            newsDownload.setVisibility(View.VISIBLE);
            newsDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isDownloadManagerAvailable(getApplication())){
                        Toast.makeText(getApplication(), "Downloading", Toast.LENGTH_SHORT).show();

                        String url = "http://"+getIntent().getStringExtra("url_download");
                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                        request.setTitle(getIntent().getStringExtra("title"));
                        // in order for this if to run, you must use the android 3.2 to compile your app
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            request.allowScanningByMediaScanner();
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        }
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, getIntent().getStringExtra("title")+".pdf");

                        // get download service and enqueue file
                        DownloadManager manager = (DownloadManager) getApplication().getSystemService(Context.DOWNLOAD_SERVICE);
                        manager.enqueue(request);
                    } else {
                        Toast.makeText(getApplication(), "Download Manager tidak tersedia!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void checkStoragePermission(){
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static boolean isDownloadManagerAvailable(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return true;
        }
        return false;
    }
}
