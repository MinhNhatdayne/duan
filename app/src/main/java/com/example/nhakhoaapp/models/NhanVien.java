package com.example.nhakhoaapp.models;

import java.util.Date;

public class NhanVien {
    private int id_nhanvien;
    private String ten;
    private String chuc_vu;
    private Date ngay_sinh;
    private String dia_chi;
    private String sdt;
    private String email;
    private String luong;

    public NhanVien(int id_nhanvien, String ten, String chuc_vu, Date ngay_sinh, String dia_chi, String sdt, String email, String luong) {
        this.id_nhanvien = id_nhanvien;
        this.ten = ten;
        this.chuc_vu = chuc_vu;
        this.ngay_sinh = ngay_sinh;
        this.dia_chi = dia_chi;
        this.sdt = sdt;
        this.email = email;
        this.luong = luong;
    }
}