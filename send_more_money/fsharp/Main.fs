module Main

type Expr =
    | ConstExpr of int
    | VariableExpr of string
    | AdditionExpr of Expr * Expr
    | MultiplicationExpr of Expr * Expr


[<Struct>]
type Constraint =
    | UniquenessConstraint
    | EqualityConstraint of Expr * Expr

[<Struct>]
type Variable = 
    { Name : string
      Values : int list }

type VarMap = Map<string, int>

let rec evaluate (expr : Expr) (varMap : VarMap) : int =
    match expr with
    | ConstExpr n -> n
    | VariableExpr var -> varMap.[var]
    | AdditionExpr (expr1, expr2) -> (evaluate expr1 varMap) + (evaluate expr2 varMap)
    | MultiplicationExpr (expr1, expr2) -> (evaluate expr1 varMap) * (evaluate expr2 varMap)

let buildNumberExpr (variables : string list) : Expr =
    match variables with
    | [] -> invalidArg "variables" "buildNumberExpr doesn't support empty list"
    | x :: xs ->
        let shiftAndAdd acc var = 
            AdditionExpr (MultiplicationExpr (acc, ConstExpr 10), VariableExpr var)
        List.fold shiftAndAdd (VariableExpr x) xs

let satisfy (c : Constraint) (varMap : VarMap) : bool =
    match c with
    | UniquenessConstraint -> true
    | EqualityConstraint (expr1, expr2) -> (evaluate expr1 varMap) = (evaluate expr2 varMap)

let satisfyAll (cs : Constraint list) (varMap : VarMap) : bool =
    List.forall (fun c -> satisfy c varMap) cs

let createVariable (name : string) (values : int list) : Variable =
    { Name = name; Values = values }

let VARIABLES : Variable list =
    [ createVariable "M" [1 .. 9];
      createVariable "D" [0 .. 9];
      createVariable "E" [0 .. 9];
      createVariable "N" [0 .. 9];
      createVariable "O" [0 .. 9];
      createVariable "R" [0 .. 9];
      createVariable "S" [0 .. 9];
      createVariable "Y" [0 .. 9]]

let CONSTRAINTS : Constraint list =
    [ UniquenessConstraint;
      EqualityConstraint(
          AdditionExpr(
              buildNumberExpr ["S"; "E"; "N"; "D"],
              buildNumberExpr ["M"; "O"; "R"; "E"]),
          buildNumberExpr ["M"; "O"; "N"; "E"; "Y"])]

let mapContainsValue (value : int) (varMap : VarMap) =
    Map.exists (fun _ v -> v = value) varMap

let findAnswer (vars : Variable list) (checkFn : VarMap -> string list) : string list =
    let rec findAnswerRec vars varMap =
        match vars with
        | [] -> checkFn varMap
        | { Name = name; Values = values} :: vs -> 
            List.fold
                (fun acc value ->
                    if mapContainsValue value varMap
                    then acc 
                    else varMap
                         |> Map.add name value 
                         |> findAnswerRec vs 
                         |> List.append acc)
                []
                values
    in findAnswerRec vars Map.empty

let checkAnswer (cs : Constraint list) (varMap : VarMap) : string list =
    [ if satisfyAll cs varMap then yield varMap.ToString() ]

let solve (vars : Variable list) (cs : Constraint list) : string list =
    findAnswer vars (checkAnswer cs)

[<EntryPoint>]
let main argv =
    printfn "Hello from F#"
    let stopWatch = System.Diagnostics.Stopwatch.StartNew()
    solve VARIABLES CONSTRAINTS |> List.iter (printfn "%s")
    stopWatch.Stop()
    printfn "%f" stopWatch.Elapsed.TotalMilliseconds
    printfn "Done"
    0
