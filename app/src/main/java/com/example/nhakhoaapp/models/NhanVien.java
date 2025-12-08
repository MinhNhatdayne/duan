package com.example.nhakhoaapp.models;

public class NhanVien {
    private String _id; // ObjectId
    private String ho_ten; 
    private String chuc_vu;
    private String ngay_sinh; // Date -> String
    private String dia_chi;
    private String so_dien_thoai; // sdt -> so_dien_thoai
    private String email;
    private String password; // Giả định có, tương tự BenhNhan
    private double luong; // String -> double
    private String createdAt; // Timestamp
    private String updatedAt; // Timestamp

    public NhanVien() {}

    public NhanVien(String ho_ten, String chuc_vu, String ngay_sinh, String dia_chi, String so_dien_thoai, String email, String password, double luong) {
        this.ho_ten = ho_ten;
        this.chuc_vu = chuc_vu;
        this.ngay_sinh = ngay_sinh;
        this.dia_chi = dia_chi;
        this.so_dien_thoai = so_dien_thoai;
        this.email = email;
        this.password = password;
        this.luong = luong;
    }

    // Getters
    public String get_id() { return _id; }
    public String getHo_ten() { return ho_ten; }
    public String getChuc_vu() { return chuc_vu; }
    public String getNgay_sinh() { return ngay_sinh; }
    public String getDia_chi() { return dia_chi; }
    public String getSo_dien_thoai() { return so_dien_thoai; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public double getLuong() { return luong; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }

    // Setters
    public void set_id(String _id) { this._id = _id; }
    public void setHo_ten(String ho_ten) { this.ho_ten = ho_ten; }
    public void setChuc_vu(String chuc_vu) { this.chuc_vu = chuc_vu; }
    public void setNgay_sinh(String ngay_sinh) { this.ngay_sinh = ngay_sinh; }
    public void setDia_chi(String dia_chi) { this.dia_chi = dia_chi; }
    public void setSo_dien_thoai(String so_dien_thoai) { this.so_dien_thoai = so_dien_thoai; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setLuong(double luong) { this.luong = luong; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}