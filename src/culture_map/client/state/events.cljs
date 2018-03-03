(ns culture-map.client.state.events
  (:require
    [re-frame.core :refer [reg-fx reg-event-fx]]
    [culture-map.client.state.fx.ajax :refer [ajax-fx]]))

(defn key-by-id [coll]
  (reduce (fn [memo item]
              (assoc memo (item :id) item))
    {}
    coll))

(reg-fx :ajax ajax-fx)

(reg-event-fx :init
  (fn [{db :db} _]
    {:db {:customs {}
          :countries {}
          :active-custom-id nil}
     :dispatch [:get-initial-data]}))

(reg-event-fx :set-active-custom-id
  (fn [{db :db} [_ id]]
    {:db (assoc db :active-custom-id id)}))

(reg-event-fx :get-initial-data
  (fn [{db :db} _]
    {:ajax {:uri "/api/records"
            :method :get
            :on-success :handle-initial-data}}))

(reg-event-fx :handle-initial-data
  (fn [{db :db} [_ records]]
    (let [grouped-records (group-by :type records)]
      {:db (assoc db :customs (-> (get grouped-records "custom")
                                  key-by-id)
                     :countries (-> (get grouped-records "country")
                                    key-by-id))})))
