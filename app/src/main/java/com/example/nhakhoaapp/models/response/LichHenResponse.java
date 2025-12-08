package com.example.nhakhoaapp.models.response;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class LichHenResponse implements Serializable {

    @SerializedName("_id")
    private String id;

    @SerializedName("thoi_gian_hen")
    private String thoiGianHen;

    @SerializedName("ly_do_kham")
    private String lyDoKham;

    @SerializedName("trang_thai")
    private String trangThai;

    @SerializedName("createdAt")
    private String createdAt;

    // [QUAN TRỌNG] Hứng Object thay vì String
    @SerializedName("id_benh_nhan")
    private BenhNhanInfo benhNhan;

    @SerializedName("id_bac_si")
    private BacSiInfo bacSi;

    // ==========================================
    // INNER CLASSES (Để hứng dữ liệu populate)
    // ==========================================
    
    public static class BenhNhanInfo implements Serializable {
        @SerializedName("_id")
        private String id;
        
        @SerializedName("ho_ten")
        private String hoTen;
        
        @SerializedName("so_dien_thoai")
        private String soDienThoai;

        public String getHoTen() { return hoTen; }
        public String getSoDienThoai() { return soDienThoai; }
    }

    public static class BacSiInfo implements Serializable {
        @SerializedName("_id")
        private String id;
        
        @SerializedName("ho_ten")
        private String hoTen;

        public String getHoTen() { return hoTen; }
    }

    // ==========================================
    // GETTERS & HELPER METHODS
    // ==========================================

    public String getId() { return id; }
    public String getThoiGianHen() { return thoiGianHen; }
    public String getLyDoKham() { return lyDoKham; }
    public String getTrangThai() { return trangThai; }
    public String getCreatedAt() { return createdAt; }

    public BenhNhanInfo getBenhNhan() { return benhNhan; }
    public BacSiInfo getBacSi() { return bacSi; }

    // --- Hàm tiện ích để Adapter gọi cho gọn ---
    // Giúp tránh lỗi NullPointerException nếu backend trả về null
    public String getTenBenhNhanDisplay() {
        return benhNhan != null ? benhNhan.getHoTen() : "Không xác định";
    }

    public String getSdtBenhNhanDisplay() {
        return benhNhan != null ? benhNhan.getSoDienThoai() : "";
    }

    public String getTenBacSiDisplay() {
        return bacSi != null ? bacSi.getHoTen() : "Không xác định";
    }
}