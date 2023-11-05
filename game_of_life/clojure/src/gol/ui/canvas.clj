(ns gol.ui.canvas
  (:gen-class)
  (:require
    [gol.domain.game-of-life :refer [rows columns]])
  (:import
    (javafx.scene.canvas
      Canvas)))


(def cell-width 5)
(def cell-height 5)
(def width (* columns cell-width))
(def height (* rows cell-height))


(defn- draw-fn
  [grid on-color off-color]
  (fn [^Canvas canvas]
    (doseq
      [[row row-index] (map #(vector %1 %2) grid (range (count grid)))]
      (doseq
        [[cell col-index] (map #(vector %1 %2) row (range (count row)))]
        (let
          [x (* col-index cell-width)
           y (* row-index cell-height)]
          (doto (.getGraphicsContext2D canvas)
            (.setFill (if cell on-color off-color))
            (.fillRect x y cell-width cell-height))
          ;;
          )))))


(defn canvas
  [{:keys [grid on-color off-color]}]
  {:fx/type :canvas
   :width width
   :height height
   :draw (draw-fn grid on-color off-color)})
