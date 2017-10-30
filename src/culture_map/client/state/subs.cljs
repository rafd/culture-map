(ns culture-map.client.state.subs
  (:require
    [re-frame.core :refer [reg-sub]]))

(reg-sub :customs
  (fn [db _]
    (db :customs)))

(reg-sub :countries
  (fn [db _]
    (db :countries)))

(reg-sub :active-custom-id
  (fn [db _]
    (db :active-custom-id)))
