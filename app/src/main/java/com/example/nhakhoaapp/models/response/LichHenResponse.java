package com.example.nhakhoaapp.models.response;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class LichHenResponse implements Serializable {

    @SerializedName("_id")
    private String _id;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("updatedAt")
    private String updatedAt;

    @SerializedName("thoi_gian_hen")
    private String thoi_gian_hen;

    @SerializedName("ly_do_kham")
    private String ly_do_kham;

    @SerializedName("trang_thai")
    private String trang_thai;

    // [QUAN TRỌNG] Sử dụng JsonElement để hứng cả Object (đã populate) và String (ID thô)
    // Giúp App không bị Crash khi server trả về ID thay vì Object
    @SerializedName("id_benh_nhan")
    private JsonElement id_benh_nhan;

    @SerializedName("id_bac_si")
    private JsonElement id_bac_si;

    // ==========================================
    // INNER CLASSES (Dùng để parse thủ công từ JsonElement)
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
    public String get_id() { return _id; }
    public String getThoi_gian_hen() { return thoi_gian_hen; }
    public String getLy_do_kham() { return ly_do_kham; }
    public String getTrang_thai() { return trang_thai; }
    public String getCreatedAt() { return createdAt; }

    // ==========================================
    // HELPER METHODS (Xử lý thông minh để lấy Tên)
    // ==========================================

    /**
     * Lấy tên Bệnh nhân an toàn.
     * - Nếu là Object: Trả về tên thật.
     * - Nếu là String ID: Trả về chuỗi ID rút gọn.
     * - Nếu null: Trả về "Không xác định".
     */
    public String getTen_benh_nhan() {
        try {
            if (id_benh_nhan != null && id_benh_nhan.isJsonObject()) {
                // Parse từ JsonElement sang Object BenhNhanInfo
                BenhNhanInfo bn = new Gson().fromJson(id_benh_nhan, BenhNhanInfo.class);
                return bn.getHo_ten();
            } else if (id_benh_nhan != null && id_benh_nhan.isJsonPrimitive()) {
                // Trường hợp API trả về String ID (chưa populate)
                return "ID: " + id_benh_nhan.getAsString().substring(0, 5) + "...";
            }
        } catch (Exception e) {
            return "Lỗi dữ liệu";
        }
        return "Không xác định";
    }

    /**
     * Lấy SĐT Bệnh nhân an toàn
     */
    public String getSdt_benh_nhan() {
        try {
            if (id_benh_nhan != null && id_benh_nhan.isJsonObject()) {
                BenhNhanInfo bn = new Gson().fromJson(id_benh_nhan, BenhNhanInfo.class);
                return bn.getSo_dien_thoai();
            }
        } catch (Exception e) { return ""; }
        return "";
    }

    /**
     * Lấy tên Bác sĩ an toàn
     */
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
}