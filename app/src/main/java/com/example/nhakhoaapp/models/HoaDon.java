package com.example.nhakhoaapp.models;

public class HoaDon {
    private int id_hoa_don;
    private int id_benhnhan;
    private int id_nhanvien_lap;
    private String ngay_lap;
    private String tong_tien;
    private String trang_thai_thanh_toan;
    private String phuong_thuc_thanh_toan;

    public HoaDon(int id_hoa_don, int id_benhnhan, int id_nhanvien_lap, String ngay_lap, String tong_tien, String trang_thai_thanh_toan, String phuong_thuc_thanh_toan) {
        this.id_hoa_don = id_hoa_don;
        this.id_benhnhan = id_benhnhan;
        this.id_nhanvien_lap = id_nhanvien_lap;
        this.ngay_lap = ngay_lap;
        this.tong_tien = tong_tien;
        this.trang_thai_thanh_toan = trang_thai_thanh_toan;
        this.phuong_thuc_thanh_toan = phuong_thuc_thanh_toan;
    }
}