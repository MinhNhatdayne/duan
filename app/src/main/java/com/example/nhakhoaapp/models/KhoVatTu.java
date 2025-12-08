package com.example.nhakhoaapp.models;

public class KhoVatTu {
    private ObjectIdRef _id;
    private String ma_vt;
    private int so_luong_ton;
    private int gia_ban;
    private String loai;

    // Constructors
    public KhoVatTu() {}

    public KhoVatTu(String ma_vt, int so_luong_ton, int gia_ban, String loai) {
        this.ma_vt = ma_vt;
        this.so_luong_ton = so_luong_ton;
        this.gia_ban = gia_ban;
        this.loai = loai;
    }

    // Getters and Setters
    public ObjectIdRef get_id() { return _id; }
    public void set_id(ObjectIdRef _id) { this._id = _id; }
    public String getMa_vt() { return ma_vt; }
    public void setMa_vt(String ma_vt) { this.ma_vt = ma_vt; }
    public int getSo_luong_ton() { return so_luong_ton; }
    public void setSo_luong_ton(int so_luong_ton) { this.so_luong_ton = so_luong_ton; }
    public int getGia_ban() { return gia_ban; }
    public void setGia_ban(int gia_ban) { this.gia_ban = gia_ban; }
    public String getLoai() { return loai; }
    public void setLoai(String loai) { this.loai = loai; }
}