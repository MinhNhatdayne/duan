package com.example.nhakhoaapp.models;

public class ChiTietDichVu {
    private ObjectIdRef _id;
    private ObjectIdRef id_ho_so;
    private ObjectIdRef id_dich_vu;
    private double so_luong;
    private String ket_qua_mo_ta;
    private double thanh_tien;
    private ObjectIdRef id_nhan_vien_thuc_hien;

    // Constructors
    public ChiTietDichVu() {}

    public ChiTietDichVu(ObjectIdRef id_ho_so, ObjectIdRef id_dich_vu, double so_luong, String ket_qua_mo_ta, double thanh_tien, ObjectIdRef id_nhan_vien_thuc_hien) {
        this.id_ho_so = id_ho_so;
        this.id_dich_vu = id_dich_vu;
        this.so_luong = so_luong;
        this.ket_qua_mo_ta = ket_qua_mo_ta;
        this.thanh_tien = thanh_tien;
        this.id_nhan_vien_thuc_hien = id_nhan_vien_thuc_hien;
    }

    // Getters and Setters
    public ObjectIdRef get_id() { return _id; }
    public void set_id(ObjectIdRef _id) { this._id = _id; }
    public ObjectIdRef getId_ho_so() { return id_ho_so; }
    public void setId_ho_so(ObjectIdRef id_ho_so) { this.id_ho_so = id_ho_so; }
    public ObjectIdRef getId_dich_vu() { return id_dich_vu; }
    public void setId_dich_vu(ObjectIdRef id_dich_vu) { this.id_dich_vu = id_dich_vu; }
    public double getSo_luong() { return so_luong; }
    public void setSo_luong(double so_luong) { this.so_luong = so_luong; }
    public String getKet_qua_mo_ta() { return ket_qua_mo_ta; }
    public void setKet_qua_mo_ta(String ket_qua_mo_ta) { this.ket_qua_mo_ta = ket_qua_mo_ta; }
    public double getThanh_tien() { return thanh_tien; }
    public void setThanh_tien(double thanh_tien) { this.thanh_tien = thanh_tien; }
    public ObjectIdRef getId_nhan_vien_thuc_hien() { return id_nhan_vien_thuc_hien; }
    public void setId_nhan_vien_thuc_hien(ObjectIdRef id_nhan_vien_thuc_hien) { this.id_nhan_vien_thuc_hien = id_nhan_vien_thuc_hien; }
}