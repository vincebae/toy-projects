(ns gol.domain.state
  (:gen-class)
  (:require
    [gol.domain.game-of-life :refer [rand-grid]]))


(def *state
  (atom {:running false
         :interval-in-ms 200
         :grid (rand-grid)}))


(defn state
  [key]
  (get @*state key))


(defn update-state
  [key value]
  (swap! *state assoc key value))
