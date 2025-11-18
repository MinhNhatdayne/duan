package com.example.nhakhoaapp.models;

public class DichVu {
    private int id_dich_vu;
    private String ten_dich_vu;
    private String chi_tiet;
    private String don_vi;
    private String gia;

    public DichVu(int id_dich_vu, String ten_dich_vu, String chi_tiet, String don_vi, String gia) {
        this.id_dich_vu = id_dich_vu;
        this.ten_dich_vu = ten_dich_vu;
        this.chi_tiet = chi_tiet;
        this.don_vi = don_vi;
        this.gia = gia;
    }
}