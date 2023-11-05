(ns gol.ui.components
  (:gen-class)
  (:require
    [gol.domain.game-loop :refer [rand-game toggle-game-loop]]
    [gol.domain.state :refer [update-state]]))


(defn rand-button
  [{:keys [disable]}]
  {:fx/type :button
   :text "Randomize"
   :disable disable
   :on-mouse-clicked {:event/type ::rand-button-clicked}})


(defn toggle-button
  [{:keys [_]}]
  {:fx/type :button
   :text "Start / End"
   :on-mouse-clicked {:event/type ::toggle-button-clicked}})


(defn interval-slider
  [{:keys [interval-in-ms]}]
  {:fx/type :slider
   :pref-width 500
   ;; unit of 10. slider value 10 mins 100ms
   :min 0
   :max 100
   :orientation :horizontal
   :major-tick-unit 2 
   :snap-to-ticks true
   :value-changing true
   :value (/ interval-in-ms 10)
   :on-value-changed {:event/type ::interval-changed}})


(defn interval
  [{:keys [interval-in-ms]}]
  {:fx/type :label
   :pref-width 200
   :text (str interval-in-ms " ms")})


(def event-handlers
  {::rand-button-clicked (fn [_] (rand-game))
   ::toggle-button-clicked (fn [_] (toggle-game-loop))
   ::interval-changed #(update-state :interval-in-ms (* (int (:fx/event %)) 10))
   ;;
   })
