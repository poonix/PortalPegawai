package com.duaruang.pnmportal.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pegawai implements Parcelable{
    // JSON Node names
    public static final String TAG_MESSAGE = "message";
    public static final String TAG_DATA = "data";
    public static final String TAG_IDSDM = "idsdm";
    public static final String TAG_NIK = "nik";
    public static final String TAG_PERUSAHAAN = "perusahaan";
    public static final String TAG_NOMOR_INDUK = "nomor_induk";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_NAMA = "nama";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_POSISI_NAMA = "posisi_nama";
    public static final String TAG_POSISI_SSO = "posisi_sso";
    public static final String TAG_POSISI_SINGKATAN = "posisi_singkatan";
    public static final String TAG_LOKASI_KERJA = "lokasi_kerja";
    public static final String TAG_KODE_CABANG = "kode_cabang";
    public static final String TAG_CABANG = "cabang";
    public static final String TAG_UNIT = "unit";
    public static final String TAG_KODE_UNIT = "kode_unit";

    public static final String TAG_RESPONSE = "response";

    @SerializedName("idsdm")
    @Expose
    private String idsdm;

    @SerializedName("nik")
    @Expose
    private String nik;

    @SerializedName("perusahaan")
    @Expose
    private String perusahaan;

    @SerializedName("nomor_induk")
    @Expose
    private String nomorInduk;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("nama")
    @Expose
    private String nama;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("posisi_nama")
    @Expose
    private String posisiNama;

    @SerializedName("posisi_sso")
    @Expose
    private String posisiSso;

    @SerializedName("posisi_singkatan")
    @Expose
    private String posisiSingkatan;

    @SerializedName("lokasi_kerja")
    @Expose
    private String lokasiKerja;

    @SerializedName("kode_cabang")
    @Expose
    private String kodeCabang;

    @SerializedName("cabang")
    @Expose
    private String cabang;

    @SerializedName("unit")
    @Expose
    private String unit;

    @SerializedName("kode_unit")
    @Expose
    private String kodeUnit;

    public Pegawai() {

    }

    protected Pegawai(Parcel in) {
        idsdm = in.readString();
        nik = in.readString();
        perusahaan = in.readString();
        nomorInduk = in.readString();
        username = in.readString();
        nama = in.readString();
        email = in.readString();
        posisiNama = in.readString();
        posisiSso = in.readString();
        posisiSingkatan = in.readString();
        lokasiKerja = in.readString();
        kodeCabang = in.readString();
        cabang = in.readString();
        unit = in.readString();
        kodeUnit = in.readString();
    }

    public static final Parcelable.Creator<Pegawai> CREATOR = new Parcelable.Creator<Pegawai>() {
        @Override
        public Pegawai createFromParcel(Parcel in) {
            return new Pegawai(in);
        }

        @Override
        public Pegawai[] newArray(int size) {
            return new Pegawai[size];
        }
    };

    public String getIdsdm() {
        return idsdm;
    }

    public void setIdsdm(String idsdm) {
        this.idsdm = idsdm;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getPerusahaan() {
        return perusahaan;
    }

    public void setPerusahaan(String perusahaan) {
        this.perusahaan = perusahaan;
    }

    public String getNomorInduk() {
        return nomorInduk;
    }

    public void setNomorInduk(String nomorInduk) {
        this.nomorInduk = nomorInduk;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPosisiNama() {
        return posisiNama;
    }

    public void setPosisiNama(String posisiNama) {
        this.posisiNama = posisiNama;
    }

    public String getPosisiSso() {
        return posisiSso;
    }

    public void setPosisiSso(String posisiSso) {
        this.posisiSso = posisiSso;
    }

    public String getPosisiSingkatan() {
        return posisiSingkatan;
    }

    public void setPosisiSingkatan(String posisiSingkatan) {
        this.posisiSingkatan = posisiSingkatan;
    }

    public String getLokasiKerja() {
        return lokasiKerja;
    }

    public void setLokasiKerja(String lokasiKerja) {
        this.lokasiKerja = lokasiKerja;
    }

    public String getKodeCabang() {
        return kodeCabang;
    }

    public void setKodeCabang(String kodeCabang) {
        this.kodeCabang = kodeCabang;
    }

    public String getCabang() {
        return cabang;
    }

    public void setCabang(String cabang) {
        this.cabang = cabang;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getKodeUnit() {
        return kodeUnit;
    }

    public void setKodeUnit(String kodeUnit) {
        this.kodeUnit = kodeUnit;
    }

    @Override
    public String toString() {
        return "UserSSOModel{" +
                "idsdm='" + idsdm + '\'' +
                ", nik='" + nik + '\'' +
                ", perusahaan='" + perusahaan + '\'' +
                ", nomorInduk='" + nomorInduk + '\'' +
                ", username='" + username + '\'' +
                ", nama='" + nama + '\'' +
                ", email='" + email + '\'' +
                ", posisiNama='" + posisiNama + '\'' +
                ", posisiSso=" + posisiSso +
                ", posisiSingkatan=" + posisiSingkatan +
                ", lokasiKerja='" + lokasiKerja + '\'' +
                ", kodeCabang='" + kodeCabang + '\'' +
                ", cabang='" + cabang + '\'' +
                ", unit='" + unit + '\'' +
                ", kodeUnit='" + kodeUnit + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idsdm);
        dest.writeString(nik);
        dest.writeString(perusahaan);
        dest.writeString(nomorInduk);
        dest.writeString(username);
        dest.writeString(nama);
        dest.writeString(email);
        dest.writeString(posisiNama);
        dest.writeString(posisiSso);
        dest.writeString(posisiSingkatan);
        dest.writeString(lokasiKerja);
        dest.writeString(kodeCabang);
        dest.writeString(cabang);
        dest.writeString(unit);
        dest.writeString(kodeUnit);
    }

}