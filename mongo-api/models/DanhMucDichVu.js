import mongoose from "mongoose";

const DanhMucDichVuSchema = new mongoose.Schema(
  {
    ten_dich_vu: { type: String, required: true },
    loai_dich_vu: { type: String, required: true }, // 'CLS','TTLS','Phí khám'
    gia_co_ban: { type: Number, required: true },
    don_vi: { type: String },
  },
  { timestamps: true, collection: "danhmucdichvu" }
);

export default mongoose.model("DanhMucDichVu", DanhMucDichVuSchema);
