(ns culture-map.server.config
  (:refer-clojure :exclude [get]))

(defn file
  "Returns file object, or nil if does not exist"
  [path]
  (let [f (clojure.java.io/file path)]
    (if (.exists f) f nil)))

(defn- get-from-file [key]
  (some-> "config.edn"
          file
          slurp
          read-string
          key))

(defn- get-from-env [key]
  (some-> (System/getenv "CONFIG")
          read-string
          key))

(defn get [key]
  (or (get-from-file key)
      (get-from-env key)))
