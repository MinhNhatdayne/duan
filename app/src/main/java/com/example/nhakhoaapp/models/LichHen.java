package com.example.nhakhoaapp.models;

import java.util.Date;

public class LichHen {
    private ObjectIdRef _id;
    private ObjectIdRef id_benh_nhan; // Sửa từ id_benhnhan
    private ObjectIdRef id_bac_si; // Sửa từ id_nhanvien_phu_trach
    private Date thoi_gian_hen;
    private String ly_do_kham;
    private String trang_thai;

    // Constructors
    public LichHen() {}

    public LichHen(ObjectIdRef id_benh_nhan, ObjectIdRef id_bac_si, Date thoi_gian_hen, String ly_do_kham, String trang_thai) {
        this.id_benh_nhan = id_benh_nhan;
        this.id_bac_si = id_bac_si;
        this.thoi_gian_hen = thoi_gian_hen;
        this.ly_do_kham = ly_do_kham;
        this.trang_thai = trang_thai;
    }

    // Getters and Setters
    public ObjectIdRef get_id() { return _id; }
    public void set_id(ObjectIdRef _id) { this._id = _id; }
    public ObjectIdRef getId_benh_nhan() { return id_benh_nhan; }
    public void setId_benh_nhan(ObjectIdRef id_benh_nhan) { this.id_benh_nhan = id_benh_nhan; }
    public ObjectIdRef getId_bac_si() { return id_bac_si; }
    public void setId_bac_si(ObjectIdRef id_bac_si) { this.id_bac_si = id_bac_si; }
    public Date getThoi_gian_hen() { return thoi_gian_hen; }
    public void setThoi_gian_hen(Date thoi_gian_hen) { this.thoi_gian_hen = thoi_gian_hen; }
    public String getLy_do_kham() { return ly_do_kham; }
    public void setLy_do_kham(String ly_do_kham) { this.ly_do_kham = ly_do_kham; }
    public String getTrang_thai() { return trang_thai; }
    public void setTrang_thai(String trang_thai) { this.trang_thai = trang_thai; }
}