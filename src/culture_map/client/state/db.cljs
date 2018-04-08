(ns culture-map.client.state.db
  (:require
    [datascript.core :as d]
    [re-posh.core :refer [connect!]]))

(def schema
  {:db/global {:db/unique :db.unique/identity}
   :custom/id {:db/unique :db.unique/identity}
   :variant/id {:db/unique :db.unique/identity}
   :country/id {:db/unique :db.unique/identity}
   :custom/variants {:db/valueType :db.type/ref
                     :db/cardinality :db.cardinality/many}
   :variant/country-ids {:db/valueType :db.type/ref
                         :db/cardinality :db.cardinality/many}})

(defonce conn (d/create-conn schema))
(defonce _ (connect! conn))


; SAMPLE EAV SCHEMA

[
 ; country
 [0 :country/id "japan"]
 [0 :country/name "Japan"]
 [0 :country/type "country"]
 ; custom
 [1 :custom/id "uuid"]
 [1 :custom/name "driving side of road"]
 [1 :custom/type "custom"]
 [1 :custom/variants 2]
 ; variant
 [2 :variant/id "uuid"]
 [2 :variant/name "left"]
 [2 :variant/country-ids "japan"]]

