import mongoose from "mongoose";

const ImgSchema = new mongoose.Schema(
  {
    link: { type: String, required: true },
    id_nhan_vien: { type: mongoose.Schema.Types.ObjectId, ref: "NhanVien" },
    id_benh_nhan: { type: mongoose.Schema.Types.ObjectId, ref: "BenhNhan" },
  },
  { timestamps: true, collection: "img" }
);

export default mongoose.model("Img", ImgSchema);
