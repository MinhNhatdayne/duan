package com.example.nhakhoaapp.models;

// KHÔNG CẦN import java.util.Date;
public class LichHen {
    // MongoDB Fields (Response)
    private String _id;
    private String createdAt;
    private String updatedAt;

    // Foreign Keys (String ObjectId) (Request & Response)
    private String id_benh_nhan; // Đặt tên khớp với backend (snake_case)
    private String id_bac_si;    // Đặt tên khớp với backend (snake_case)

    // Custom Fields (Request & Response)
    private String thoi_gian_hen; // String (ISO Date String từ MongoDB)
    private String ly_do_kham;
    private String trang_thai;

    // Fields Dành cho UI (Response/Populate)
    private String ten_benh_nhan;
    private String ten_bac_si; // <--- ĐÃ THÊM: Tên bác sĩ
    private String gio_kham;

    public LichHen() {}

    // Constructor để tạo dữ liệu giả lập/test
    public LichHen(String _id, String id_benh_nhan, String id_bac_si, String thoi_gian_hen, String ly_do_kham, String trang_thai, String ten_benh_nhan, String ten_bac_si, String gio_kham) {
        this._id = _id;
        this.id_benh_nhan = id_benh_nhan;
        this.id_bac_si = id_bac_si;
        this.thoi_gian_hen = thoi_gian_hen;
        this.ly_do_kham = ly_do_kham;
        this.trang_thai = trang_thai;
        this.ten_benh_nhan = ten_benh_nhan;
        this.ten_bac_si = ten_bac_si; // Thêm vào constructor
        this.gio_kham = gio_kham;
    }

    // Getters & Setters
    public String get_id() { return _id; }
    public void set_id(String _id) { this._id = _id; }

    public String getId_benh_nhan() { return id_benh_nhan; }
    public void setId_benh_nhan(String id_benh_nhan) { this.id_benh_nhan = id_benh_nhan; }

    public String getId_bac_si() { return id_bac_si; }
    public void setId_bac_si(String id_bac_si) { this.id_bac_si = id_bac_si; }

    public String getThoi_gian_hen() { return thoi_gian_hen; }
    public void setThoi_gian_hen(String thoi_gian_hen) { this.thoi_gian_hen = thoi_gian_hen; }

    public String getLy_do_kham() { return ly_do_kham; }
    public void setLy_do_kham(String ly_do_kham) { this.ly_do_kham = ly_do_kham; }

    public String getTrang_thai() { return trang_thai; }
    public void setTrang_thai(String trang_thai) { this.trang_thai = trang_thai; }

    public String getTen_benh_nhan() { return ten_benh_nhan; }
    public void setTen_benh_nhan(String ten_benh_nhan) { this.ten_benh_nhan = ten_benh_nhan; }

    // Getter & Setter mới cho tên bác sĩ
    public String getTen_bac_si() { return ten_bac_si; }
    public void setTen_bac_si(String ten_bac_si) { this.ten_bac_si = ten_bac_si; }

    public String getGio_kham() { return gio_kham; }
    public void setGio_kham(String gio_kham) { this.gio_kham = gio_kham; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}