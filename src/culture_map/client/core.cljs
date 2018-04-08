(ns culture-map.client.core
  (:require
    [reagent.core :as r]
    [culture-map.client.state.core :refer [dispatch-sync]]
    [culture-map.client.views.app :refer [app-view]]))

(enable-console-print!)

(defn render []
  (r/render-component [app-view]
    (.. js/document (getElementById "app"))))

(defn ^:export init []
  (dispatch-sync [:init!])
  (render))

(defn ^:export reload []
  (render))
