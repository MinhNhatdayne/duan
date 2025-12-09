import mongoose from "mongoose";

const ChiTietToaThuocSchema = new mongoose.Schema(
  {
    id_ho_so: {
      type: mongoose.Schema.Types.ObjectId,
      ref: "HoSoBenhAn",
      required: true,
    },
    so_luong: { type: Number, required: true },
    huong_dan_su_dung: { type: String },
    thanh_tien: { type: Number, default: 0 },
  },
  { timestamps: true, collection: "chitiettoathuoc" }
);

export default mongoose.model("ChiTietToaThuoc", ChiTietToaThuocSchema);
