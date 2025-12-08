package com.example.nhakhoaapp.models;

import java.util.Date;

public class HoaDon {
    private ObjectIdRef _id;
    private ObjectIdRef id_benh_nhan;
    private ObjectIdRef id_nhan_vien_lap;
    private Date ngay_lap;
    private double tong_tien;
    private String trang_thai_thanh_toan;
    private String phuong_thuc_thanh_toan;

    // Constructors
    public HoaDon() {}

    public HoaDon(ObjectIdRef id_benh_nhan, ObjectIdRef id_nhan_vien_lap, Date ngay_lap, double tong_tien, String trang_thai_thanh_toan, String phuong_thuc_thanh_toan) {
        this.id_benh_nhan = id_benh_nhan;
        this.id_nhan_vien_lap = id_nhan_vien_lap;
        this.ngay_lap = ngay_lap;
        this.tong_tien = tong_tien;
        this.trang_thai_thanh_toan = trang_thai_thanh_toan;
        this.phuong_thuc_thanh_toan = phuong_thuc_thanh_toan;
    }

    // Getters and Setters
    public ObjectIdRef get_id() { return _id; }
    public void set_id(ObjectIdRef _id) { this._id = _id; }
    public ObjectIdRef getId_benh_nhan() { return id_benh_nhan; }
    public void setId_benh_nhan(ObjectIdRef id_benh_nhan) { this.id_benh_nhan = id_benh_nhan; }
    public ObjectIdRef getId_nhan_vien_lap() { return id_nhan_vien_lap; }
    public void setId_nhan_vien_lap(ObjectIdRef id_nhan_vien_lap) { this.id_nhan_vien_lap = id_nhan_vien_lap; }
    public Date getNgay_lap() { return ngay_lap; }
    public void setNgay_lap(Date ngay_lap) { this.ngay_lap = ngay_lap; }
    public double getTong_tien() { return tong_tien; }
    public void setTong_tien(double tong_tien) { this.tong_tien = tong_tien; }
    public String getTrang_thai_thanh_toan() { return trang_thai_thanh_toan; }
    public void setTrang_thai_thanh_toan(String trang_thai_thanh_toan) { this.trang_thai_thanh_toan = trang_thai_thanh_toan; }
    public String getPhuong_thuc_thanh_toan() { return phuong_thuc_thanh_toan; }
    public void setPhuong_thuc_thanh_toan(String phuong_thuc_thanh_toan) { this.phuong_thuc_thanh_toan = phuong_thuc_thanh_toan; }
}