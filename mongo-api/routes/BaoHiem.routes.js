import BaoHiem from "../models/BaoHiem.js";
import createCrudRouter from "./baseCrud.js";

const router = createCrudRouter(BaoHiem, ["id_benh_nhan"]);
export default router;
