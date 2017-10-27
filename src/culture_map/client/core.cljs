(ns culture-map.client.core)

(enable-console-print!)

(defn render []
  (println "Hello World"))

(defn ^:export init []
  (render))

(defn ^:export reload []
  (render))