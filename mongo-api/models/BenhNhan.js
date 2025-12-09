import mongoose from "mongoose";

const BenhNhanSchema = new mongoose.Schema(
  {
    ho_ten: { type: String, required: true, trim: true },
    ngay_sinh: { type: Date, required: true },
    gioi_tinh: { type: String, required: true }, // 'Nam','Nữ','Khác'
    dia_chi: { type: String },
    so_dien_thoai: { type: String },
    email: { type: String, unique: true, sparse: true },
    password: { type: String, required: true },
  },
  { timestamps: true, collection: "benhnhan" }
);

export default mongoose.model("BenhNhan", BenhNhanSchema);
