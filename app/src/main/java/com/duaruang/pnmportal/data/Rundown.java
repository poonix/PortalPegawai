package com.duaruang.pnmportal.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rundown {

    @SerializedName("id_event")
    @Expose
    private String idEvent;
    @SerializedName("nama_event")
    @Expose
    private String namaEvent;
    @SerializedName("tanggal")
    @Expose
    private String tanggal;
    @SerializedName("waktu")
    @Expose
    private String waktu;
    @SerializedName("nama_kegiatan")
    @Expose
    private String namaKegiatan;
    @SerializedName("pic")
    @Expose
    private String pic;
    @SerializedName("keterangan")
    @Expose
    private String keterangan;

    public Rundown(String idEvent, String namaEvent, String tanggal, String waktu, String namaKegiatan,
                   String pic, String keterangan){
        this.idEvent = idEvent;
        this.namaEvent = namaEvent;
        this.tanggal = tanggal;
        this.waktu = waktu;
        this.namaKegiatan = namaKegiatan;
        this.pic = pic;
        this.keterangan = keterangan;
    }

    public String getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(String idEvent) {
        this.idEvent = idEvent;
    }

    public String getNamaEvent() {
        return namaEvent;
    }

    public void setNamaEvent(String namaEvent) {
        this.namaEvent = namaEvent;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getNamaKegiatan() {
        return namaKegiatan;
    }

    public void setNamaKegiatan(String namaKegiatan) {
        this.namaKegiatan = namaKegiatan;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
}
