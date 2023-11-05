import { promises as fs } from "fs";
import path from "path";

const recipesFileName = "../../src/db/recipes.json";
const recipesFilePath = path.join(__dirname, recipesFileName);

interface Recipe {
  id: number;
  name: string;
  healthLabels: string[];
  cookTimeMinutes: number;
  prepTimeMinutes: number;
  ingredients: string[];
};

const getAll = async (): Promise<Recipe[]> => {
  let data = await fs.readFile(recipesFilePath);
  return JSON.parse(data.toString());
}

const get = async (id: string): Promise<Recipe | undefined> => {
  const recipes = await getAll();
  const idNumber = parseInt(id);
  return recipes.find((recipe => recipe.id === idNumber));
}

const save = async (recipe: Recipe): Promise<Recipe | undefined> => {
  const recipes = await getAll();
  recipe.id = recipes.length + 1;
  recipes.push(recipe);
  await fs.writeFile(recipesFilePath, JSON.stringify(recipes));
  return recipe;
};

const update
    = async (id: string, updated: Recipe): Promise<Recipe | undefined> => {
  const recipes = await getAll();
  updated.id = parseInt(id);
  const updatedRecipes = recipes.map((recipe) => 
    (recipe.id === updated.id) ? updated : recipe);
  await fs.writeFile(recipesFilePath, JSON.stringify(updatedRecipes));
  return updated;
};

const remove = async (id: string) => {
  const recipes = await getAll();
  const idNumber = parseInt(id);
  const updatedRecipes = recipes
     .map((recipe) => (recipe.id === idNumber) ? null : recipe)
     .filter((recipe) => recipe !== null);
  await fs.writeFile(recipesFilePath, JSON.stringify(updatedRecipes));
}

export {
  Recipe,
  getAll,
  get,
  save,
  update,
  remove,
};
