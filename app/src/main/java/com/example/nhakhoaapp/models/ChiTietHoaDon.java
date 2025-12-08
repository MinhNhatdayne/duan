package com.example.nhakhoaapp.models;

public class ChiTietHoaDon {
    private String _id; // ObjectId
    private String id_hoa_don; // ObjectId
    private String noi_dung;
    private double so_tien; // Number -> double
    private String createdAt; // Timestamp
    private String updatedAt; // Timestamp

    public ChiTietHoaDon() {}

    public ChiTietHoaDon(String id_hoa_don, String noi_dung, double so_tien) {
        this.id_hoa_don = id_hoa_don;
        this.noi_dung = noi_dung;
        this.so_tien = so_tien;
    }

    // Getters
    public String get_id() { return _id; }
    public String getId_hoa_don() { return id_hoa_don; }
    public String getNoi_dung() { return noi_dung; }
    public double getSo_tien() { return so_tien; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }

    // Setters
    public void set_id(String _id) { this._id = _id; }
    public void setId_hoa_don(String id_hoa_don) { this.id_hoa_don = id_hoa_don; }
    public void setNoi_dung(String noi_dung) { this.noi_dung = noi_dung; }
    public void setSo_tien(double so_tien) { this.so_tien = so_tien; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}