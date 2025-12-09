import express from "express";

const createCrudRouter = (Model, populate = []) => {
  const router = express.Router();

  const applyPopulate = (query) => {
    if (!populate) return query;
    if (Array.isArray(populate)) {
      populate.forEach((p) => query.populate(p));
    } else {
      query.populate(populate);
    }
    return query;
  };
  
  // GET all
  router.get("/", async (req, res) => {
    try {
      let q = Model.find().sort({ createdAt: -1 });
      q = applyPopulate(q);
      const items = await q;
      res.json(items);
    } catch (e) {
      res.status(500).json({ message: e.message });
    }
  });

  // GET by id
  router.get("/:id", async (req, res) => {
    try {
      let q = Model.findById(req.params.id);
      q = applyPopulate(q);
      const item = await q;
      if (!item) return res.status(404).json({ message: "Not found" });
      res.json(item);
    } catch (e) {
      res.status(400).json({ message: e.message });
    }
  });

  // POST create
  router.post("/", async (req, res) => {
    try {
      const created = await Model.create(req.body);
      res.status(201).json(created);
    } catch (e) {
      res.status(400).json({ message: e.message });
    }
  });

  // PUT update
  router.put("/:id", async (req, res) => {
    try {
      const updated = await Model.findByIdAndUpdate(req.params.id, req.body, {
        new: true,
      });
      if (!updated) return res.status(404).json({ message: "Not found" });
      res.json(updated);
    } catch (e) {
      res.status(400).json({ message: e.message });
    }
  });

  // DELETE
  router.delete("/:id", async (req, res) => {
    try {
      const deleted = await Model.findByIdAndDelete(req.params.id);
      if (!deleted) return res.status(404).json({ message: "Not found" });
      res.json({ message: "Deleted" });
    } catch (e) {
      res.status(400).json({ message: e.message });
    }
  });

  return router;
};

export default createCrudRouter;
