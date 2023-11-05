import { Request, Response, NextFunction } from "express";

class CustomError extends Error {
  statusCode: number;
  message: string;

  constructor(statusCode: number, message: string) {
    super();
    this.statusCode = statusCode;
    this.message = message;
  }
}

const handleError =
    (err: CustomError, req: Request, res:Response, next: NextFunction) => {
  console.error(err.message);
  if (!err.statusCode) {
    err.statusCode = 500;
  }
  res.status(err.statusCode).json({
    status: "error",
    code: err.statusCode,
    message: err.message,
  });
};

export {
  CustomError,
  handleError,
};

