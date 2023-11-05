(ns gol.domain.game-of-life
  (:gen-class))


(def rows 160)
(def columns 240)


(defn seq->2d-vectorize
  [seq]
  (->> seq
       (partition columns)
       (map #(into [] %))
       (into [])))


(defn rand-grid
  []
  (->> (repeatedly #(zero? (rand-int 2)))
       (take (* rows columns))
       (seq->2d-vectorize)))


(defn- cell
  [grid x y]
  (get-in grid [y x]))


(defn- neighbor-mod
  [grid x y [dx dy]]
  (let [neighbor-x (mod (+ x dx) columns)
        neighbor-y (mod (+ y dy) rows)]
    (cell grid neighbor-x neighbor-y)))


(defn- neighbor-no-mod
  [grid x y [dx dy]]
  (cell grid (+ x dx) (+ y dy)))


(def neighbor-deltas
  (drop 1 (for [x [0 -1 1] y [0 -1 1]] [x y])))


(defn- count-neighbors
  ([grid x y] (count-neighbors grid x y neighbor-mod))
  ([grid x y neighbor-fn]
   (->> neighbor-deltas
        (map #(neighbor-fn grid x y %))
        (map #(if % 1 0))
        (apply +))))


(defn- next-stage-value
  ([grid x y] (next-stage-value grid x y neighbor-mod))
  ([grid x y neighbor-fn]
   (let [cell (cell grid x y)
         neighbors (count-neighbors grid x y neighbor-fn)]
     (or (= neighbors 3) (and (= neighbors 2) cell)))))


(defn next-stage-v1
  [grid]
  (into []
        (for [y (range rows)]
          (into []
                (for [x (range columns)]
                  (next-stage-value grid x y))))))


(defn next-stage-v2
  [grid]
  (->>
    (for [y (range rows) x (range columns)]
      (next-stage-value grid x y))
    (seq->2d-vectorize)))


(defn next-stage-row
  [grid row-index]
  (let [last-row (dec rows)
        last-col (dec columns)
        neighbor-fn
        (if (contains? #{0 last-row} row-index) neighbor-mod neighbor-no-mod)]
    (-> (vector (next-stage-value grid 0 row-index neighbor-fn))
        (into
          (for [col (range 1 last-col)]
            (next-stage-value grid col row-index neighbor-fn)))
        (conj (next-stage-value grid last-col row-index)))))


(defn next-stage-v3
  [grid]
  (into [] (for [row (range rows)] (next-stage-row grid row))))


(def next-stage next-stage-v3)
