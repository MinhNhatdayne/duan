package com.example.nhakhoaapp.models.response;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LichHenResponse implements Serializable {

    @SerializedName("_id")
    private String _id;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("updatedAt")
    private String updatedAt;

    // Giả sử định dạng từ server là ISO (ví dụ: "2025-12-20T09:30:00.000Z") hoặc "yyyy-MM-dd HH:mm"
    @SerializedName("thoi_gian_hen")
    private String thoi_gian_hen;

    @SerializedName("ly_do_kham")
    private String ly_do_kham;

    @SerializedName("trang_thai")
    private String trang_thai;

    // [QUAN TRỌNG] Sử dụng JsonElement để hứng cả Object (đã populate) và String (ID thô)
    @SerializedName("id_benh_nhan")
    private JsonElement id_benh_nhan;

    @SerializedName("id_bac_si")
    private JsonElement id_bac_si;

    // ==========================================
    // INNER CLASSES
    // ==========================================
    public static class BenhNhanInfo implements Serializable {
        @SerializedName("_id") private String _id;
        @SerializedName("ho_ten") private String ho_ten;
        @SerializedName("so_dien_thoai") private String so_dien_thoai;

        public String getHo_ten() { return ho_ten; }
        public String getSo_dien_thoai() { return so_dien_thoai; }
    }

    public static class BacSiInfo implements Serializable {
        @SerializedName("_id") private String _id;
        @SerializedName("ho_ten") private String ho_ten;

        public String getHo_ten() { return ho_ten; }
    }

    // ==========================================
    // GETTERS CƠ BẢN
    // ==========================================
    public String getId() { return _id; } // Alias tiện lợi
    public String get_id() { return _id; }
    public String getThoi_gian_hen() { return thoi_gian_hen; }
    public String getLy_do_kham() { return ly_do_kham; }
    public String getTrang_thai() { return trang_thai; }
    public String getCreatedAt() { return createdAt; }


    // ==========================================
    // HELPER METHODS (Xử lý Ngày/Giờ hiển thị)
    // ==========================================

    /**
     * Tách lấy NGÀY và format lại thành dd/MM/yyyy
     */
    public String getNgayHenFormatted() {
        if (thoi_gian_hen == null || thoi_gian_hen.isEmpty()) return "";
        try {
            // Trường hợp 1: Chuỗi có chứa chữ T (ISO format: 2023-12-20T09:00...)
            if (thoi_gian_hen.contains("T")) {
                String rawDate = thoi_gian_hen.split("T")[0]; // Lấy 2023-12-20
                return formatToVN(rawDate);
            }
            // Trường hợp 2: Chuỗi cách nhau bằng khoảng trắng (2023-12-20 09:00)
            else if (thoi_gian_hen.contains(" ")) {
                String rawDate = thoi_gian_hen.split(" ")[0];
                return formatToVN(rawDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return thoi_gian_hen; // Trả về gốc nếu lỗi
    }

    /**
     * Tách lấy GIỜ (HH:mm)
     */
    public String getGioHenFormatted() {
        if (thoi_gian_hen == null || thoi_gian_hen.isEmpty()) return "";
        try {
            String timePart = "";
            if (thoi_gian_hen.contains("T")) {
                timePart = thoi_gian_hen.split("T")[1]; // Lấy phần sau T
            } else if (thoi_gian_hen.contains(" ")) {
                timePart = thoi_gian_hen.split(" ")[1]; // Lấy phần sau dấu cách
            }

            // Cắt lấy 5 ký tự đầu (HH:mm) bỏ phần giây/timezone nếu có
            if (timePart.length() >= 5) {
                return timePart.substring(0, 5);
            }
            return timePart;
        } catch (Exception e) {
            return "";
        }
    }

    // Hàm phụ trợ đổi yyyy-MM-dd sang dd-MM-yyyy
    private String formatToVN(String yyyyMMdd) {
        try {
            String[] parts = yyyyMMdd.split("-");
            if (parts.length == 3) {
                return parts[2] + "/" + parts[1] + "/" + parts[0];
            }
        } catch (Exception e) { }
        return yyyyMMdd;
    }

    // ==========================================
    // HELPER METHODS (Lấy thông tin liên kết hiển thị)
    // ==========================================

    public String getTen_benh_nhan() {
        try {
            if (id_benh_nhan != null && id_benh_nhan.isJsonObject()) {
                BenhNhanInfo bn = new Gson().fromJson(id_benh_nhan, BenhNhanInfo.class);
                return bn.getHo_ten();
            } else if (id_benh_nhan != null && id_benh_nhan.isJsonPrimitive()) {
                return "ID: " + id_benh_nhan.getAsString().substring(0, 5) + "...";
            }
        } catch (Exception e) { return "Lỗi dữ liệu"; }
        return "Không xác định";
    }

    public String getSdt_benh_nhan() {
        try {
            if (id_benh_nhan != null && id_benh_nhan.isJsonObject()) {
                BenhNhanInfo bn = new Gson().fromJson(id_benh_nhan, BenhNhanInfo.class);
                return bn.getSo_dien_thoai();
            }
        } catch (Exception e) { return ""; }
        return "";
    }

    public String getTen_bac_si() {
        try {
            if (id_bac_si != null && id_bac_si.isJsonObject()) {
                BacSiInfo bs = new Gson().fromJson(id_bac_si, BacSiInfo.class);
                return bs.getHo_ten();
            } else if (id_bac_si != null && id_bac_si.isJsonPrimitive()) {
                return "BS ID: " + id_bac_si.getAsString().substring(0, 5) + "...";
            }
        } catch (Exception e) { return "Lỗi dữ liệu"; }
        return "Chưa phân công";
    }

    // ==========================================
    // [QUAN TRỌNG] HELPER METHODS CHO UPDATE/PUT
    // Lấy ID gốc (String) để gửi lên Server
    // ==========================================

    public String getRawBenhNhanId() {
        if (id_benh_nhan == null) return null;
        try {
            // Nếu là Object (đã populate), lấy field "_id" bên trong
            if (id_benh_nhan.isJsonObject()) {
                JsonElement idElement = id_benh_nhan.getAsJsonObject().get("_id");
                return idElement != null ? idElement.getAsString() : null;
            }
            // Nếu là Primitive (String ID), lấy trực tiếp
            else if (id_benh_nhan.isJsonPrimitive()) {
                return id_benh_nhan.getAsString();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public String getRawBacSiId() {
        if (id_bac_si == null) return null;
        try {
            if (id_bac_si.isJsonObject()) {
                JsonElement idElement = id_bac_si.getAsJsonObject().get("_id");
                return idElement != null ? idElement.getAsString() : null;
            }
            else if (id_bac_si.isJsonPrimitive()) {
                return id_bac_si.getAsString();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}