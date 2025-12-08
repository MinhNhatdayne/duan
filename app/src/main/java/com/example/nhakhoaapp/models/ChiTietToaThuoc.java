package com.example.nhakhoaapp.models;

public class ChiTietToaThuoc {
    private String _id; // ObjectId
    private String id_ho_so; // ObjectId
    private double so_luong; // Number -> double
    private String huong_dan_su_dung;
    private double thanh_tien; // Number -> double
    private String createdAt; // Timestamp
    private String updatedAt; // Timestamp

    public ChiTietToaThuoc() {}
    
    public ChiTietToaThuoc(String id_ho_so, double so_luong, String huong_dan_su_dung, double thanh_tien) {
        this.id_ho_so = id_ho_so;
        this.so_luong = so_luong;
        this.huong_dan_su_dung = huong_dan_su_dung;
        this.thanh_tien = thanh_tien;
    }

    // Getters
    public String get_id() { return _id; }
    public String getId_ho_so() { return id_ho_so; }
    public double getSo_luong() { return so_luong; }
    public String getHuong_dan_su_dung() { return huong_dan_su_dung; }
    public double getThanh_tien() { return thanh_tien; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }

    // Setters
    public void set_id(String _id) { this._id = _id; }
    public void setId_ho_so(String id_ho_so) { this.id_ho_so = id_ho_so; }
    public void setSo_luong(double so_luong) { this.so_luong = so_luong; }
    public void setHuong_dan_su_dung(String huong_dan_su_dung) { this.huong_dan_su_dung = huong_dan_su_dung; }
    public void setThanh_tien(double thanh_tien) { this.thanh_tien = thanh_tien; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}