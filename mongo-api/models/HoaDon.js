import mongoose from "mongoose";

const HoaDonSchema = new mongoose.Schema(
  {
    id_benh_nhan: {
      type: mongoose.Schema.Types.ObjectId,
      ref: "BenhNhan",
      required: true,
    },
    id_nhan_vien_lap: {
      type: mongoose.Schema.Types.ObjectId,
      ref: "NhanVien",
      required: true,
    },
    ngay_lap: { type: Date, default: Date.now },
    tong_tien: { type: Number, default: 0 },
    trang_thai_thanh_toan: { type: String, default: "Chưa thanh toán" },
    phuong_thuc_thanh_toan: { type: String },
  },
  { timestamps: true, collection: "hoadon" }
);

export default mongoose.model("HoaDon", HoaDonSchema);
