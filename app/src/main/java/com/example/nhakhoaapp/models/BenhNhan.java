package com.example.nhakhoaapp.models;

import java.util.Date;

public class BenhNhan {
    private int id_benhnhan;
    private String ten_bn;
    private Date ngay_sinh;
    private String gioi_tinh;
    private String dia_chi;
    private String sdt;
    private String email;

    public BenhNhan(int id_benhnhan, String ten_bn, Date ngay_sinh, String gioi_tinh, String dia_chi, String sdt, String email) {
        this.id_benhnhan = id_benhnhan;
        this.ten_bn = ten_bn;
        this.ngay_sinh = ngay_sinh;
        this.gioi_tinh = gioi_tinh;
        this.dia_chi = dia_chi;
        this.sdt = sdt;
        this.email = email;
    }
}