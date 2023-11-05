import { Request, Response, NextFunction } from "express";
import * as service from "../services/recipes";
import { CustomError } from "../utils/error";

const getAll = async (req: Request, res: Response, next: NextFunction) => {
  try {
    res.status(200).json({ data: await service.getAll() });
  } catch (error) {
    next(error);
  }
};

const get = async (req: Request, res: Response, next: NextFunction) => {
  try {
    const recipe = await getRecipeOrThrow(req.params.id);
    res.status(200).json({ data: recipe });
  } catch (error) {
    next(error);
  }
};

const save = async (req: Request, res: Response, next: NextFunction) => {
  try {
    const recipe: service.Recipe = req.body;
    res.status(201).json({ data: await service.save(recipe) });
  } catch (error) {
    next(error);
  }
};

const update = async (req: Request, res: Response, next: NextFunction) => {
  try {
    const _ = await getRecipeOrThrow(req.params.id);
    const recipe: service.Recipe = req.body;
    const updated = await service.update(req.params.id, recipe);
    res.json({ data: updated });
  } catch (error) {
    next(error);
  }
}

const remove = async (req: Request, res: Response, next: NextFunction) => {
  try {
    const _ = await getRecipeOrThrow(req.params.id);
    await service.remove(req.params.id);
  } catch (error) {
    next(error);
  }
}

const getRecipeOrThrow = async (id: string) => {
  const recipe = await service.get(id);
  if (recipe === undefined) {
    throw new CustomError(404, "Recipe not found");
  }
  return recipe;
}


export {
  getAll,
  get,
  save,
  update,
  remove
};
