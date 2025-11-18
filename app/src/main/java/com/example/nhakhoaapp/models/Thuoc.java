package com.example.nhakhoaapp.models;

public class Thuoc {
    // KHÓA CHÍNH (PK)
    private String ma_vt;
    private String ten_vt;
    private String ma_bhyte;
    private String don_vi;

    public Thuoc(String ma_vt, String ten_vt, String ma_bhyte, String don_vi) {
        this.ma_vt = ma_vt;
        this.ten_vt = ten_vt;
        this.ma_bhyte = ma_bhyte;
        this.don_vi = don_vi;
    }
}