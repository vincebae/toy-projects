module Main (main) where

import qualified Data.Map as M
import Data.Time.Clock (diffUTCTime, getCurrentTime)
import Lib
  ( Constraint (EqualityConstraint, UniquenessConstraint),
    Expr (AdditionExpr),
    VarMap,
    Variable (Variable, name, values),
    buildNumberExpr,
    satisfyAll,
  )

variables :: [Variable]
variables =
  [ Variable {name = "M", values = [1 .. 9]},
    Variable {name = "D", values = [0 .. 9]},
    Variable {name = "E", values = [0 .. 9]},
    Variable {name = "N", values = [0 .. 9]},
    Variable {name = "O", values = [0 .. 9]},
    Variable {name = "R", values = [0 .. 9]},
    Variable {name = "S", values = [0 .. 9]},
    Variable {name = "Y", values = [0 .. 9]}
  ]

constraints :: [Constraint]
constraints =
  [ UniquenessConstraint,
    EqualityConstraint
      ( AdditionExpr
          (buildNumberExpr ["S", "E", "N", "D"])
          (buildNumberExpr ["M", "O", "R", "E"])
      )
      (buildNumberExpr ["M", "O", "N", "E", "Y"])
  ]

findAnswer :: [Variable] -> (VarMap -> [String]) -> [String]
findAnswer vars checkFn = findAnswer' vars M.empty
  where
    findAnswer' [] varMap = checkFn varMap
    findAnswer' (v : vs) varMap = concatMap (goDeeper vs varMap (name v)) (values v)
    goDeeper restVars varMap varName varValue =
      if varValue `elem` M.elems varMap
        then []
        else findAnswer' restVars (M.insert varName varValue varMap)

getAnswer :: (Foldable t) => t Constraint -> VarMap -> [String]
getAnswer cs varMap = [show varMap | satisfyAll cs varMap]

solve :: (Foldable t) => [Variable] -> t Constraint -> IO ()
solve vars cs = do
  let answers = findAnswer vars (getAnswer cs)
  print answers

main :: IO ()
main = do
  putStrLn "Hello World"
  startTime <- getCurrentTime
  solve variables constraints
  endTime <- getCurrentTime
  putStrLn "Done"
  print $ diffUTCTime endTime startTime
