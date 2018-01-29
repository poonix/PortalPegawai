package com.duaruang.pnmportal.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Feedback {

    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("id_event")
    @Expose
    private String idEvent;
    @SerializedName("idsdm")
    @Expose
    private String idsdm;
    @SerializedName("answer")
    @Expose
    private String answer;

    public Feedback(String question, String idEvent, String idsdm, String answer){
        this.question = question;
        this.idEvent = idEvent;
        this.idsdm = idsdm;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(String idEvent) {
        this.idEvent = idEvent;
    }

    public String getIdsdm() {
        return idsdm;
    }

    public void setIdsdm(String idsdm) {
        this.idsdm = idsdm;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

}