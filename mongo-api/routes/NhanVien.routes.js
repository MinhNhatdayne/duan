import NhanVien from "../models/NhanVien.js";
import createCrudRouter from "./baseCrud.js";

const router = createCrudRouter(NhanVien);
export default router;
