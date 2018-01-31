package com.duaruang.pnmportal.data;

import android.os.Parcel;
import android.os.Parcelable;

public class EventEms implements Parcelable {

    private String id_event, nomor_memo, nama_event, topik_event, mulai_tanggal_pelaksanaan,
            selesai_tanggal_pelaksanaan, kategori_event, tipe_pelatihan;

    public EventEms(String id_event, String nomor_memo, String nama_event, String topik_event, String mulai_tanggal_pelaksanaan,
                    String selesai_tanggal_pelaksanaan, String kategori_event, String tipe_pelatihan){
        this.id_event = id_event;
        this.nomor_memo = nomor_memo;
        this.nama_event = nama_event;
        this.topik_event = topik_event;
        this.mulai_tanggal_pelaksanaan = mulai_tanggal_pelaksanaan;
        this.selesai_tanggal_pelaksanaan = selesai_tanggal_pelaksanaan;
        this.kategori_event = kategori_event;
        this.tipe_pelatihan = tipe_pelatihan;
    }

    public String getId_event(){
        return id_event;
    }

    public String getNomor_memo(){
        return nomor_memo;
    }

    public String getNama_event(){
        return nama_event;
    }

    public String getTopik_event(){
        return topik_event;
    }

    public String getMulai_tanggal_pelaksanaan(){
        return mulai_tanggal_pelaksanaan;
    }

    public String getSelesai_tanggal_pelaksanaan(){
        return selesai_tanggal_pelaksanaan;
    }

    public String getKategori_event(){
        return kategori_event;
    }

    public String getTipe_pelatihan(){
        return tipe_pelatihan;
    }

    @Override
    public String toString()
    {
        return String.format(
                "[event: id_event=%1$s, nomor_memo=%2$s, nama_event=%3$s, topik_event=%4$s, mulai_tanggal_pelaksanaan=%5$s, " +
                        "selesai_tanggal_pelaksanaan=%6$s, kategori_event=%7$s, tipe_pelatihan=%8$s]",
                id_event, nomor_memo, nama_event, topik_event, mulai_tanggal_pelaksanaan,
                selesai_tanggal_pelaksanaan, kategori_event, tipe_pelatihan);
    }

    protected EventEms(Parcel in) {
        id_event = in.readString();
        nomor_memo = in.readString();
        nama_event = in.readString();
        topik_event = in.readString();
        mulai_tanggal_pelaksanaan = in.readString();
        selesai_tanggal_pelaksanaan = in.readString();
        kategori_event = in.readString();
        tipe_pelatihan = in.readString();
    }

    public static final Creator<EventEms> CREATOR = new Creator<EventEms>() {
        @Override
        public EventEms createFromParcel(Parcel in) {
            return new EventEms(in);
        }

        @Override
        public EventEms[] newArray(int size) {
            return new EventEms[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_event);
        dest.writeString(nomor_memo);
        dest.writeString(nama_event);
        dest.writeString(topik_event);
        dest.writeString(mulai_tanggal_pelaksanaan);
        dest.writeString(selesai_tanggal_pelaksanaan);
        dest.writeString(kategori_event);
        dest.writeString(tipe_pelatihan);
    }
}
