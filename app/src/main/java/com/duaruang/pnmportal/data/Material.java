package com.duaruang.pnmportal.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Material {

    @SerializedName("id_event")
    @Expose
    private String idEvent;
    @SerializedName("nama_file")
    @Expose
    private String namaFile;
    @SerializedName("tipe_file")
    @Expose
    private String tipeFile;

    public Material(String idEvent, String namaFile, String tipeFile){
        this.idEvent = idEvent;
        this.namaFile = namaFile;
        this.tipeFile = tipeFile;
    }

    public String getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(String idEvent) {
        this.idEvent = idEvent;
    }

    public String getNamaFile() {
        return namaFile;
    }

    public void setNamaFile(String namaFile) {
        this.namaFile = namaFile;
    }

    public String getTipeFile() {
        return tipeFile;
    }

    public void setTipeFile(String tipeFile) {
        this.tipeFile = tipeFile;
    }

}