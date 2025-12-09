package com.example.nhakhoaapp.models;

import com.google.gson.annotations.SerializedName;

public class DanhMucDichVu {
    @SerializedName("-id")
    private int id_dich_vu;
    @SerializedName("ten_dich_vu")
    private String ten_dich_vu;
    @SerializedName("loai_dich_vu")
    private String loai_dich_vu;
    @SerializedName("gia_co_ban")
    private String gia_co_ban;
    @SerializedName("don_vi")
    private String don_vi;
    //model để tạm tạm chưa chỉnh

    public DanhMucDichVu(int id_dich_vu, String ten_dich_vu, String loai_dich_vu, String gia_co_ban, String don_vi) {
        this.id_dich_vu = id_dich_vu;
        this.ten_dich_vu = ten_dich_vu;
        this.loai_dich_vu = loai_dich_vu;
        this.gia_co_ban = gia_co_ban;
        this.don_vi = don_vi;
    }
}