package com.duaruang.pnmportal.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.duaruang.pnmportal.R;
import com.duaruang.pnmportal.activity.NewsGeneralDetailActivity;
import com.duaruang.pnmportal.data.News;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.List;

import me.biubiubiu.justifytext.library.JustifyTextView;

/**
 * Created by way on 9/17/2017.
 */

public class MainNewsAdapter extends RecyclerView.Adapter<MainNewsAdapter.NewsViewHolder> {

    private List<News> newsList;
    private Context context;
    private boolean collapseAll = true;
    private static int lastPos = -1;

    private static int currentPosition = -1;

    public MainNewsAdapter(List<News> newsList, Context context) {
        this.newsList = newsList;
        this.context = context;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout_news, parent, false);
        return new NewsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final NewsViewHolder holder, final int position) {
        final News news = newsList.get(position);
        holder.textViewTitle.setText(news.getTitle());
        holder.textViewDescription.setText(news.getDescription());
//        holder.textViewPicture.setText(news.getPicture());
//        holder.textViewCreated.setText(news.getCreated_date());
//        holder.textViewModified.setText(news.getModified_date());

//        Glide.with(context).load(hero.getImageUrl()).into(holder.imageView);

        holder.linearLayout.setVisibility(View.GONE);

        //if the position is equals to the item position which is to be expanded
        if (currentPosition == position) {
            //creating an animation
            Animation slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_down);

            //toggling visibility
            holder.linearLayout.setVisibility(View.VISIBLE);

            //adding sliding effect
            holder.linearLayout.startAnimation(slideDown);

        }

        holder.textViewTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                lastPos = position;
//                //getting the position of the item to expand it
//                if (lastPos != currentPosition) {
//                    collapseAll = false;
//                    currentPosition = position;
////                    lastPos = position;
//                    notifyDataSetChanged();
//                }
//                else {
//                    currentPosition = -1;
//                    holder.linearLayout.setVisibility(View.GONE);
//                }

                //reloding the list

                Intent intent = new Intent(context, NewsGeneralDetailActivity.class);
                intent.putExtra("title", news.getTitle());
                intent.putExtra("description", news.getDescription());
                intent.putExtra("picture", news.getPicture());
                intent.putExtra("url_download", news.getUrlDownload());
                intent.putExtra("created_date", news.getCreated_date());
                context.startActivity(intent);
            }
        });

//        holder.textViewDescription.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, NewsGeneralDetailActivity.class);
//                intent.putExtra("title", news.getTitle());
//                intent.putExtra("description", news.getDescription());
//                intent.putExtra("picture", news.getPicture());
//                intent.putExtra("created_date", news.getCreated_date());
//                context.startActivity(intent);
////                Toast.makeText(context, news.getPicture(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewPicture, textViewCreated,
                textViewModified;
        JustifyTextView textViewDescription;

        LinearLayout linearLayout;

        NewsViewHolder(View itemView) {
            super(itemView);

            textViewTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            textViewDescription = (JustifyTextView) itemView.findViewById(R.id.textViewDescription);
//            textViewPicture = (TextView) itemView.findViewById(R.id.textViewTeam);
//            textViewCreated = (TextView) itemView.findViewById(R.id.textViewFirstAppearance);
//            textViewModified = (TextView) itemView.findViewById(R.id.textViewCreatedBy);

            linearLayout = (LinearLayout) itemView.findViewById(R.id.newsGeneralLinearLayout);
        }
    }
}
