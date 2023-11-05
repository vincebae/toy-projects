import express, { Request, Response, NextFunction } from 'express';
import cors from 'cors';
import { handleError } from "./utils/error";
import { router as recipesRouter } from "./routers/recipes";

const app = express();

app.use(cors());

app.use((req: Request, res: Response, next: NextFunction) => {
  const { method, path } = req;
  console.log(
    `New request to: ${method} ${path} at ${new Date().toISOString()}`);
  next();
});

app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use('/api/v1/recipes', recipesRouter);
app.use(handleError);

const port = process.env.PORT || 8080;
app.listen(port, () => {
  console.log(`Server is up on port ${port}.`);
});

