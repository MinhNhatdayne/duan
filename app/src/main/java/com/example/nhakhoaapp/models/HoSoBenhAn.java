package com.example.nhakhoaapp.models;

import java.util.Date;

public class HoSoBenhAn {
    private int id_ho_so;
    private int id_benhnhan;
    private int id_bac_si;
    private Date ngay_kham;
    private String chan_doan;
    private String ket_qua_xet_nghiem;
    private String toa_thuoc;

    public HoSoBenhAn(int id_ho_so, int id_benhnhan, int id_bac_si, Date ngay_kham, String chan_doan, String ket_qua_xet_nghiem, String toa_thuoc) {
        this.id_ho_so = id_ho_so;
        this.id_benhnhan = id_benhnhan;
        this.id_bac_si = id_bac_si;
        this.ngay_kham = ngay_kham;
        this.chan_doan = chan_doan;
        this.ket_qua_xet_nghiem = ket_qua_xet_nghiem;
        this.toa_thuoc = toa_thuoc;
    }
}