package com.example.nhakhoaapp.models;

public class Thuoc {
    private String _id; // ObjectId
    private String ma_vt;
    private String ten_vt;
    private String ma_bhyte;
    private String don_vi;
    private String createdAt; // Timestamp
    private String updatedAt; // Timestamp

    public Thuoc() {}

    public Thuoc(String ma_vt, String ten_vt, String ma_bhyte, String don_vi) {
        this.ma_vt = ma_vt;
        this.ten_vt = ten_vt;
        this.ma_bhyte = ma_bhyte;
        this.don_vi = don_vi;
    }

    // Getters
    public String get_id() { return _id; }
    public String getMa_vt() { return ma_vt; }
    public String getTen_vt() { return ten_vt; }
    public String getMa_bhyte() { return ma_bhyte; }
    public String getDon_vi() { return don_vi; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }

    // Setters
    public void set_id(String _id) { this._id = _id; }
    public void setMa_vt(String ma_vt) { this.ma_vt = ma_vt; }
    public void setTen_vt(String ten_vt) { this.ten_vt = ten_vt; }
    public void setMa_bhyte(String ma_bhyte) { this.ma_bhyte = ma_bhyte; }
    public void setDon_vi(String don_vi) { this.don_vi = don_vi; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}