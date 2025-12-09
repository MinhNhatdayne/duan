package com.example.nhakhoaapp.models.request;

public class RegisterRequest {
    private String ho_ten;
    private String email;
    private String password;
    private String so_dien_thoai;
    private String gioi_tinh;
    private String ngay_sinh; // Định dạng chuỗi yyyy-MM-dd hoặc ISO

    public RegisterRequest(String ho_ten, String email, String password, String so_dien_thoai, String gioi_tinh, String ngay_sinh) {
        this.ho_ten = ho_ten;
        this.email = email;
        this.password = password;
        this.so_dien_thoai = so_dien_thoai;
        this.gioi_tinh = gioi_tinh;
        this.ngay_sinh = ngay_sinh;
    }
}