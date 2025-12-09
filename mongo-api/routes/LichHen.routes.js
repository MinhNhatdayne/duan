import LichHen from "../models/LichHen.js";
import createCrudRouter from "./baseCrud.js";

const router = createCrudRouter(LichHen, ["id_benh_nhan", "id_bac_si"]);
export default router;
