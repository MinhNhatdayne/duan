package com.example.nhakhoaapp.models;

import java.util.Date;

public class HoSoBenhAn {
    private ObjectIdRef _id;
    private ObjectIdRef id_benh_nhan;
    private ObjectIdRef id_bac_si_kham;
    private Date ngay_kham;
    private String chan_doan;
    private String ket_qua_xet_nghiem;
    private String img;

    // Constructors
    public HoSoBenhAn() {}

    public HoSoBenhAn(ObjectIdRef id_benh_nhan, ObjectIdRef id_bac_si_kham, Date ngay_kham, String chan_doan, String ket_qua_xet_nghiem, String img) {
        this.id_benh_nhan = id_benh_nhan;
        this.id_bac_si_kham = id_bac_si_kham;
        this.ngay_kham = ngay_kham;
        this.chan_doan = chan_doan;
        this.ket_qua_xet_nghiem = ket_qua_xet_nghiem;
        this.img = img;
    }

    // Getters and Setters
    public ObjectIdRef get_id() { return _id; }
    public void set_id(ObjectIdRef _id) { this._id = _id; }
    public ObjectIdRef getId_benh_nhan() { return id_benh_nhan; }
    public void setId_benh_nhan(ObjectIdRef id_benh_nhan) { this.id_benh_nhan = id_benh_nhan; }
    public ObjectIdRef getId_bac_si_kham() { return id_bac_si_kham; }
    public void setId_bac_si_kham(ObjectIdRef id_bac_si_kham) { this.id_bac_si_kham = id_bac_si_kham; }
    public Date getNgay_kham() { return ngay_kham; }
    public void setNgay_kham(Date ngay_kham) { this.ngay_kham = ngay_kham; }
    public String getChan_doan() { return chan_doan; }
    public void setChan_doan(String chan_doan) { this.chan_doan = chan_doan; }
    public String getKet_qua_xet_nghiem() { return ket_qua_xet_nghiem; }
    public void setKet_qua_xet_nghiem(String ket_qua_xet_nghiem) { this.ket_qua_xet_nghiem = ket_qua_xet_nghiem; }
    public String getImg() { return img; }
    public void setImg(String img) { this.img = img; }
}