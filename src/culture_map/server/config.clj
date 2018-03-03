(ns culture-map.server.config
  (:refer-clojure :exclude [get]))

(defn get [key]
  (some-> "config.edn"
          slurp
          read-string
          key))
