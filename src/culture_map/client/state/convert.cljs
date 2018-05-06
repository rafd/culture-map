(ns culture-map.client.state.convert
  (:require
    [datascript.core :as d]
    [clojure.walk :as walk]
    [bloom.omni.eav :as eav]))

(defn denamespace-keys [coll]
  (walk/postwalk (fn [form]
                   (if (keyword? form)
                     (keyword (name form))
                     form))
    coll))

(defn records->txs [records]
  (->> records
       vec
       (eav/namespace-keys (fn [r]
                             (keyword (r :type))))
       (eav/recs->eavs (fn [r]
                         (let [abs (fn [i]
                                     (if (pos-int? i)
                                       i
                                       (* -1 i)))]
                           (abs (hash (or
                                        (r :custom/id)
                                        (r :variant/id)
                                        (r :country/id))))))
         {:custom/id :id
          :variant/id :id
          :country/id :id
          :custom/variants  :embed-many
          :variant/country-ids :reference-many})
       (map (fn [eav]
              (concat [:db/add] eav)))))

(defn ds->custom [ds custom-id]
  (-> (d/pull ds '[:custom/id
                   :custom/name
                   :custom/type
                   {:custom/variants [:variant/id
                                      :variant/name
                                      :variant/type
                                      {:variant/country-ids [:country/id]}]}]
        [:custom/id custom-id])
      (update :custom/variants
        (fn [variants]
          (map (fn [variant]
                 (update variant :variant/country-ids
                   (fn [country-ids]
                     (map :country/id country-ids)))) variants)))
      denamespace-keys))
