(ns gol.ui.root
  (:gen-class)
  (:require
    [gol.domain.game-loop :refer [shutdown]]
    [gol.ui.canvas :refer [canvas]]
    [gol.ui.components :as c])
  (:import
    (javafx.application
      Platform)
    (javafx.scene.paint
      Color)))


(defn root
  [{:keys [grid running interval-in-ms]}]
  {:fx/type :stage
   :showing true
   :title "Game of Life"
   :x 1000
   :y 100
   :width 1400
   :height 1000
   :resizable false
   :on-close-request (fn [_] (shutdown) (Platform/exit))
   :scene {:fx/type :scene
           :root {:fx/type :v-box
                  :alignment :center
                  :children [{:fx/type :h-box
                              :alignment :center
                              :spacing 20
                              :padding 20
                              :children [{:fx/type c/rand-button
                                          :disable running}
                                         {:fx/type c/toggle-button}
                                         {:fx/type c/interval-slider
                                          :interval-in-ms interval-in-ms}
                                         {:fx/type c/interval
                                          :interval-in-ms interval-in-ms}]}
                             {:fx/type canvas
                              :grid grid
                              :on-color Color/BLACK
                              :off-color Color/LIGHTGREY}
                             ;;
                             ]}}})


(def event-handlers
  (merge c/event-handlers))


(defn map-event-handler
  [event]
  (let [handle-fn (get event-handlers (:event/type event))]
    (handle-fn event)))
