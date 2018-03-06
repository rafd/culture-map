(ns culture-map.client.state.core
  (:require
    [re-frame.core :as re-frame]
    ; require to initialize datascript, subs, events
    [culture-map.client.state.db]
    [culture-map.client.state.events]
    [culture-map.client.state.subs]))

(def dispatch re-frame/dispatch)
(def dispatch-sync re-frame/dispatch-sync)
(def subscribe re-frame/subscribe)
