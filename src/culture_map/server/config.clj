(ns culture-map.server.config
  (:refer-clojure :exclude [get]))

(defn file
  "Returns file object, or nil if does not exist"
  [path]
  (let [f (clojure.java.io/file path)]
    (if (.exists f) f nil)))

(defn get [key]
  (some-> "config.edn"
          file
          slurp
          read-string
          key))
