import java.time.Instant
import java.time.temporal.ChronoUnit
import Expression.*
import Constraint.*

type VarMap = Map[String, Int]

case class Variable(name: String, values: Seq[Int])

enum Expression:
  case ConstExpr(value: Int)
  case VariableExpr(varName: String)
  case AdditionExpr(expr1: Expression, expr2: Expression)
  case MultiplicationExpr(expr1: Expression, expr2: Expression)

  def evaluate(varMap: VarMap): Int =
    this match
      case ConstExpr(value)      => value
      case VariableExpr(varName) => varMap(varName)
      case AdditionExpr(expr1, expr2) =>
        expr1.evaluate(varMap) + expr2.evaluate(varMap)
      case MultiplicationExpr(expr1, expr2) =>
        expr1.evaluate(varMap) * expr2.evaluate(varMap)

object Expression:
  def buildNumberExpr(vars: Seq[String]): Expression =
    vars match
      case Seq() => throw IllegalArgumentException("vars is empty")
      case x :: xs =>
        xs.foldLeft(VariableExpr(x))((acc: Expression, v: String) =>
          AdditionExpr(MultiplicationExpr(acc, ConstExpr(10)), VariableExpr(v))
        )

enum Constraint:
  case UniquenessConstraint
  case EqualityConstraint(expr1: Expression, expr2: Expression)

  def satisfy(varMap: VarMap): Boolean =
    this match
      case UniquenessConstraint => true
      case EqualityConstraint(expr1, expr2) =>
        expr1.evaluate(varMap) == expr2.evaluate(varMap)

object Constraint:
  def satisfyAll(constraints: Seq[Constraint], varMap: VarMap): Boolean =
    constraints.forall(_.satisfy(varMap))

class Puzzle(val vars: Seq[Variable], val constraints: Seq[Constraint])

class PuzzleSolver(val puzzle: Puzzle):
  def solve() = findAnswer(puzzle.vars).foreach(println)

  private def findAnswer(
      vars: Seq[Variable],
      varMap: VarMap = Map()
  ): Seq[String] =
    vars match
      case Seq() => checkFn(varMap)
      case Variable(varName, varValues) :: vs =>
        varValues.map(goDeeperFn(varName, vs, varMap)).flatten

  val checkFn =
    (varMap: VarMap) =>
      if Constraint.satisfyAll(puzzle.constraints, varMap)
      then List(varMap.toString)
      else List()

  val goDeeperFn =
    (varName: String, vars: Seq[Variable], varMap: VarMap) =>
      (value: Int) =>
        if varMap.exists((k, v) => v == value)
        then List()
        else findAnswer(vars, varMap + (varName -> value))

val VARIABLES = List(
  Variable("M", 1 to 9),
  Variable("D", 0 to 9),
  Variable("E", 0 to 9),
  Variable("N", 0 to 9),
  Variable("O", 0 to 9),
  Variable("R", 0 to 9),
  Variable("S", 0 to 9),
  Variable("Y", 0 to 9)
)

val CONSTRAINTS = List(
  UniquenessConstraint,
  EqualityConstraint(
    AdditionExpr(
      Expression.buildNumberExpr(List("S", "E", "N", "D")),
      Expression.buildNumberExpr(List("M", "O", "R", "E"))
    ),
    Expression.buildNumberExpr(List("M", "O", "N", "E", "Y"))
  )
)

@main def hello: Unit =
  println("Hello world!")
  val startTime = Instant.now()
  PuzzleSolver(Puzzle(VARIABLES, CONSTRAINTS)).solve()
  val endTime = Instant.now()
  val elapsed = startTime.until(endTime, ChronoUnit.MILLIS)
  println("Done. elapsed: " + elapsed)
