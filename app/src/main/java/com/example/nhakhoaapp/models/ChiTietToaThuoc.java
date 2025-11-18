package com.example.nhakhoaapp.models;

public class ChiTietToaThuoc {
    private int id_chi_tiet;
    private String ma_vt;
    private double so_luong;
    private String huong_dan_su_dung;
    private String thanh_tien;

    public ChiTietToaThuoc(int id_chi_tiet, String ma_vt, double so_luong, String huong_dan_su_dung, String thanh_tien) {
        this.id_chi_tiet = id_chi_tiet;
        this.ma_vt = ma_vt;
        this.so_luong = so_luong;
        this.huong_dan_su_dung = huong_dan_su_dung;
        this.thanh_tien = thanh_tien;
    }
}