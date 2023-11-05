(ns send-more-money.core
  (:gen-class)
  (:require
   [send-more-money.constraint :as c]
   [send-more-money.expression :as e])
  (:import
   (send_more_money.constraint
    EqualityConstraint
    UniquenessConstraint)))

(defrecord Variable [^String var-name range]
  Object)

(def ^:private VARS
  [(Variable. "M" (range 1 10))
   (Variable. "D" (range 0 10))
   (Variable. "E" (range 0 10))
   (Variable. "N" (range 0 10))
   (Variable. "O" (range 0 10))
   (Variable. "R" (range 0 10))
   (Variable. "S" (range 0 10))
   (Variable. "Y" (range 0 10))])

(def ^:private CONSTRAINTS
  (vector
   (UniquenessConstraint.)
   (EqualityConstraint.
    (e/addition-expr (e/build-number-expr ["S" "E" "N" "D"])
                     (e/build-number-expr ["M" "O" "R" "E"]))
    (e/build-number-expr ["M" "O" "N" "E" "Y"]))))

(defn- find-answer
  ([vars check-fn] (find-answer vars check-fn {}))
  ([vars check-fn var-map-acc]
   (if-let [[v & vs] vars]
     (let [{:keys [var-name range]} v
           existing-values (vals var-map-acc)]
       (reduce
        (fn [acc value]
          (if (not-any? #(= value %) existing-values)
            (->> (assoc var-map-acc var-name value)
                 (find-answer vs check-fn)
                 (into acc))
            acc))
        []
        range))
     (check-fn var-map-acc))))

(defn- check-answer-fn
  [constraints]
  (fn [var-map]
    (if (c/satisfy-all? constraints var-map) [var-map] [])))

(defn- solve
  [constraints]
  (find-answer VARS (check-answer-fn constraints)))

(defn -main
  []
  (println "started")
  (let [started (java.time.Instant/now)]
    (doseq [answer (solve CONSTRAINTS)] (println answer))
    (let [elapsed (.until started
                          (java.time.Instant/now)
                          java.time.temporal.ChronoUnit/MILLIS)]
      (println "end, elapsed:" elapsed))))
