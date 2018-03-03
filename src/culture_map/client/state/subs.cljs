(ns culture-map.client.state.subs
  (:require
    [re-frame.core :refer [reg-sub]]))

(reg-sub :customs
  (fn [db _]
    (vals (db :customs))))

(reg-sub :country
  (fn [db [_ id]]
    (get-in db [:countries id])))

(reg-sub :countries
  (fn [db _]
    (vals (db :countries))))

(reg-sub :custom
  (fn [db [_ id]]
    (get-in db [:customs id])))

(reg-sub :page
  (fn [db _]
    (db :page)))

