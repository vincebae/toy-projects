(ns send-more-money.expression
  (:gen-class))

(defprotocol Expr
  (evaluate [this var-map]))

(defrecord ConstExpr [^long value]
  Expr
  (evaluate [this _]
    (:value this)))

(defrecord VariableExpr [^String var-name]
  Expr
  (evaluate [this var-map]
    (get var-map (:var-name this))))

(defrecord AdditionExpr [expr1 expr2]
  Expr
  (evaluate [this var-map]
    (+ (evaluate (:expr1 this) var-map)
       (evaluate (:expr2 this) var-map))))

(defrecord MultiplicationExpr [expr1 expr2]
  Expr
  (evaluate [this var-map]
    (* (evaluate (:expr1 this) var-map)
       (evaluate (:expr2 this) var-map))))

(defn const-expr
  [^long value]
  (ConstExpr. value))

(defn variable-expr
  [^String var-name]
  (VariableExpr. var-name))

(defn addition-expr
  [expr1 expr2]
  (AdditionExpr. expr1 expr2))

(defn multiplication-expr
  [expr1 expr2]
  (MultiplicationExpr. expr1 expr2))

(defn build-number-expr
  [[v & vs]]
  (reduce
   (fn [acc var-name]
     (addition-expr (multiplication-expr acc (const-expr 10))
                    (variable-expr var-name)))
   (variable-expr v)
   vs))
