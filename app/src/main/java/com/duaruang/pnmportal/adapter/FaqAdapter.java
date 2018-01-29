package com.duaruang.pnmportal.adapter;

import android.content.Context;
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
import com.duaruang.pnmportal.data.Faq;

import java.util.List;

public class FaqAdapter extends
        RecyclerView.Adapter<FaqAdapter.MyViewHolder> {

    private List<Faq> list_item ;
    public Context mcontext;

    private boolean collapseAll = true;
    private static int lastPos = -1;
    private static int currentPosition = -1;

    public FaqAdapter(List<Faq> list, Context context) {

        list_item = list;
        mcontext = context;
    }

    // Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
    @Override
    public FaqAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a layout
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item_faq, null);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    // Called by RecyclerView to display the data at the specified position.
    @Override
    public void onBindViewHolder(final MyViewHolder viewHolder, final int position ) {

        final Faq faq = list_item.get(position);
        viewHolder.faq_question.setText(faq.getQuestion());
        viewHolder.faq_answer.setText(faq.getAnswer());

//        viewHolder.faq_title.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Toast.makeText(mcontext, faq.getQuestion(),
//                        Toast.LENGTH_SHORT).show();
//            }
//        });

        viewHolder.linearLayout.setVisibility(View.GONE);

        //if the position is equals to the item position which is to be expanded
        if (currentPosition == position) {
            //creating an animation
            Animation slideDown = AnimationUtils.loadAnimation(mcontext, R.anim.slide_down);

            //toggling visibility
            viewHolder.linearLayout.setVisibility(View.VISIBLE);

            //adding sliding effect
            viewHolder.linearLayout.startAnimation(slideDown);

        }

        viewHolder.faq_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lastPos = position;
                //getting the position of the item to expand it
                if (lastPos != currentPosition) {
                    collapseAll = false;
                    currentPosition = position;
//                    lastPos = position;
                    notifyDataSetChanged();
                }
                else {
                    currentPosition = -1;
                    viewHolder.linearLayout.setVisibility(View.GONE);
                }


            }
        });

    }

    // initializes textview in this class
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView faq_question;
        public TextView faq_answer;

        LinearLayout linearLayout;

        public MyViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            faq_question = (TextView) itemLayoutView.findViewById(R.id.faq_question);
            faq_answer = (TextView) itemLayoutView.findViewById(R.id.faq_answer);

            linearLayout = (LinearLayout) itemView.findViewById(R.id.faqGeneralLinearLayout);
        }
    }

    //Returns the total number of items in the data set hold by the adapter.
    @Override
    public int getItemCount() {
        return list_item.size();
    }

}