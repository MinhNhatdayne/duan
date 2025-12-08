package com.example.nhakhoaapp.models;

public class ChiTietDichVu {
    private String _id; // ObjectId
    private String id_ho_so; // ObjectId
    private String id_dich_vu; // ObjectId
    private double so_luong; // Number -> double
    private String ket_qua_mo_ta;
    private double thanh_tien; // Number -> double
    private String id_nhan_vien_thuc_hien; // ObjectId
    private String createdAt; // Timestamp
    private String updatedAt; // Timestamp

    public ChiTietDichVu() {}

    public ChiTietDichVu(String id_ho_so, String id_dich_vu, double so_luong, String ket_qua_mo_ta, double thanh_tien, String id_nhan_vien_thuc_hien) {
        this.id_ho_so = id_ho_so;
        this.id_dich_vu = id_dich_vu;
        this.so_luong = so_luong;
        this.ket_qua_mo_ta = ket_qua_mo_ta;
        this.thanh_tien = thanh_tien;
        this.id_nhan_vien_thuc_hien = id_nhan_vien_thuc_hien;
    }

    // Getters
    public String get_id() { return _id; }
    public String getId_ho_so() { return id_ho_so; }
    public String getId_dich_vu() { return id_dich_vu; }
    public double getSo_luong() { return so_luong; }
    public String getKet_qua_mo_ta() { return ket_qua_mo_ta; }
    public double getThanh_tien() { return thanh_tien; }
    public String getId_nhan_vien_thuc_hien() { return id_nhan_vien_thuc_hien; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }

    // Setters
    public void set_id(String _id) { this._id = _id; }
    public void setId_ho_so(String id_ho_so) { this.id_ho_so = id_ho_so; }
    public void setId_dich_vu(String id_dich_vu) { this.id_dich_vu = id_dich_vu; }
    public void setSo_luong(double so_luong) { this.so_luong = so_luong; }
    public void setKet_qua_mo_ta(String ket_qua_mo_ta) { this.ket_qua_mo_ta = ket_qua_mo_ta; }
    public void setThanh_tien(double thanh_tien) { this.thanh_tien = thanh_tien; }
    public void setId_nhan_vien_thuc_hien(String id_nhan_vien_thuc_hien) { this.id_nhan_vien_thuc_hien = id_nhan_vien_thuc_hien; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}