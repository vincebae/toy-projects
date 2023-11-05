type expr =
  | ConstExpr of int
  | VariableExpr of string
  | AdditionExpr of expr * expr
  | MultiplicationExpr of expr * expr

type rule = UniquenessRule | EqualityRule of expr * expr
type variable = { name : string; values : int list }

let rec evaluate expr varMap =
  match expr with
  | ConstExpr n -> n
  | VariableExpr var -> List.assoc var varMap
  | AdditionExpr (expr1, expr2) -> evaluate expr1 varMap + evaluate expr2 varMap
  | MultiplicationExpr (expr1, expr2) ->
      evaluate expr1 varMap * evaluate expr2 varMap

let buildNumberExpr variables =
  match variables with
  | [] -> invalid_arg "buildNumberExpr doesn't support empty list"
  | x :: xs ->
      let shiftAndAdd acc var =
        AdditionExpr (MultiplicationExpr (acc, ConstExpr 10), VariableExpr var)
      in
      List.fold_left shiftAndAdd (VariableExpr x) xs

let satisfy rule varMap =
  match rule with
  | UniquenessRule -> true
  | EqualityRule (expr1, expr2) -> evaluate expr1 varMap = evaluate expr2 varMap

let satisfyAll rules varMap = List.for_all (fun r -> satisfy r varMap) rules
let createVariable name values = { name; values }

let range startInclusive endInclusive =
  List.init (endInclusive - startInclusive + 1) (fun x -> x + startInclusive)

let variables =
  [
    createVariable "M" (range 1 9);
    createVariable "D" (range 0 9);
    createVariable "E" (range 0 9);
    createVariable "N" (range 0 9);
    createVariable "O" (range 0 9);
    createVariable "R" (range 0 9);
    createVariable "S" (range 0 9);
    createVariable "Y" (range 0 9);
  ]

let constraints =
  [
    UniquenessRule;
    EqualityRule
      ( AdditionExpr
          ( buildNumberExpr [ "S"; "E"; "N"; "D" ],
            buildNumberExpr [ "M"; "O"; "R"; "E" ] ),
        buildNumberExpr [ "M"; "O"; "N"; "E"; "Y" ] );
  ]

let varMapToString varMap =
  let pairToString pair =
    match pair with a, b -> Printf.sprintf "%s: %d" a b
  in
  varMap |> List.map pairToString |> String.concat "; "

let findAnswer vars checkFn =
  let rec findAnswerRec vars varMap =
    match vars with
    | [] -> checkFn varMap
    | { name = n; values = vs } :: rest_vars ->
        vs
        |> List.concat_map (fun v ->
               if List.mem v (List.map snd varMap) then []
               else findAnswerRec rest_vars ((n, v) :: varMap))
  in
  findAnswerRec vars []

let checkAnswer rules varMap =
  if satisfyAll rules varMap then [ varMapToString varMap ] else []

let solve vars rules = findAnswer vars (checkAnswer rules)

let main =
  print_endline "Hello, World!";
  let startTime = Sys.time () in
  solve variables constraints |> List.iter print_endline;
  Printf.printf "Execution time: %fs\n" (Sys.time () -. startTime);
  print_endline "Done."

let () = main
