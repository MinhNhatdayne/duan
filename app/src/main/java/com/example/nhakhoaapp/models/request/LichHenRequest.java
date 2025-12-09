package com.example.nhakhoaapp.models.request;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class LichHenRequest implements Serializable {

    // Giữ nguyên tên biến giống model cũ để quen thuộc
    private String id_benh_nhan;
    private String id_bac_si;
    private String thoi_gian_hen;
    private String ly_do_kham;
    private String trang_thai;

    // Constructor rỗng
    public LichHenRequest() {}

    // Getters & Setters
    public String getId_benh_nhan() { return id_benh_nhan; }
    public void setId_benh_nhan(String id_benh_nhan) { this.id_benh_nhan = id_benh_nhan; }

    public String getId_bac_si() { return id_bac_si; }
    public void setId_bac_si(String id_bac_si) { this.id_bac_si = id_bac_si; }

    public String getThoi_gian_hen() { return thoi_gian_hen; }
    public void setThoi_gian_hen(String thoi_gian_hen) { this.thoi_gian_hen = thoi_gian_hen; }

    public String getLy_do_kham() { return ly_do_kham; }
    public void setLy_do_kham(String ly_do_kham) { this.ly_do_kham = ly_do_kham; }

    public String getTrang_thai() { return trang_thai; }
    public void setTrang_thai(String trang_thai) { this.trang_thai = trang_thai; }
}