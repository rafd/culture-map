(ns culture-map.server.db
  (:require
    [human-db.core :as human-db]
    [human-db.persistors.github]
    [human-db.processors.yaml]))

(def db-config
  {:processor :yaml
   :persistor {:type :github
               :user "culture-map-bot"
               :token ""
               :repo "cannawen/culture-map-data"
               :branch "dev"
               :data-path "data"
               :author {:name "Canna Wen"
                        :email "cannawen@gmail.com"}
               :committer {:name "Culture Map Bot"
                           :email "cannawen+culture-map-bot@gmail.com"}}})

(defn save-record! [record]
  (human-db/store-record! db-config (record :id) record))

(defn get-records []
  (human-db/get-records db-config))
