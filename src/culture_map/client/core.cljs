(ns culture-map.client.core
  (:require
    [reagent.core :as r]
    [culture-map.client.state.subs]
    [culture-map.client.state.events]
    [re-frame.core :refer [dispatch]]
    [culture-map.client.views.app :refer [app-view]]))

(enable-console-print!)

(defn render []
  (r/render-component [app-view]
    (.. js/document (getElementById "app"))))

(defn ^:export init []
  (render)
  (dispatch [:init]))

(defn ^:export reload []
  (render))
