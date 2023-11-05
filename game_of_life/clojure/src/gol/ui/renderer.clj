(ns gol.ui.renderer
  (:gen-class)
  (:require
    [cljfx.api :as fx]
    [gol.domain.state :refer [*state]]
    [gol.ui.root :refer [root map-event-handler]]))

(def renderer
  (fx/create-renderer
    :middleware (fx/wrap-map-desc assoc :fx/type root)
    :opts {:fx.opt/map-event-handler map-event-handler}))


(defn open-window
  []
  (fx/mount-renderer *state renderer))

