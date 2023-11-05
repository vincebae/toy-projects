(ns gol.core
  (:gen-class)
  (:require
    [criterium.core :refer [bench]]
    [gol.domain.game-of-life :as g]))
    ; [gol.ui.renderer :refer [open-window]]))


(defn bench-next-stage
  []
  (let [grid (g/rand-grid)]
    ; (println "Bench next-stage-v1")
    ; (bench (doall (g/next-stage-v1 grid)))
    ; (println "Bench next-stage-v2")
    ; (bench (doall (g/next-stage-v2 grid)))
    (println "Bench next-stage-v3")
    (bench (doall (g/next-stage-v3 grid)))
    (println "Bench done")))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  ; (open-window)
  (println "Main done.")
  (bench-next-stage)
  ;;
  )
