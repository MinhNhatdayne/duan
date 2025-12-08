package com.example.nhakhoaapp.models;

import java.util.Date;

public class NhanVien {
    private ObjectIdRef _id;
    private String ho_ten;
    private String chuc_vu;
    private Date ngay_sinh;
    private String dia_chi;
    private String so_dien_thoai; // Sửa từ sdt
    private String email;
    private double luong; // Sửa từ String
    private String password;

    // Constructors
    public NhanVien() {}

    public NhanVien(String ho_ten, String chuc_vu, Date ngay_sinh, String dia_chi, String so_dien_thoai, String email, double luong, String password) {
        this.ho_ten = ho_ten;
        this.chuc_vu = chuc_vu;
        this.ngay_sinh = ngay_sinh;
        this.dia_chi = dia_chi;
        this.so_dien_thoai = so_dien_thoai;
        this.email = email;
        this.luong = luong;
        this.password = password;
    }

    // Getters and Setters
    public ObjectIdRef get_id() { return _id; }
    public void set_id(ObjectIdRef _id) { this._id = _id; }
    public String getHo_ten() { return ho_ten; }
    public void setHo_ten(String ho_ten) { this.ho_ten = ho_ten; }
    public String getChuc_vu() { return chuc_vu; }
    public void setChuc_vu(String chuc_vu) { this.chuc_vu = chuc_vu; }
    public Date getNgay_sinh() { return ngay_sinh; }
    public void setNgay_sinh(Date ngay_sinh) { this.ngay_sinh = ngay_sinh; }
    public String getDia_chi() { return dia_chi; }
    public void setDia_chi(String dia_chi) { this.dia_chi = dia_chi; }
    public String getSo_dien_thoai() { return so_dien_thoai; }
    public void setSo_dien_thoai(String so_dien_thoai) { this.so_dien_thoai = so_dien_thoai; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public double getLuong() { return luong; }
    public void setLuong(double luong) { this.luong = luong; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}