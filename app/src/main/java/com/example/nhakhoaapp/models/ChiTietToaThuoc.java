package com.example.nhakhoaapp.models;

import com.google.gson.annotations.SerializedName;

public class ChiTietToaThuoc {
    @SerializedName("_id")
    private int id_chi_tiet;
    @SerializedName("so_luong")
    private int so_luong;
    @SerializedName("huong_dan_su_dung")
    private String huong_dan_su_dung;
    @SerializedName("thanh_tien")
    private String thanh_tien;

    public ChiTietToaThuoc(int id_chi_tiet, int so_luong, String huong_dan_su_dung, String thanh_tien) {
        this.id_chi_tiet = id_chi_tiet;
        this.so_luong = so_luong;
        this.huong_dan_su_dung = huong_dan_su_dung;
        this.thanh_tien = thanh_tien;
    }
}