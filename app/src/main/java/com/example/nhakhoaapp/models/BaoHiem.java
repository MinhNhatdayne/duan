package com.example.nhakhoaapp.models;

import java.util.Date;

public class BaoHiem {
    private ObjectIdRef _id;
    private ObjectIdRef id_benh_nhan;
    private String ma_bhyt;
    private String ten_cong_ty;
    private Date ngay_het_han;

    // Constructors
    public BaoHiem() {}

    public BaoHiem(ObjectIdRef id_benh_nhan, String ma_bhyt, String ten_cong_ty, Date ngay_het_han) {
        this.id_benh_nhan = id_benh_nhan;
        this.ma_bhyt = ma_bhyt;
        this.ten_cong_ty = ten_cong_ty;
        this.ngay_het_han = ngay_het_han;
    }

    // Getters and Setters
    public ObjectIdRef get_id() { return _id; }
    public void set_id(ObjectIdRef _id) { this._id = _id; }
    public ObjectIdRef getId_benh_nhan() { return id_benh_nhan; }
    public void setId_benh_nhan(ObjectIdRef id_benh_nhan) { this.id_benh_nhan = id_benh_nhan; }
    public String getMa_bhyt() { return ma_bhyt; }
    public void setMa_bhyt(String ma_bhyt) { this.ma_bhyt = ma_bhyt; }
    public String getTen_cong_ty() { return ten_cong_ty; }
    public void setTen_cong_ty(String ten_cong_ty) { this.ten_cong_ty = ten_cong_ty; }
    public Date getNgay_het_han() { return ngay_het_han; }
    public void setNgay_het_han(Date ngay_het_han) { this.ngay_het_han = ngay_het_han; }
}