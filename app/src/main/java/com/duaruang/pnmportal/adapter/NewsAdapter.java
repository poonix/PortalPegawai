package com.duaruang.pnmportal.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.duaruang.pnmportal.R;
import com.duaruang.pnmportal.activity.NewsGeneralDetailActivity;
import com.duaruang.pnmportal.data.News;

import java.util.List;

public class NewsAdapter extends
        RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    private List<News> list_item ;
    public static Context mcontext;


    public NewsAdapter(List<News> list, Context context) {

        list_item = list;
        mcontext = context;
    }

    // Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
    @Override
    public NewsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a layout
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item_news, null);

        MyViewHolder myViewHolder = new MyViewHolder(view);


        return myViewHolder;
    }

    // Called by RecyclerView to display the data at the specified position.
    @Override
    public void onBindViewHolder(final MyViewHolder viewHolder, final int position ) {

        final News news = list_item.get(position);
        viewHolder.news_title.setText(news.getTitle());
        viewHolder.news_subtitle.setText(news.getDescription());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mcontext, NewsGeneralDetailActivity.class);
                intent.putExtra("title", news.getTitle());
                intent.putExtra("description", news.getDescription());
                intent.putExtra("picture", news.getPicture());
                intent.putExtra("url_download", news.getUrlDownload());
                intent.putExtra("created_date", news.getCreated_date());
                mcontext.startActivity(intent);
            }
        });

    }

    // initializes textview in this class
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView news_title, news_subtitle;

        public MyViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            news_title = (TextView) itemLayoutView.findViewById(R.id.news_title);
            news_subtitle = (TextView) itemLayoutView.findViewById(R.id.news_subtitle);

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