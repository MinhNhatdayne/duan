package com.example.nhakhoaapp.models;

import java.util.Date;

public class BenhNhan {
    private int id_benhnhan;
    private String name;
    private String email;
    private Date ngaysinh;
    private String gioitinh;
    private String diachi;
    private String sodienthoai;
    private String password;

    public BenhNhan(int id_benhnhan, String name, String email, Date ngaysinh, String gioitinh, String diachi, String sodienthoai, String password) {
        this.id_benhnhan = id_benhnhan;
        this.name = name;
        this.email = email;
        this.ngaysinh = ngaysinh;
        this.gioitinh = gioitinh;
        this.diachi = diachi;
        this.sodienthoai = sodienthoai;
        this.password = password;
    }

    public int getId_benhnhan() {
        return id_benhnhan;
    }

    public void setId_benhnhan(int id_benhnhan) {
        this.id_benhnhan = id_benhnhan;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getNgaysinh() {
        return ngaysinh;
    }

    public void setNgaysinh(Date ngaysinh) {
        this.ngaysinh = ngaysinh;
    }

    public String getGioitinh() {
        return gioitinh;
    }

    public void setGioitinh(String gioitinh) {
        this.gioitinh = gioitinh;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getSodienthoai() {
        return sodienthoai;
    }

    public void setSodienthoai(String sodienthoai) {
        this.sodienthoai = sodienthoai;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}