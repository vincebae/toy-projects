(ns send-more-money.constraint
  (:gen-class)
  (:require
   [send-more-money.expression :refer [evaluate]]))

(defprotocol Constraint
  (satisfy? [this var-map]))

(defrecord UniquenessConstraint []
  Constraint
  (satisfy? [_ _] true))

(defrecord EqualityConstraint [expr1 expr2]
  Constraint
  (satisfy? [this var-map]
    (= (evaluate (:expr1 this) var-map)
       (evaluate (:expr2 this) var-map))))

(defn satisfy-all?
  [constraints var-map]
  (every? #(satisfy? % var-map) constraints))
