import mongoose from "mongoose";

const HoSoBenhAnSchema = new mongoose.Schema(
  {
    id_benh_nhan: {
      type: mongoose.Schema.Types.ObjectId,
      ref: "BenhNhan",
      required: true,
    },
    id_bac_si_kham: {
      type: mongoose.Schema.Types.ObjectId,
      ref: "NhanVien",
      required: true,
    },
    ngay_kham: { type: Date, required: true },
    chan_doan: { type: String },
    ket_qua_xet_nghiem: { type: String },
    img: { type: String },
  },
  { timestamps: true, collection: "hosobenhan" }
);

export default mongoose.model("HoSoBenhAn", HoSoBenhAnSchema);
