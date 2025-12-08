package com.example.nhakhoaapp.models;

import java.util.Date;

public class BenhNhan {
    private ObjectIdRef _id;
    private String ho_ten;
    private Date ngay_sinh;
    private String gioi_tinh;
    private String dia_chi;
    private String so_dien_thoai;
    private String email;
    private String password;

    // Constructors
    public BenhNhan() {}

    public BenhNhan(String ho_ten, Date ngay_sinh, String gioi_tinh, String dia_chi,
                    String so_dien_thoai, String email, String password) {
        this.ho_ten = ho_ten;
        this.ngay_sinh = ngay_sinh;
        this.gioi_tinh = gioi_tinh;
        this.dia_chi = dia_chi;
        this.so_dien_thoai = so_dien_thoai;
        this.email = email;
        this.password = password;
    }

    // Getters and Setters
    public ObjectIdRef get_id() { return _id; }
    public void set_id(ObjectIdRef _id) { this._id = _id; }
    public String getHo_ten() { return ho_ten; }
    public void setHo_ten(String ho_ten) { this.ho_ten = ho_ten; }
    public Date getNgay_sinh() { return ngay_sinh; }
    public void setNgay_sinh(Date ngay_sinh) { this.ngay_sinh = ngay_sinh; }
    public String getGioi_tinh() { return gioi_tinh; }
    public void setGioi_tinh(String gioi_tinh) { this.gioi_tinh = gioi_tinh; }
    public String getDia_chi() { return dia_chi; }
    public void setDia_chi(String dia_chi) { this.dia_chi = dia_chi; }
    public String getSo_dien_thoai() { return so_dien_thoai; }
    public void setSo_dien_thoai(String so_dien_thoai) { this.so_dien_thoai = so_dien_thoai; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}