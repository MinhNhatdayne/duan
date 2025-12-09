package com.example.nhakhoaapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class HoSoBenhAn {
    @SerializedName("_id")
    private int id_ho_so;
    private int id_benhnhan;
    private int id_bac_si_kham;
    private Date ngay_kham;
    private String chan_doan;
    private String ket_qua_xet_nghiem;
    private String img;

    public HoSoBenhAn(int id_ho_so, int id_benhnhan, int id_bac_si_kham, Date ngay_kham, String chan_doan, String ket_qua_xet_nghiem, String img) {
        this.id_ho_so = id_ho_so;
        this.id_benhnhan = id_benhnhan;
        this.id_bac_si_kham = id_bac_si_kham;
        this.ngay_kham = ngay_kham;
        this.chan_doan = chan_doan;
        this.ket_qua_xet_nghiem = ket_qua_xet_nghiem;
        this.img = img;
    }
}