package com.duaruang.pnmportal.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.duaruang.pnmportal.R;
import com.duaruang.pnmportal.config.Config;
import com.duaruang.pnmportal.data.Pegawai;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventDetailActivity extends BaseActivity {

    @BindView(R.id.event_detail_title)
    TextView tvTitle;
    @BindView(R.id.event_nomor_memo)
    TextView tvNomorMemo;
    @BindView(R.id.event_topik_event)
    TextView tvTopikEvent;
    @BindView(R.id.event_mulai_tanggal_pelaksanaan)
    TextView tvMulaiTglPelaksanaan;

    @BindView((R.id.cv_absen))
    CardView cvAbsen;
    @BindView((R.id.cv_rundown))
    CardView cvRundown;
    @BindView((R.id.cv_materi))
    CardView cvMateri;
    @BindView((R.id.cv_feedback))
    CardView cvFeedback;
    @BindView((R.id.cv_location_map))
    CardView cvLocationMap;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_event_detail;
    }

    public static String [] menuList={"Absen","Rundown","Materi","Location"};
    public static int [] iconId={R.drawable.icon_absen,R.drawable.icon_rundown,R.drawable.icon_materi,R.drawable.icon_location};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

        if (ab != null) {
            getSupportActionBar().setTitle("Event Detail");
        }
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        final String idsdm = sharedPreferences.getString(Pegawai.TAG_IDSDM,"B0000000000000000000000");
        final String idevent = getIntent().getStringExtra("id_event");

        tvTitle.setText(getIntent().getStringExtra("nama_event"));
        tvNomorMemo.setText(getIntent().getStringExtra("nomor_memo"));
        tvTopikEvent.setText(getIntent().getStringExtra("topik_event"));
        tvMulaiTglPelaksanaan.setText(getIntent().getStringExtra("mulai_tanggal_pelaksanaan"));

        cvAbsen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventDetailActivity.this, AbsensiActivity.class);
                intent.putExtra("idevent", idevent);
                intent.putExtra("idsdm", idsdm);
                intent.putExtra("nama_event", tvTitle.getText());
                startActivity(intent);
            }
        });

        cvRundown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventDetailActivity.this, RundownActivity.class);
                intent.putExtra("nama_event", tvTitle.getText().toString());
                intent.putExtra("idevent", idevent);
                startActivity(intent);
            }
        });

        cvLocationMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventDetailActivity.this, LocationActivity.class);
                intent.putExtra("idevent", idevent);
                startActivity(intent);
            }
        });

        cvMateri.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventDetailActivity.this, EventMateriActivity.class);
                intent.putExtra("nama_event", tvTitle.getText().toString());
                intent.putExtra("idevent", idevent);
                startActivity(intent);
            }
        });

        cvFeedback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventDetailActivity.this, FeedbackActivity.class);
                intent.putExtra("idevent", idevent);
                intent.putExtra("idsdm", idsdm);
                startActivity(intent);
            }
        });

//        gv=(GridView) findViewById(R.id.gridview_parallax_header);
//        gv.setAdapter(new EventDetailAdapter(this, menuList, iconId));
//        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(EventDetailActivity.this, "You Clicked at " +menuList[+ i], Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}
