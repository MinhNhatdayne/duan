package com.example.nhakhoaapp.models;

public class ChiTietToaThuoc {
    private ObjectIdRef _id;
    private ObjectIdRef id_ho_so;
    private double so_luong;
    private String huong_dan_su_dung;
    private double thanh_tien;

    // Constructors
    public ChiTietToaThuoc() {}

    public ChiTietToaThuoc(ObjectIdRef id_ho_so, double so_luong, String huong_dan_su_dung, double thanh_tien) {
        this.id_ho_so = id_ho_so;
        this.so_luong = so_luong;
        this.huong_dan_su_dung = huong_dan_su_dung;
        this.thanh_tien = thanh_tien;
    }

    // Getters and Setters
    public ObjectIdRef get_id() { return _id; }
    public void set_id(ObjectIdRef _id) { this._id = _id; }
    public ObjectIdRef getId_ho_so() { return id_ho_so; }
    public void setId_ho_so(ObjectIdRef id_ho_so) { this.id_ho_so = id_ho_so; }
    public double getSo_luong() { return so_luong; }
    public void setSo_luong(double so_luong) { this.so_luong = so_luong; }
    public String getHuong_dan_su_dung() { return huong_dan_su_dung; }
    public void setHuong_dan_su_dung(String huong_dan_su_dung) { this.huong_dan_su_dung = huong_dan_su_dung; }
    public double getThanh_tien() { return thanh_tien; }
    public void setThanh_tien(double thanh_tien) { this.thanh_tien = thanh_tien; }
}