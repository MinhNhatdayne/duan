//package com.example.nhakhoaapp.models;
//
//import java.util.Date;
//
//public class BenhNhan {
//    private int id_benhnhan;
//    private String ho_ten;
//    private Date ngay_sinh;
//    private String gioi_tinh;
//    private String dia_chi;
//    private String so_dien_thoai;
//    private String email;
//    private String password;
//
//    public BenhNhan(int id_benhnhan, String ho_ten, Date ngay_sinh, String gioi_tinh, String dia_chi, String so_dien_thoai, String email, String password) {
//        this.id_benhnhan = id_benhnhan;
//        this.ho_ten = ho_ten;
//        this.ngay_sinh = ngay_sinh;
//        this.gioi_tinh = gioi_tinh;
//        this.dia_chi = dia_chi;
//        this.so_dien_thoai = so_dien_thoai;
//        this.email = email;
//        this.password = password;
//    }
//
//    public String getHo_ten() {
//        return ho_ten;
//    }
//
//    public void setHo_ten(String ho_ten) {
//        this.ho_ten = ho_ten;
//    }
//
//    public Date getNgay_sinh() {
//        return ngay_sinh;
//    }
//
//    public void setNgay_sinh(Date ngay_sinh) {
//        this.ngay_sinh = ngay_sinh;
//    }
//
//    public String getGioi_tinh() {
//        return gioi_tinh;
//    }
//
//    public void setGioi_tinh(String gioi_tinh) {
//        this.gioi_tinh = gioi_tinh;
//    }
//
//    public String getDia_chi() {
//        return dia_chi;
//    }
//
//    public void setDia_chi(String dia_chi) {
//        this.dia_chi = dia_chi;
//    }
//
//    public String getSo_dien_thoai() {
//        return so_dien_thoai;
//    }
//
//    public void setSo_dien_thoai(String so_dien_thoai) {
//        this.so_dien_thoai = so_dien_thoai;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//}

package com.example.nhakhoaapp.models;

import com.google.gson.annotations.SerializedName;

public class BenhNhan {

    @SerializedName("_id")
    private String id;

    @SerializedName("ho_ten")
    private String ten;

    @SerializedName("ngay_sinh")
    private String ngaySinh;

    @SerializedName("gioi_tinh")
    private String gioiTinh;

    @SerializedName("dia_chi")
    private String diaChi;

    @SerializedName("so_dien_thoai")
    private String soDienThoai;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    public BenhNhan() {
    }

    public BenhNhan(String ten, String ngaySinh, String gioiTinh, String diaChi,
                    String soDienThoai, String email, String password) {
        this.ten = ten;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.diaChi = diaChi;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.password = password;
    }

    // Getter & Setter
    public String getId() {
        return id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
