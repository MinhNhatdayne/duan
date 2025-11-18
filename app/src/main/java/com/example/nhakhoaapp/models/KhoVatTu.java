package com.example.nhakhoaapp.models;

public class KhoVatTu {
    // KHÓA CHÍNH (PK) riêng cho KhoVatTu (tùy chọn)
    private int id_kho_vt;

    // KHÓA NGOẠI (FK) - Liên kết đến Thuoc.ma_vt
    private String ma_vt;
    private int so_luong_ton;
    private int gia_ban;
    private String loai;

    public KhoVatTu(int id_kho_vt, String ma_vt, int so_luong_ton, int gia_ban, String loai) {
        this.id_kho_vt = id_kho_vt;
        this.ma_vt = ma_vt;
        this.so_luong_ton = so_luong_ton;
        this.gia_ban = gia_ban;
        this.loai = loai;
    }
}