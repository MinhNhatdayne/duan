import ChiTietToaThuoc from "../models/ChiTietToaThuoc.js";
import createCrudRouter from "./baseCrud.js";

const router = createCrudRouter(ChiTietToaThuoc, ["id_ho_so"]);
export default router;
