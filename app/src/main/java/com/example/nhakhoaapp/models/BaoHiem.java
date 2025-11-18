package com.example.nhakhoaapp.models;

import java.util.Date;

public class BaoHiem {
    private int id_bao_hiem;
    private int id_benh_nhan;
    private String ma_bhyt;
    private String ten_cong_ty;
    private Date ngay_het_han;

    public BaoHiem(int id_bao_hiem, int id_benh_nhan, String ma_bhyt, String ten_cong_ty, Date ngay_het_han) {
        this.id_bao_hiem = id_bao_hiem;
        this.id_benh_nhan = id_benh_nhan;
        this.ma_bhyt = ma_bhyt;
        this.ten_cong_ty = ten_cong_ty;
        this.ngay_het_han = ngay_het_han;
    }
}