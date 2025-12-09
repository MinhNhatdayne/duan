import mongoose from "mongoose";

const BaoHiemSchema = new mongoose.Schema(
  {
    id_benh_nhan: {
      type: mongoose.Schema.Types.ObjectId,
      ref: "BenhNhan",
      required: true,
    },
    ma_bhyt: { type: String, required: true },
    ten_cong_ty: { type: String },
    ngay_het_han: { type: Date }, 
  },
  { timestamps: true, collection: "baohiem" }
);

export default mongoose.model("BaoHiem", BaoHiemSchema);
