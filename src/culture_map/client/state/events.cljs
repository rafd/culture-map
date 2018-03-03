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
          :page {:type :home}}
     :dispatch [:get-initial-data]}))

(reg-event-fx :set-active-custom-id
  (fn [{db :db} [_ id]]
    {:db (assoc db :page {:type :custom
                          :custom-id id
                          :editing? false})}))

(reg-event-fx :get-initial-data
  (fn [{db :db} _]
    {:ajax {:uri "/api/records"
            :method :get
            :on-success :handle-initial-data}}))

(reg-event-fx :new-custom
  (fn [{db :db} _]
    (let [custom {:id (random-uuid)
                  :name ""
                  :type "custom"
                  :variants []}]
      {:db (-> db
               (assoc :page {:type :custom
                             :custom-id (custom :id)
                             :editing? true})
               (assoc-in [:customs (custom :id)] custom))})))

(reg-event-fx :update-custom-name
  (fn [{db :db} [_ custom-id value]]
    {:db (assoc-in db [:customs custom-id :name] value)}))

(reg-event-fx :update-custom-variant-name
  (fn [{db :db} [_ custom-id variant-id value]]
    {:db (update-in
           db
           [:customs custom-id :variants]
           (fn [variants]
             (mapv
               (fn [variant]
                 (if (= (variant :id) variant-id)
                   (assoc variant :name value)
                   variant))
               variants)))}))

(reg-event-fx :new-custom-variant-country
  (fn [{db :db} [_ custom-id variant-id]]
    {:db (update-in
           db
           [:customs custom-id :variants]
           (fn [variants]
             (mapv
               (fn [variant]
                 (if (= (variant :id) variant-id)
                   (update variant :country-ids conj nil)
                   variant))
               variants)))}))

(reg-event-fx :add-custom-variant-country
  (fn [{db :db} [_ custom-id variant-id country-id]]
    {:db (update-in
           db
           [:customs custom-id :variants]
           (fn [variants]
             (mapv
               (fn [variant]
                 (if (= (variant :id) variant-id)
                   (-> variant
                       (update :country-ids conj country-id)
                       (update :country-ids (fn [ids]
                                              (vec (remove nil? ids)))))
                   variant))
               variants)))}))

(reg-event-fx :new-custom-variant
  (fn [{db :db} [_ custom-id]]
    (let [variant {:country-ids []
                   :id (random-uuid)
                   :name ""}]
      {:db (update-in db [:customs custom-id :variants] conj variant)})))

(reg-event-fx :handle-initial-data
  (fn [{db :db} [_ records]]
    (let [grouped-records (group-by :type records)]
      {:db (assoc db :customs (-> (get grouped-records "custom")
                                  key-by-id)
                     :countries (-> (get grouped-records "country")
                                    key-by-id))})))
