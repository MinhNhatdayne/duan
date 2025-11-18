package com.example.nhakhoaapp.models;

import java.util.Date;

public class LichHen {
    private int id_lichhen;
    private int id_benhnhan;
    private int id_nhanvien_phu_trach;
    private Date thoi_gian_hen;
    private String ly_do_kham;
    private String trang_thai;
    // Giả định thêm tên bệnh nhân và giờ khám
    private String ten_benh_nhan; 
    private String gio_kham; 

    public LichHen(int id_lichhen, int id_benhnhan, int id_nhanvien_phu_trach, Date thoi_gian_hen, String ly_do_kham, String trang_thai, String ten_benh_nhan, String gio_kham) {
        this.id_lichhen = id_lichhen;
        this.id_benhnhan = id_benhnhan;
        this.id_nhanvien_phu_trach = id_nhanvien_phu_trach;
        this.thoi_gian_hen = thoi_gian_hen;
        this.ly_do_kham = ly_do_kham;
        this.trang_thai = trang_thai;
        this.ten_benh_nhan = ten_benh_nhan;
        this.gio_kham = gio_kham;
    }

    // Getters
    public int getId_lichhen() { return id_lichhen; }
    public int getId_benhnhan() { return id_benhnhan; }
    public int getId_nhanvien_phu_trach() { return id_nhanvien_phu_trach; }
    public Date getThoi_gian_hen() { return thoi_gian_hen; }
    public String getLy_do_kham() { return ly_do_kham; }
    public String getTrang_thai() { return trang_thai; }
    public String getTen_benh_nhan() { return ten_benh_nhan; }
    public String getGio_kham() { return gio_kham; }

    // Setters (Nếu cần)
    public void setTrang_thai(String trang_thai) { this.trang_thai = trang_thai; }
    // ...
}