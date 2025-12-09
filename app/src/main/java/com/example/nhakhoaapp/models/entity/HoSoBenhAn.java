package com.example.nhakhoaapp.models.entity;

public class HoSoBenhAn {
    private String _id; // ObjectId
    private String id_benh_nhan; 
    private String id_bac_si_kham; 
    private String ngay_kham; // Date -> String
    private String chan_doan;
    private String ket_qua_xet_nghiem;
    private String img; // toa_thuoc -> img
    private String createdAt; // Timestamp
    private String updatedAt; // Timestamp

    public HoSoBenhAn() {}

    public HoSoBenhAn(String id_benh_nhan, String id_bac_si_kham, String ngay_kham, String chan_doan, String ket_qua_xet_nghiem, String img) {
        this.id_benh_nhan = id_benh_nhan;
        this.id_bac_si_kham = id_bac_si_kham;
        this.ngay_kham = ngay_kham;
        this.chan_doan = chan_doan;
        this.ket_qua_xet_nghiem = ket_qua_xet_nghiem;
        this.img = img;
    }

    // Getters
    public String get_id() { return _id; }
    public String getId_benh_nhan() { return id_benh_nhan; }
    public String getId_bac_si_kham() { return id_bac_si_kham; }
    public String getNgay_kham() { return ngay_kham; }
    public String getChan_doan() { return chan_doan; }
    public String getKet_qua_xet_nghiem() { return ket_qua_xet_nghiem; }
    public String getImg() { return img; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }

    // Setters
    public void set_id(String _id) { this._id = _id; }
    public void setId_benh_nhan(String id_benh_nhan) { this.id_benh_nhan = id_benh_nhan; }
    public void setId_bac_si_kham(String id_bac_si_kham) { this.id_bac_si_kham = id_bac_si_kham; }
    public void setNgay_kham(String ngay_kham) { this.ngay_kham = ngay_kham; }
    public void setChan_doan(String chan_doan) { this.chan_doan = chan_doan; }
    public void setKet_qua_xet_nghiem(String ket_qua_xet_nghiem) { this.ket_qua_xet_nghiem = ket_qua_xet_nghiem; }
    public void setImg(String img) { this.img = img; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}