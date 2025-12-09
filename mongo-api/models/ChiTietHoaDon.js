import mongoose from "mongoose";

const ChiTietHoaDonSchema = new mongoose.Schema(
  {
    id_hoa_don: {
      type: mongoose.Schema.Types.ObjectId,
      ref: "HoaDon",
      required: true,
    },
    noi_dung: { type: String, required: true },
    so_tien: { type: Number, required: true },
  },
  { timestamps: true, collection: "chitiethoadon" }
);

export default mongoose.model("ChiTietHoaDon", ChiTietHoaDonSchema);
