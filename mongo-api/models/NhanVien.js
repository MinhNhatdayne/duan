import mongoose from "mongoose";

const NhanVienSchema = new mongoose.Schema(
  {
    ho_ten: { type: String, required: true, trim: true },
    chuc_vu: { type: String, required: true }, // Bác sĩ, Quản lý, Lễ tân
    ngay_sinh: { type: Date, required: true },
    dia_chi: { type: String },
    so_dien_thoai: { type: String },
    email: { type: String, unique: true, sparse: true },
    luong: { type: Number, default: 0 },
    password: { type: String, required: true },
  },
  { timestamps: true, collection: "nhanvien" }
);

export default mongoose.model("NhanVien", NhanVienSchema);
