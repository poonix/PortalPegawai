package com.duaruang.pnmportal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.duaruang.pnmportal.R;
import com.duaruang.pnmportal.data.Rundown;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RundownAdapter extends
        RecyclerView.Adapter<RundownAdapter.MyViewHolder> {

    private List<Rundown> list_item ;
    public static Context mcontext;


    public RundownAdapter(List<Rundown> list, Context context) {

        list_item = list;
        mcontext = context;
    }

    // Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
    @Override
    public RundownAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a layout
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item_rundown, null);

        MyViewHolder myViewHolder = new MyViewHolder(view);


        return myViewHolder;
    }

    // Called by RecyclerView to display the data at the specified position.
    @Override
    public void onBindViewHolder(final MyViewHolder viewHolder, final int position ) {

        final Rundown rundown = list_item.get(position);
//        String dateTime[]=rundown.getCreatedDate().split(" ");
//        viewHolder.tvJamAbsen.setText(dateTime[1]);
//
//        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
//        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
//
//        String date = null;
//        try {
//            Date d = inputFormat.parse(dateTime[0]);
//            date = outputFormat.format(d);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

//        Log.d("DATETIME", "dateTime[0]: "+ dateTime[0]+" dateTime[1]: "+ dateTime[1]);

        viewHolder.tvKegiatan.setText(rundown.getNamaKegiatan());
        viewHolder.tvPic.setText(rundown.getPic());
     //   viewHolder.tvKeterangan.setText(rundown.getKeterangan());
    //    viewHolder.tvTanggal.setText(rundown.getTanggal());
        viewHolder.tvWaktu.setText(rundown.getWaktu());


    }

    // initializes textview in this class
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tvKegiatan;
        public TextView tvTanggal;
        public TextView tvPic;
        public TextView tvWaktu;
        public TextView tvKeterangan;

        public MyViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            tvKegiatan = (TextView) itemLayoutView.findViewById(R.id.tv_kegiatan);
         //   tvTanggal = (TextView) itemLayoutView.findViewById(R.id.tv_tanggal);
          //  tvKeterangan = (TextView) itemLayoutView.findViewById(R.id.tv_keterangan);
            tvPic = (TextView) itemLayoutView.findViewById(R.id.tv_pic);
            tvWaktu = (TextView) itemLayoutView.findViewById(R.id.tv_waktu);

//            itemLayoutView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(mcontext, NewsGeneralDetailActivity.class);
//                    intent.putExtra("title", news.getTitle());
//                    intent.putExtra("description", news.getDescription());
//                    intent.putExtra("picture", news.getPicture());
//                    intent.putExtra("created_date", news.getCreated_date());
//                    mcontext.startActivity(intent);
//                }
//            });

        }
    }

    //Returns the total number of items in the data set hold by the adapter.
    @Override
    public int getItemCount() {
        return list_item.size();
    }

}