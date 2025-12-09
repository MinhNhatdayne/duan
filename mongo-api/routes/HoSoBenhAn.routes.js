import HoSoBenhAn from "../models/HoSoBenhAn.js";
import createCrudRouter from "./baseCrud.js";

const router = createCrudRouter(HoSoBenhAn, [
  "id_benh_nhan",
  "id_bac_si_kham",
]);
export default router;
