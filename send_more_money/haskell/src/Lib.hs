module Lib
  ( Constraint (..),
    Expr (..),
    Variable (..),
    VarMap,
    buildNumberExpr,
    evaluate,
    satisfy,
    satisfyAll,
  )
where

import Data.Foldable (Foldable (foldl'))
import Data.Map (Map, (!))

data Variable = Variable
  { name :: String,
    values :: [Int]
  }

data Expr
  = ConstExpr Int
  | VariableExpr String
  | AdditionExpr Expr Expr
  | MultiplicationExpr Expr Expr

evaluate :: Expr -> Map String Int -> Int
evaluate (ConstExpr n) _ = n
evaluate (VariableExpr varName) varMap = varMap ! varName
evaluate (AdditionExpr expr1 expr2) varMap = (evaluate expr1 varMap) + (evaluate expr2 varMap)
evaluate (MultiplicationExpr expr1 expr2) varMap = (evaluate expr1 varMap) * (evaluate expr2 varMap)

buildNumberExpr :: [String] -> Expr
buildNumberExpr [] = error "expression should not be empty"
buildNumberExpr (x : xs) = foldl' shiftAndAdd (VariableExpr x) xs
  where
    shiftAndAdd acc var = AdditionExpr (MultiplicationExpr acc (ConstExpr 10)) (VariableExpr var)

data Constraint
  = UniquenessConstraint
  | EqualityConstraint Expr Expr

type VarMap = Map String Int

satisfy :: Constraint -> VarMap -> Bool
satisfy UniquenessConstraint _ = True
satisfy (EqualityConstraint expr1 expr2) varMap = (evaluate expr1 varMap) == (evaluate expr2 varMap)

satisfyAll :: (Foldable t) => t Constraint -> VarMap -> Bool
satisfyAll constraints varMap = all (`satisfy` varMap) constraints
