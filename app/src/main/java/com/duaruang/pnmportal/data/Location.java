package com.duaruang.pnmportal.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Location {

    public Location(String idEvent, String namaEvent, String lokasiKerja, String namaTempat, String latitude,
                    String longitude, String kategoriTempat){
        this.idEvent = idEvent;
        this.namaEvent = namaEvent;
        this.lokasiKerja = lokasiKerja;
        this.namaTempat = namaTempat;
        this.latitude = latitude;
        this.longitude = longitude;
        this.kategoriTempat = kategoriTempat;
    }

    @SerializedName("id_event")
    @Expose
    private String idEvent;
    @SerializedName("nama_event")
    @Expose
    private String namaEvent;
    @SerializedName("lokasi_kerja")
    @Expose
    private String lokasiKerja;
    @SerializedName("nama_tempat")
    @Expose
    private String namaTempat;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("kategori_tempat")
    @Expose
    private String kategoriTempat;

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

    public String getLokasiKerja() {
        return lokasiKerja;
    }

    public void setLokasiKerja(String lokasiKerja) {
        this.lokasiKerja = lokasiKerja;
    }

    public String getNamaTempat() {
        return namaTempat;
    }

    public void setNamaTempat(String namaTempat) {
        this.namaTempat = namaTempat;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getKategoriTempat() {
        return kategoriTempat;
    }

    public void setKategoriTempat(String kategoriTempat) {
        this.kategoriTempat = kategoriTempat;
    }
}