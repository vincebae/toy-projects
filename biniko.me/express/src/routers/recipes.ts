import { Router } from 'express';
import * as controller from "../controllers/recipes";

const router = Router();

router.route("/")
  .get(controller.getAll)
  .post(controller.save);

router.route("/:id")
  .get(controller.get)
  .put(controller.update)
  .delete(controller.remove);

export { router };
