(ns culture-map.lib.data-tools
  (:require
    [culture-map.server.db :as db]
    [clojure.set :as set]))

(defn all-entities-of-type [type]
  (filter
    #(= (% :type) type)
    (db/get-records)))

(defn get-first-record-matching [key value]
  (first
    (filter
      #(= (% key) value)
      (db/get-records))))

(defn country-ids-for-custom [custom-name]
    (reduce
      #(concat %1 (%2 :country-ids))
      []
      ((get-first-record-matching :name custom-name) :variants)))

(defn uncategorized-countries-for-custom [custom-name]
  (set/difference
    (set (map :id (all-entities-of-type "country")))
    (set (country-ids-for-custom custom-name))))


