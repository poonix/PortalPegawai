package com.duaruang.pnmportal.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Absensi {
    @SerializedName("nama_event")
    @Expose
    private String namaEvent;
    @SerializedName("id_event")
    @Expose
    private String idEvent;
    @SerializedName("idsdm")
    @Expose
    private String idsdm;
    @SerializedName("created_by")
    @Expose
    private String createdBy;
    @SerializedName("created_date")
    @Expose
    private String createdDate;

    public Absensi(String namaEvent, String idEvent, String idsdm, String createdBy, String createdDate){
        this.namaEvent = namaEvent;
        this.idEvent = idEvent;
        this.idsdm = idsdm;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
    }

    public String getNamaEvent() {
        return namaEvent;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
