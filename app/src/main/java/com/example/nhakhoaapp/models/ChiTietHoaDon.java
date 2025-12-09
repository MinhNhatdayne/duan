package com.example.nhakhoaapp.models;

import com.google.gson.annotations.SerializedName;

public class ChiTietHoaDon {
    @SerializedName("id_hoa_don")
    private int id_hoa_don;
    @SerializedName("id_dich_vu")
    private int id_dich_vu;
    @SerializedName("so_luong")
    private double so_luong;
    @SerializedName("so_tien")
    private String don_gia;

    public ChiTietHoaDon(int id_hoa_don, int id_dich_vu, double so_luong, String don_gia) {
        this.id_hoa_don = id_hoa_don;
        this.id_dich_vu = id_dich_vu;
        this.so_luong = so_luong;
        this.don_gia = don_gia;
    }
}