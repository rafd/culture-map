(ns culture-map.client.state.events
  (:require
    [re-frame.core :refer [reg-fx reg-event-fx]]
    [culture-map.client.state.eav :as eav]
    [culture-map.client.state.fx.ajax :refer [ajax-fx]]))

(defn key-by-id [coll]
  (reduce (fn [memo item]
              (assoc memo (item :id) item))
    {}
    coll))

(reg-fx :ajax ajax-fx)

(reg-event-fx :init
  (fn [_ _]
    {:dispatch-n [[:set-page :home {}]
                  [:get-initial-data]]}))

(reg-event-fx :set-page
  (fn [_ [_ id data]]
    {:transact [{:db/global :page
                 :page/id id
                 :page/data data}]}))

(reg-event-fx :set-active-custom-id
  (fn [_ [_ id]]
    {:dispatch [:set-page :custom {:custom-id id
                                   :editing? false}]}))

(reg-event-fx :get-initial-data
  (fn [_ _]
    {:ajax {:uri "/api/records"
            :method :get
            :on-success :handle-initial-data}}))

(reg-event-fx :new-custom
  (fn [_ _]
    (let [custom {:custom/id (random-uuid)
                  :custom/name ""
                  :custom/type "custom"
                  :custom/variants []}]
      {:transact [custom]
       :dispatch [:set-page :custom {:custom-id (custom :custom/id)
                                     :editing? true}]})))

(reg-event-fx :update-custom-name
  (fn [_ [_ custom-id value]]
    {:transact [{:custom/id custom-id
                 :custom/name value}]}))

(reg-event-fx :update-custom-variant-name
  (fn [_ [_ custom-id variant-id value]]
    {:transact [{:variant/id variant-id
                 :variant/name value}]}))

(reg-event-fx :add-custom-variant-country
  (fn [_ [_ custom-id variant-id country-id]]
    {:transact [{:variant/id variant-id
                 :variant/country-ids country-id}]}))

(reg-event-fx :new-custom-variant
  (fn [_ [_ custom-id]]
    (let [variant {:variant/country-ids []
                   :variant/id (random-uuid)
                   :variant/name ""}]
      {:transact [variant
                  {:custom/id custom-id
                   :custom/variants (variant :variant/id)}]})))

(reg-event-fx :handle-initial-data
  (fn [_ [_ records]]
    {:transact (->> records
                    ; namespace each key based on the type of entity
                    eav/recs->eavs
                    (group-by first)
                    (mapcat (fn [[e eavs]]
                              (let [e-type (->> eavs
                                                (filter (fn [[e a v]]
                                                          (= a :type)))
                                                last
                                                last)]
                                (->> eavs
                                     (map (fn [[e a v]]
                                            [e (keyword e-type a) v]))))))
                    (map (fn [eav]
                           (concat [:db/add] eav))))}))
