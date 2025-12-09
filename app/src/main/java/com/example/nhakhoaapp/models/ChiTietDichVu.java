package com.example.nhakhoaapp.models;

import com.google.gson.annotations.SerializedName;

public class ChiTietDichVu {
    @SerializedName("_id")
    private String id;
    @SerializedName("id_ho_so")
    private int id_ho_so;
    @SerializedName("id_dich_vu")
    private int id_dich_vu;
    @SerializedName("so_luong")
    private double so_luong;
    @SerializedName("ket_qua_mo_ta")
    private String ket_qua;
    @SerializedName("thanh_tien")
    private String thanh_tien; // Sử dụng String hoặc double/BigDecimal
    @SerializedName("id_nhan_vien_thuc_hien")
    private String id_nhan_vien_thuc_hien;

    public ChiTietDichVu(String id, int id_ho_so, int id_dich_vu, double so_luong, String ket_qua, String thanh_tien, String id_nhan_vien_thuc_hien) {
        this.id = id;
        this.id_ho_so = id_ho_so;
        this.id_dich_vu = id_dich_vu;
        this.so_luong = so_luong;
        this.ket_qua = ket_qua;
        this.thanh_tien = thanh_tien;
        this.id_nhan_vien_thuc_hien = id_nhan_vien_thuc_hien;
    }
}