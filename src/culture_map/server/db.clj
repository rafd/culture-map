(ns culture-map.server.db
  (:require
    [human-db.core :as human-db]
    [human-db.persistors.github]
    [human-db.processors.yaml]
    [culture-map.server.config :as config]))

(defn db-config []
  (or (config/get :db-config)
      {:processor :yaml
       :persistor {:type :file-system
                   :data-path "data"}}))

(defn save-record! [record]
  (human-db/store-record! (db-config) (record :id) record))

(defn get-records []
  (human-db/get-records (db-config)))
