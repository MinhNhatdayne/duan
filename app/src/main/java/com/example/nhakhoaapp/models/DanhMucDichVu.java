package com.example.nhakhoaapp.models;

public class DanhMucDichVu {
    private ObjectIdRef _id;
    private String ten_dich_vu;
    private String loai_dich_vu;
    private double gia_co_ban;
    private String don_vi;

    // Constructors
    public DanhMucDichVu() {}

    public DanhMucDichVu(String ten_dich_vu, String loai_dich_vu, double gia_co_ban, String don_vi) {
        this.ten_dich_vu = ten_dich_vu;
        this.loai_dich_vu = loai_dich_vu;
        this.gia_co_ban = gia_co_ban;
        this.don_vi = don_vi;
    }

    // Getters and Setters
    public ObjectIdRef get_id() { return _id; }
    public void set_id(ObjectIdRef _id) { this._id = _id; }
    public String getTen_dich_vu() { return ten_dich_vu; }
    public void setTen_dich_vu(String ten_dich_vu) { this.ten_dich_vu = ten_dich_vu; }
    public String getLoai_dich_vu() { return loai_dich_vu; }
    public void setLoai_dich_vu(String loai_dich_vu) { this.loai_dich_vu = loai_dich_vu; }
    public double getGia_co_ban() { return gia_co_ban; }
    public void setGia_co_ban(double gia_co_ban) { this.gia_co_ban = gia_co_ban; }
    public String getDon_vi() { return don_vi; }
    public void setDon_vi(String don_vi) { this.don_vi = don_vi; }
}