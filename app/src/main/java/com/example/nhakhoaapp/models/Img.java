package com.example.nhakhoaapp.models;

public class Img {
    private ObjectIdRef _id;
    private String link;
    private ObjectIdRef id_nhan_vien;
    private ObjectIdRef id_benh_nhan;
    private String createdAt;
    private String updatedAt;

    // Constructors
    public Img() {}

    public Img(String link, ObjectIdRef id_nhan_vien, ObjectIdRef id_benh_nhan) {
        this.link = link;
        this.id_nhan_vien = id_nhan_vien;
        this.id_benh_nhan = id_benh_nhan;
    }

    // Getters and Setters
    public ObjectIdRef get_id() { return _id; }
    public void set_id(ObjectIdRef _id) { this._id = _id; }
    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }
    public ObjectIdRef getId_nhan_vien() { return id_nhan_vien; }
    public void setId_nhan_vien(ObjectIdRef id_nhan_vien) { this.id_nhan_vien = id_nhan_vien; }
    public ObjectIdRef getId_benh_nhan() { return id_benh_nhan; }
    public void setId_benh_nhan(ObjectIdRef id_benh_nhan) { this.id_benh_nhan = id_benh_nhan; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}