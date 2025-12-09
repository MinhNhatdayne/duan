import ChiTietHoaDon from "../models/ChiTietHoaDon.js";
import createCrudRouter from "./baseCrud.js";

const router = createCrudRouter(ChiTietHoaDon, ["id_hoa_don"]);
export default router;
