(ns culture-map.client.state.subs
  (:require
    [re-frame.core :refer [reg-sub]]))

(reg-sub :customs
  (fn [db _]
    (db :customs)))

(reg-sub :country
  (fn [db [_ id]]
    (->> (db :countries)
         (filter (fn [country]
                   (= (country :id) id)))
         first)))

(reg-sub :active-custom
  (fn [db _]
    (->> (db :customs)
         (filter (fn [custom]
                   (= (custom :id) (db :active-custom-id))))
         first)))
