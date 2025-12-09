import express from "express";
import mongoose from "mongoose";
import cors from "cors";
import dotenv from "dotenv";

dotenv.config();

const app = express();
app.use(cors());
app.use(express.json());

// Test route
app.get("/", (req, res) => {
  res.send("Backend is running");
});

// Kết nối MongoDB
mongoose
  .connect(process.env.MONGO_URI)
  .then(() => console.log("✅ Connected to MongoDB Atlas"))
  .catch((err) => console.log("❌ Mongo error:", err));

// IMPORT ROUTES
import benhnhanRoutes from "./routes/BenhNhan.routes.js";
import nhanvienRoutes from "./routes/NhanVien.routes.js";
import imgRoutes from "./routes/Img.routes.js";
import chitiettoathuocRoutes from "./routes/ChiTietToaThuoc.routes.js";
import hosobenhanRoutes from "./routes/HoSoBenhAn.routes.js";
import lichhenRoutes from "./routes/LichHen.routes.js";
import hoadonRoutes from "./routes/HoaDon.routes.js";
import chitiethoadonRoutes from "./routes/ChiTietHoaDon.routes.js";
import BaoHiemRoutes from "./routes/BaoHiem.routes.js";
import DanhMucDichVu from "./routes/DanhMucDichVu.routes.js";
import chiTietDichVu from "./routes/ChiTietDichVu.routes.js";
// GẮN PREFIX CHO API
app.use("/BenhNhan", benhnhanRoutes);   // /BenhNhan, /BenhNhan/:id...
app.use("/NhanVien", nhanvienRoutes);   // /NhanVien
app.use("/Img", imgRoutes);             // /Img`
app.use("/ChiTietToaThuoc", chitiettoathuocRoutes); // /ChiTietToaThuoc
app.use("/HoSoBenhAn", hosobenhanRoutes); // /HoSoBenhAn
app.use("/LichHen", lichhenRoutes);     // /LichHen
app.use("/HoaDon", hoadonRoutes);       // /HoaDon
app.use("/ChiTietHoaDon", chitiethoadonRoutes); // /ChiTietHoaDon
app.use("/BaoHiem", BaoHiemRoutes);     // /BaoHiem
app.use("/DichVu", DanhMucDichVu); // /DanhMucDichVu
app.use("/ChiTietDichVu", chiTietDichVu); // /ChiTietDichVu
const PORT = 3000;
app.listen(PORT, () =>
  console.log(`Server running at http://localhost:${PORT}`)
);
