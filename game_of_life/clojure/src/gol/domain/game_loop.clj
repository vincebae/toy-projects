(ns gol.domain.game-loop
  (:gen-class)
  (:require
    [gol.domain.game-of-life :refer [rand-grid next-stage]]
    [gol.domain.state :refer [state update-state]])
  (:import
    (java.util.concurrent
      Executors)))


(def executor (Executors/newCachedThreadPool))


(defn game-loop-task-fn
  []
  (when (state :running)
    (Thread/sleep (state :interval-in-ms))
    (let [grid (state :grid)]
      (update-state :grid (next-stage grid)))
    (recur)))


(defn rand-game
  []
  (println "rand-game")
  (when (not (state :running))
    (update-state :grid (rand-grid))))


(defn start-game-loop
  []
  (println "start-game-loop")
  (when (not (state :running))
    (update-state :running true)
    (.submit executor game-loop-task-fn)
    (println "Game loop started")))


(defn end-game-loop
  []
  (println "end-game-loop")
  (update-state :running false))


(defn toggle-game-loop
  []
  (println "toggle-game-loop")
  (if (state :running)
    (end-game-loop)
    (start-game-loop)))


(defn shutdown
  []
  (end-game-loop)
  (.shutdown executor))
