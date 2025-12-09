import mongoose from "mongoose";

const ChiTietDichVuSchema = new mongoose.Schema(
  {
    id_ho_so: {
      type: mongoose.Schema.Types.ObjectId,
      ref: "HoSoBenhAn",
      required: true,
    },
    id_dich_vu: {
      type: mongoose.Schema.Types.ObjectId,
      ref: "DanhMucDichVu",
      required: true,
    },
    so_luong: { type: Number, required: true },
    ket_qua_mo_ta: { type: String },
    thanh_tien: { type: Number, default: 0 },
    id_nhan_vien_thuc_hien: {
      type: mongoose.Schema.Types.ObjectId,
      ref: "NhanVien",
    },
  },
  { timestamps: true, collection: "chitietdichvu" }
);

export default mongoose.model("ChiTietDichVu", ChiTietDichVuSchema);
