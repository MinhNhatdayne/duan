package com.example.nhakhoaapp.models;

public class Img {
    private String _id;          // MongoDB tự tạo
    private String link;         // Link ảnh
    private String id_nhan_vien; // ObjectId dạng String
    private String id_benh_nhan; // ObjectId dạng String
    private String createdAt;    // timestamps
    private String updatedAt;

    public Img() {
    }

    public Img(String link, String id_nhan_vien, String id_benh_nhan) {
        this.link = link;
        this.id_nhan_vien = id_nhan_vien;
        this.id_benh_nhan = id_benh_nhan;
    }

    // ====== GETTERS & SETTERS ======

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getId_nhan_vien() {
        return id_nhan_vien;
    }

    public void setId_nhan_vien(String id_nhan_vien) {
        this.id_nhan_vien = id_nhan_vien;
    }

    public String getId_benh_nhan() {
        return id_benh_nhan;
    }

    public void setId_benh_nhan(String id_benh_nhan) {
        this.id_benh_nhan = id_benh_nhan;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}