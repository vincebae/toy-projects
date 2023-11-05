(ns send-more-money.expression2
  (:gen-class))

(defmulti evaluate :type)

(defmethod evaluate :const
  [expr _]
  (:value expr))

(defmethod evaluate :variable
  [expr var-map]
  (get var-map (:var-name expr)))

(defmethod evaluate :addition
  [expr var-map]
  (+ (evaluate (:expr1 expr) var-map)
     (evaluate (:expr2 expr) var-map)))

(defmethod evaluate :multiplication
  [expr var-map]
  (* (evaluate (:expr1 expr) var-map)
     (evaluate (:expr2 expr) var-map)))

(defn const-expr
  [value]
  {:type :const, :value value})

(defn variable-expr
  [var-name]
  {:type :variable, :var-name var-name})

(defn addition-expr
  [expr1 expr2]
  {:type :addition, :expr1 expr1, :expr2 expr2})

(defn multiplication-expr
  [expr1 expr2]
  {:type :multiplication, :expr1 expr1, :expr2 expr2})

(defn build-number-expr
  [var-names]
  (loop [[v & vs] var-names
         acc nil]
    (if v
      (let [new-expr (variable-expr v)
            new-acc (if acc
                      (addition-expr (multiplication-expr acc (const-expr 10))
                                     new-expr)
                      new-expr)]
        (recur vs new-acc))
      acc)))
