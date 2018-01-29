package com.duaruang.pnmportal.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.duaruang.pnmportal.R;
import com.duaruang.pnmportal.activity.EventDetailActivity;
import com.duaruang.pnmportal.activity.EventGeneralDetailActivity;
import com.duaruang.pnmportal.data.Event;
import com.duaruang.pnmportal.data.EventEms;

import java.util.List;

public class EventEmsAdapter extends
        RecyclerView.Adapter<EventEmsAdapter.MyViewHolder> {

    private List<EventEms> list_item ;
    public static Context mcontext;


    public EventEmsAdapter(List<EventEms> list, Context context) {

        list_item = list;
        mcontext = context;
    }

    // Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
    @Override
    public EventEmsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a layout
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item_event, null);

        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    // Called by RecyclerView to display the data at the specified position.
    @Override
    public void onBindViewHolder(final MyViewHolder viewHolder, final int position ) {

        final EventEms event = list_item.get(position);
        viewHolder.event_title.setText(event.getNama_event());
        viewHolder.event_subtitle.setText(event.getTopik_event());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mcontext, EventDetailActivity.class);
                intent.putExtra("id_event", event.getId_event());
                intent.putExtra("nomor_memo", event.getNomor_memo());
                intent.putExtra("nama_event", event.getNama_event());
                intent.putExtra("topik_event", event.getTopik_event());
                intent.putExtra("mulai_tanggal_pelaksanaan", event.getMulai_tanggal_pelaksanaan());
                mcontext.startActivity(intent);
            }
        });

    }

    // initializes textview in this class
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView event_title, event_subtitle;

        public MyViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            event_title = (TextView) itemLayoutView.findViewById(R.id.event_title);
            event_subtitle = (TextView) itemLayoutView.findViewById(R.id.event_subtitle);

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