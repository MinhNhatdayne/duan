package com.example.nhakhoaapp.models;

public class LichHen {
    private String _id; // ObjectId
    private String id_benh_nhan; 
    private String id_bac_si; 
    private String thoi_gian_hen; // Date -> String
    private String ly_do_kham;
    private String trang_thai;
    private String createdAt; // Timestamp
    private String updatedAt; // Timestamp

    public LichHen() {}

    public LichHen(String id_benh_nhan, String id_bac_si, String thoi_gian_hen, String ly_do_kham, String trang_thai) {
        this.id_benh_nhan = id_benh_nhan;
        this.id_bac_si = id_bac_si;
        this.thoi_gian_hen = thoi_gian_hen;
        this.ly_do_kham = ly_do_kham;
        this.trang_thai = trang_thai;
    }

    // Getters
    public String get_id() { return _id; }
    public String getId_benh_nhan() { return id_benh_nhan; }
    public String getId_bac_si() { return id_bac_si; }
    public String getThoi_gian_hen() { return thoi_gian_hen; }
    public String getLy_do_kham() { return ly_do_kham; }
    public String getTrang_thai() { return trang_thai; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }

    // Setters
    public void set_id(String _id) { this._id = _id; }
    public void setId_benh_nhan(String id_benh_nhan) { this.id_benh_nhan = id_benh_nhan; }
    public void setId_bac_si(String id_bac_si) { this.id_bac_si = id_bac_si; }
    public void setThoi_gian_hen(String thoi_gian_hen) { this.thoi_gian_hen = thoi_gian_hen; }
    public void setLy_do_kham(String ly_do_kham) { this.ly_do_kham = ly_do_kham; }
    public void setTrang_thai(String trang_thai) { this.trang_thai = trang_thai; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}