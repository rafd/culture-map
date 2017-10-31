(ns culture-map.client.state.subs
  (:require
    [re-frame.core :refer [reg-sub]]))

(reg-sub :customs
  (fn [db _]
    (vals (db :customs))))

(reg-sub :country
  (fn [db [_ id]]
    (get-in db [:countries id])))

(reg-sub :active-custom
  (fn [db _]
    (get-in db [:customs (db :active-custom-id)])))
