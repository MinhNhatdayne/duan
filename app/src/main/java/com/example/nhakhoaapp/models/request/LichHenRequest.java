package com.example.nhakhoaapp.models.request;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class LichHenRequest implements Serializable {

    // [QUAN TRỌNG] Phải có @SerializedName khớp y hệt database MongoDB
    @SerializedName("id_benh_nhan")
    private String idBenhNhan;

    @SerializedName("id_bac_si")
    private String idBacSi;

    @SerializedName("thoi_gian_hen")
    private String thoiGianHen;

    @SerializedName("ly_do_kham")
    private String lyDoKham;

    @SerializedName("trang_thai")
    private String trangThai;

    // Constructor rỗng
    public LichHenRequest() {}

    // Getters & Setters
    public String getIdBenhNhan() { return idBenhNhan; }
    public void setIdBenhNhan(String idBenhNhan) { this.idBenhNhan = idBenhNhan; }

    public String getIdBacSi() { return idBacSi; }
    public void setIdBacSi(String idBacSi) { this.idBacSi = idBacSi; }

    public String getThoiGianHen() { return thoiGianHen; }
    public void setThoiGianHen(String thoiGianHen) { this.thoiGianHen = thoiGianHen; }

    public String getLyDoKham() { return lyDoKham; }
    public void setLyDoKham(String lyDoKham) { this.lyDoKham = lyDoKham; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}