package com.example.nhakhoaapp.models;

public class ChiTietDichVu {
    private int id_ho_so;
    private int id_dich_vu;
    private double so_luong;
    private String ket_qua;
    private String thanh_tien; // Sử dụng String hoặc double/BigDecimal

    public ChiTietDichVu(int id_ho_so, int id_dich_vu, double so_luong, String ket_qua, String thanh_tien) {
        this.id_ho_so = id_ho_so;
        this.id_dich_vu = id_dich_vu;
        this.so_luong = so_luong;
        this.ket_qua = ket_qua;
        this.thanh_tien = thanh_tien;
    }
}