import ChiTietDichVu from "../models/ChiTietDichVu.js";
import createCrudRouter from "./baseCrud.js";

const router = createCrudRouter(ChiTietDichVu, [
  "id_ho_so",
  "id_dich_vu",
  "id_nhan_vien_thuc_hien",
]);
export default router;
