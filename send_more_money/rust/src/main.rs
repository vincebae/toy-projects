use std::boxed::Box;
use std::collections::BTreeMap;
use std::ops::Range;
use std::time::Instant;

type VarMap = BTreeMap<String, i32>;

enum Expr {
    Const(i32),
    Variable(String),
    Addition(Box<Expr>, Box<Expr>),
    Multiplication(Box<Expr>, Box<Expr>)
}

enum Constraint {
    Uniqueness,
    Equality(Box<Expr>, Box<Expr>)
}

struct Variable {
    name : String,
    values : Range<i32>
}

fn evaluate(expr : &Expr, var_map: &VarMap) -> i32 {
    match expr {
        Expr::Const(value) => *value,
        Expr::Variable(var_name) => var_map[var_name],
        Expr::Addition(expr1, expr2) => 
            evaluate(expr1, var_map) + evaluate(expr2, var_map),
        Expr::Multiplication(expr1, expr2) =>
            evaluate(expr1, var_map) * evaluate(expr2, var_map),
    }
}

fn build_number_expr(var_names : &[&str]) -> Box<Expr> {
    let mut result: Option<Box<Expr>> = None;
    for var_name in var_names {
        let var_expr : Box<Expr> = Box::new(Expr::Variable(var_name.to_string()));
        result = match result {
            None => Some(var_expr),
            Some(expr) =>
                Some(Box::new(
                        Expr::Addition(
                            Box::new(
                                Expr::Multiplication(
                                    expr,
                                    Box::new(Expr::Const(10)))),
                            var_expr)))
        }
    }
    match result {
        None => panic!("Number expression is empty"),
        Some(expr) => expr
    }
}

fn satisfy(constraint : &Constraint, var_map : &VarMap) -> bool {
    match constraint {
        Constraint::Uniqueness => true,
        Constraint::Equality(expr1, expr2) =>
            evaluate(expr1, var_map) == evaluate(expr2, var_map)
    }
}

fn satisfy_all(constraints : &[Constraint], var_map : &VarMap) -> bool {
    for constraint in constraints {
        if !satisfy(constraint, var_map) {
            return false
        }
    }
    true
}

fn solve(variables : &[Variable], constraints : &[Constraint]) {
    let num_variables : usize = variables.len();
    let var_values : Vec<i32> = vec![-1; num_variables];
    search_answer(variables, constraints, 0, &var_values);
}

fn search_answer(
    variables : &[Variable],
    constraints : &[Constraint],
    depth : usize,
    var_values : &Vec<i32>) {
    if variables.len() == depth {
        let mut var_map : BTreeMap<String, i32> = BTreeMap::new();
        for i in 0 .. depth {
            var_map.insert(variables[i].name.clone(), var_values[i]);
        }
        if satisfy_all(constraints, &var_map) {
            for (var_name, var_value) in &var_map {
                print!("{}: {}, ", var_name, var_value);
            }
            println!(".");
        }
        return
    }
    for var_value in variables[depth].values.clone() {
        if !var_values.contains(&var_value) {
            let mut new_var_values = var_values.clone();
            new_var_values[depth] = var_value;
            search_answer(variables, constraints, depth + 1, &new_var_values);
        }
    }
}

fn main() {
    println!("Hello, world!");
    let variables: [Variable; 8] = [
        Variable{ name: "M".to_string(), values: 1 .. 10},
        Variable{ name: "D".to_string(), values: 0 .. 10},
        Variable{ name: "E".to_string(), values: 0 .. 10},
        Variable{ name: "N".to_string(), values: 0 .. 10},
        Variable{ name: "O".to_string(), values: 0 .. 10},
        Variable{ name: "R".to_string(), values: 0 .. 10},
        Variable{ name: "S".to_string(), values: 0 .. 10},
        Variable{ name: "Y".to_string(), values: 0 .. 10}
    ];

    let constraints: [Constraint; 2] = [
        Constraint::Uniqueness,
        Constraint::Equality(
            Box::new(
                Expr::Addition(
                    build_number_expr(&["S", "E", "N", "D"]),
                    build_number_expr(&["M", "O", "R", "E"]))),
            build_number_expr(&["M", "O", "N", "E", "Y"]))
    ];
    let started = Instant::now();
    solve(&variables, &constraints);
    let elapsed_time = started.elapsed();
    println!("Done: elapsed = {}.", elapsed_time.as_millis());
}
