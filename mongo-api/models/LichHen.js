import mongoose from "mongoose";

const LichHenSchema = new mongoose.Schema(
  {
    id_benh_nhan: {
      type: mongoose.Schema.Types.ObjectId,
      ref: "BenhNhan",
      required: true,
    },
    id_bac_si: {
      type: mongoose.Schema.Types.ObjectId,
      ref: "NhanVien",
      required: true,
    },
    thoi_gian_hen: { type: Date, required: true },
    ly_do_kham: { type: String },
    trang_thai: { type: String, default: "Chờ khám" },
  },
  { timestamps: true, collection: "lichhen" }
);

export default mongoose.model("LichHen", LichHenSchema);
