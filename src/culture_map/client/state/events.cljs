(ns culture-map.client.state.events
  (:require
    [datascript.core :as d]
    [re-frame.core :refer [reg-fx reg-event-fx inject-cofx]]
    [culture-map.client.state.fx.ajax :refer [ajax-fx]]
    [bloom.omni.eav :as eav]))

(defn key-by-id [coll]
  (reduce (fn [memo item]
              (assoc memo (item :id) item))
    {}
    coll))

(reg-fx :ajax ajax-fx)

(reg-event-fx :init!
  (fn [_ _]
    {:dispatch-n [[:set-page! :home {}]
                  [:get-initial-data!]]}))

(reg-event-fx :set-page!
  (fn [_ [_ id data]]
    {:transact [{:db/global :page
                 :page/id id
                 :page/data data}]}))

(reg-event-fx :view-custom!
  (fn [_ [_ id]]
    {:dispatch [:set-page! :custom {:custom-id id
                                    :editing? false}]}))

(reg-event-fx :edit-custom!
  (fn [_ [_ custom-id]]
    {:dispatch [:set-page! :custom {:custom-id custom-id
                                    :editing? true}]}))

(reg-event-fx :get-initial-data!
  (fn [_ _]
    {:ajax {:uri "/api/records"
            :method :get
            :on-success :handle-initial-data!}}))

(reg-event-fx :new-custom!
  (fn [_ _]
    (let [custom {:custom/id (random-uuid)
                  :custom/name ""
                  :custom/type "custom"
                  :custom/variants []}]
      {:transact [custom]
       :dispatch [:set-page! :custom {:custom-id (custom :custom/id)
                                      :editing? true}]})))

(reg-event-fx :remove-custom!
  [(inject-cofx :ds)]
  (fn [{ds :ds} [_ custom-id]]
    (let [variant-eids (d/q '[:find [?variant-eid ...]
                              :in $ ?custom-id
                              :where 
                              [?custom-eid :custom/id ?custom-id]
                              [?custom-eid :custom/variants ?variant-eid]]
                             ds
                             custom-id)]
      {:transact (concat [[:db.fn/retractEntity [:custom/id custom-id]]]
                         (for [variant-eid variant-eids]
                           [:db.fn/retractEntity variant-eid]))
       :dispatch [:set-page! :home {}]})))

(reg-event-fx :update-custom-name!
  (fn [_ [_ custom-id value]]
    {:transact [{:custom/id custom-id
                 :custom/name value}]}))

(reg-event-fx :new-custom-variant!
  (fn [_ [_ custom-id]]
    {:transact [{:db/id "variantid"
                 :variant/country-ids []
                 :variant/id (random-uuid)
                 :variant/name "variant"}
                {:custom/id custom-id
                 :custom/variants "variantid"}]}))

(reg-event-fx :remove-custom-variant!
  (fn [_ [_ custom-id variant-id]]
    {:transact [[:db.fn/retractEntity [:variant/id variant-id]]]}))

(reg-event-fx :update-custom-variant-name!
  (fn [_ [_ custom-id variant-id value]]
    {:transact [{:variant/id variant-id
                 :variant/name value}]}))

(reg-event-fx :add-custom-variant-country!
  (fn [_ [_ custom-id variant-id country-id]]
    {:transact [{:variant/id variant-id
                 :variant/country-ids [{:country/id country-id}]}]}))

(reg-event-fx :remove-custom-variant-country!
  (fn [_ [_ custom-id variant-id country-id]]
    {:transact [[:db/retract [:variant/id variant-id] :variant/country-ids [:country/id country-id]]]}))

(reg-event-fx :handle-initial-data!
  (fn [_ [_ records]]
    {:transact (->> records
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
                             (concat [:db/add] eav))))}))
