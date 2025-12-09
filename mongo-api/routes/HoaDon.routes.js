import HoaDon from "../models/HoaDon.js";
import createCrudRouter from "./baseCrud.js";

const router = createCrudRouter(HoaDon, ["id_benh_nhan", "id_nhan_vien_lap"]);
export default router;
