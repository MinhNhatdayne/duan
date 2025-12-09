package com.example.nhakhoaapp.models.entity;

public class BaoHiem {
    private String _id; // ObjectId
    private String id_benh_nhan; // ObjectId
    private String ma_bhyt;
    private String ten_cong_ty;
    private String ngay_het_han; // Date -> String
    private String createdAt; // Timestamp
    private String updatedAt; // Timestamp

    public BaoHiem() {}
    
    public BaoHiem(String id_benh_nhan, String ma_bhyt, String ten_cong_ty, String ngay_het_han) {
        this.id_benh_nhan = id_benh_nhan;
        this.ma_bhyt = ma_bhyt;
        this.ten_cong_ty = ten_cong_ty;
        this.ngay_het_han = ngay_het_han;
    }

    // Getters
    public String get_id() { return _id; }
    public String getId_benh_nhan() { return id_benh_nhan; }
    public String getMa_bhyt() { return ma_bhyt; }
    public String getTen_cong_ty() { return ten_cong_ty; }
    public String getNgay_het_han() { return ngay_het_han; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }

    // Setters
    public void set_id(String _id) { this._id = _id; }
    public void setId_benh_nhan(String id_benh_nhan) { this.id_benh_nhan = id_benh_nhan; }
    public void setMa_bhyt(String ma_bhyt) { this.ma_bhyt = ma_bhyt; }
    public void setTen_cong_ty(String ten_cong_ty) { this.ten_cong_ty = ten_cong_ty; }
    public void setNgay_het_han(String ngay_het_han) { this.ngay_het_han = ngay_het_han; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}