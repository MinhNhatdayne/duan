package com.example.nhakhoaapp.models;

public class Thuoc {
    private ObjectIdRef _id;
    private String ma_vt;
    private String ten_vt;
    private String ma_bhyte;
    private String don_vi;

    // Constructors
    public Thuoc() {}

    public Thuoc(String ma_vt, String ten_vt, String ma_bhyte, String don_vi) {
        this.ma_vt = ma_vt;
        this.ten_vt = ten_vt;
        this.ma_bhyte = ma_bhyte;
        this.don_vi = don_vi;
    }

    // Getters and Setters
    public ObjectIdRef get_id() { return _id; }
    public void set_id(ObjectIdRef _id) { this._id = _id; }
    public String getMa_vt() { return ma_vt; }
    public void setMa_vt(String ma_vt) { this.ma_vt = ma_vt; }
    public String getTen_vt() { return ten_vt; }
    public void setTen_vt(String ten_vt) { this.ten_vt = ten_vt; }
    public String getMa_bhyte() { return ma_bhyte; }
    public void setMa_bhyte(String ma_bhyte) { this.ma_bhyte = ma_bhyte; }
    public String getDon_vi() { return don_vi; }
    public void setDon_vi(String don_vi) { this.don_vi = don_vi; }
}