package com.example.nhakhoaapp.models;

public class ChiTietHoaDon {
    private ObjectIdRef _id;
    private ObjectIdRef id_hoa_don;
    private String noi_dung;
    private double so_tien;

    // Constructors
    public ChiTietHoaDon() {}

    public ChiTietHoaDon(ObjectIdRef id_hoa_don, String noi_dung, double so_tien) {
        this.id_hoa_don = id_hoa_don;
        this.noi_dung = noi_dung;
        this.so_tien = so_tien;
    }

    // Getters and Setters
    public ObjectIdRef get_id() { return _id; }
    public void set_id(ObjectIdRef _id) { this._id = _id; }
    public ObjectIdRef getId_hoa_don() { return id_hoa_don; }
    public void setId_hoa_don(ObjectIdRef id_hoa_don) { this.id_hoa_don = id_hoa_don; }
    public String getNoi_dung() { return noi_dung; }
    public void setNoi_dung(String noi_dung) { this.noi_dung = noi_dung; }
    public double getSo_tien() { return so_tien; }
    public void setSo_tien(double so_tien) { this.so_tien = so_tien; }
}