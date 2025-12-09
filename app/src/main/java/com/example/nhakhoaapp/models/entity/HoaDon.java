package com.example.nhakhoaapp.models.entity;

public class HoaDon {
    private String _id; // ObjectId
    private String id_benh_nhan; 
    private String id_nhan_vien_lap; 
    private String ngay_lap; // Date -> String
    private double tong_tien; // Number -> double
    private String trang_thai_thanh_toan;
    private String phuong_thuc_thanh_toan;
    private String createdAt; // Timestamp
    private String updatedAt; // Timestamp

    public HoaDon() {}

    public HoaDon(String id_benh_nhan, String id_nhan_vien_lap, String ngay_lap, double tong_tien, String trang_thai_thanh_toan, String phuong_thuc_thanh_toan) {
        this.id_benh_nhan = id_benh_nhan;
        this.id_nhan_vien_lap = id_nhan_vien_lap;
        this.ngay_lap = ngay_lap;
        this.tong_tien = tong_tien;
        this.trang_thai_thanh_toan = trang_thai_thanh_toan;
        this.phuong_thuc_thanh_toan = phuong_thuc_thanh_toan;
    }

    // Getters
    public String get_id() { return _id; }
    public String getId_benh_nhan() { return id_benh_nhan; }
    public String getId_nhan_vien_lap() { return id_nhan_vien_lap; }
    public String getNgay_lap() { return ngay_lap; }
    public double getTong_tien() { return tong_tien; }
    public String getTrang_thai_thanh_toan() { return trang_thai_thanh_toan; }
    public String getPhuong_thuc_thanh_toan() { return phuong_thuc_thanh_toan; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }

    // Setters
    public void set_id(String _id) { this._id = _id; }
    public void setId_benh_nhan(String id_benh_nhan) { this.id_benh_nhan = id_benh_nhan; }
    public void setId_nhan_vien_lap(String id_nhan_vien_lap) { this.id_nhan_vien_lap = id_nhan_vien_lap; }
    public void setNgay_lap(String ngay_lap) { this.ngay_lap = ngay_lap; }
    public void setTong_tien(double tong_tien) { this.tong_tien = tong_tien; }
    public void setTrang_thai_thanh_toan(String trang_thai_thanh_toan) { this.trang_thai_thanh_toan = trang_thai_thanh_toan; }
    public void setPhuong_thuc_thanh_toan(String phuong_thuc_thanh_toan) { this.phuong_thuc_thanh_toan = phuong_thuc_thanh_toan; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}