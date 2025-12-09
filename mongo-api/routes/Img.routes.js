import Img from "../models/Img.js";
import createCrudRouter from "./baseCrud.js";

const router = createCrudRouter(Img, ["id_benh_nhan", "id_nhan_vien"]);
export default router;
