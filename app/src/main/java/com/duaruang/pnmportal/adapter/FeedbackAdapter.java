package com.duaruang.pnmportal.adapter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.duaruang.pnmportal.R;
import com.duaruang.pnmportal.data.Feedback;
import com.duaruang.pnmportal.data.Question;

import java.util.ArrayList;
import java.util.List;

public class FeedbackAdapter extends BaseAdapter{
    Context context;
    List<Feedback> questionsList;
    int flag;
    LayoutInflater inflter;
    public static ArrayList<String> selectedAnswers;

    private ArrayList<Boolean> itemCheckedYes;
    private ArrayList<Boolean> itemCheckedNo;
    public static ArrayList<Boolean> radioGroupCheck;

    public FeedbackAdapter(Context applicationContext, List<Feedback> questionsList, int flag) {
        this.context = context;
        this.questionsList = questionsList;
        this.flag = flag;
        // initialize arraylist and add static string for all the questions
        selectedAnswers = new ArrayList<>();
        itemCheckedYes = new ArrayList<>();
        itemCheckedNo = new ArrayList<>();
        radioGroupCheck = new ArrayList<>();
        for (int i = 0; i < questionsList.size(); i++) {
            selectedAnswers.add("Belum dijawab");
            itemCheckedYes.add(i, false);
            itemCheckedNo.add(i, false);
            radioGroupCheck.add(i, false);
        }
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return questionsList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.list_item_feedback, null);
        // get the reference of TextView and Button's
        TextView question = (TextView) view.findViewById(R.id.question);
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGroupLayout);
        RadioButton yes = (RadioButton) view.findViewById(R.id.yes);
        RadioButton no = (RadioButton) view.findViewById(R.id.no);
        // perform setOnCheckedChangeListener event on yes button

        if (flag == 0) {

            yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // set Yes values in ArrayList if RadioButton is checked
                    radioGroupCheck.set(i, true);
                    if (isChecked) {
                        selectedAnswers.set(i, "Ya");
                        itemCheckedYes.set(i, true);
                    } else {
                        itemCheckedYes.set(i, false);
                    }
                }
            });
            // perform setOnCheckedChangeListener event on no button
            no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // set No values in ArrayList if RadioButton is checked
                    radioGroupCheck.set(i, true);
                    if (isChecked) {
                        selectedAnswers.set(i, "Tidak");
                        itemCheckedNo.set(i, true);
                    } else {
                        itemCheckedNo.set(i, false);
                    }
                }
            });
// set the value in TextView

            question.setText(questionsList.get(i).getQuestion());
            yes.setChecked(itemCheckedYes.get(i));
            no.setChecked(itemCheckedNo.get(i));
        } else {
            question.setText(questionsList.get(i).getQuestion());
            if (TextUtils.equals("Ya", questionsList.get(i).getAnswer())) {
                yes.setChecked(true);
                yes.setEnabled(false);
                no.setChecked(false);
                no.setEnabled(false);
            }
            if (TextUtils.equals("Tidak", questionsList.get(i).getAnswer())) {
                yes.setChecked(false);
                yes.setEnabled(false);
                no.setChecked(true);
                no.setEnabled(false);
            }
        }
        return view;
    }
}
