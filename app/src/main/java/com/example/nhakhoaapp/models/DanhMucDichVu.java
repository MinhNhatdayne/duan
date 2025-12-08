package com.example.nhakhoaapp.models;

public class DanhMucDichVu {
    private String _id; // ObjectId
    private String ten_dich_vu;
    private String loai_dich_vu; // chi_tiet -> loai_dich_vu
    private double gia_co_ban; // gia -> gia_co_ban (Number -> double)
    private String don_vi;
    private String createdAt; // Timestamp
    private String updatedAt; // Timestamp

    public DanhMucDichVu() {}

    public DanhMucDichVu(String ten_dich_vu, String loai_dich_vu, double gia_co_ban, String don_vi) {
        this.ten_dich_vu = ten_dich_vu;
        this.loai_dich_vu = loai_dich_vu;
        this.gia_co_ban = gia_co_ban;
        this.don_vi = don_vi;
    }

    // Getters
    public String get_id() { return _id; }
    public String getTen_dich_vu() { return ten_dich_vu; }
    public String getLoai_dich_vu() { return loai_dich_vu; }
    public double getGia_co_ban() { return gia_co_ban; }
    public String getDon_vi() { return don_vi; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }

    // Setters
    public void set_id(String _id) { this._id = _id; }
    public void setTen_dich_vu(String ten_dich_vu) { this.ten_dich_vu = ten_dich_vu; }
    public void setLoai_dich_vu(String loai_dich_vu) { this.loai_dich_vu = loai_dich_vu; }
    public void setGia_co_ban(double gia_co_ban) { this.gia_co_ban = gia_co_ban; }
    public void setDon_vi(String don_vi) { this.don_vi = don_vi; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}