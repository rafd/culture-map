(ns culture-map.client.state.events
  (:require
    [bloom.omni.router :as router]
    [datascript.core :as d]
    [re-frame.core :refer [reg-fx reg-event-fx inject-cofx]]
    [culture-map.client.state.fx.ajax :refer [ajax-fx]]
    [culture-map.client.state.convert :as convert]
    [culture-map.client.state.routes :as routes]))

(defn key-by-id [coll]
  (reduce (fn [memo item]
              (assoc memo (item :id) item))
    {}
    coll))

(reg-fx :ajax ajax-fx)

(reg-fx :router router/fx)

(reg-event-fx :init!
  (fn [_ _]
    {:router [:init!
              :dispatch-current!]
     :dispatch-n [[:get-initial-data!]]}))

(reg-event-fx :set-page!
  (fn [_ [_ id data]]
    {:transact [{:db/global :page
                 :page/id id
                 :page/data data}]}))

(reg-event-fx :save-custom!
  (fn [_ [_ custom-id]]
    {:router [:navigate! (routes/view-custom-path {:id custom-id})]
     :dispatch [:-persist-custom! custom-id]}))

(reg-event-fx :get-initial-data!
  (fn [_ _]
    {:ajax {:uri "/api/records"
            :method :get
            :on-success :handle-initial-data!}}))

(reg-event-fx :new-custom!
  (fn [_ _]
    (let [custom {:custom/id (random-uuid)
                  :custom/name "new-custom"
                  :custom/type "custom"
                  :custom/variants []}]
      {:transact [custom]
       :router [:navigate! (routes/edit-custom-path {:id (custom :custom/id)})]})))

(reg-event-fx :-persist-custom!
  [(inject-cofx :ds)]
  (fn [{ds :ds} [_ custom-id]]
    {:ajax {:uri "/api/records"
            :method :put
            :params {:custom (convert/ds->custom ds custom-id)}}}))

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
       :router [:navigate! (routes/index-path)]})))

(reg-event-fx :update-custom-name!
  (fn [_ [_ custom-id value]]
    {:transact [{:custom/id custom-id
                 :custom/name value}]}))

(reg-event-fx :new-custom-variant!
  (fn [_ [_ custom-id]]
    {:transact [{:db/id "variantid"
                 :variant/country-ids []
                 :variant/type "variant"
                 :variant/id (random-uuid)
                 :variant/name "variant"}
                {:custom/id custom-id
                 :custom/variants "variantid"}]}))

(reg-event-fx :remove-custom-variant!
  (fn [_ [_ variant-id]]
    {:transact [[:db.fn/retractEntity [:variant/id variant-id]]]}))

(reg-event-fx :update-custom-variant-name!
  (fn [_ [_ variant-id value]]
    {:transact [{:variant/id variant-id
                 :variant/name value}]}))

(reg-event-fx :add-custom-variant-country!
  (fn [_ [_ variant-id country-id]]
    {:transact [{:variant/id variant-id
                 :variant/country-ids [{:country/id country-id}]}]}))

(reg-event-fx :remove-custom-variant-country!
  (fn [_ [_ variant-id country-id]]
    {:transact [[:db/retract [:variant/id variant-id] :variant/country-ids [:country/id country-id]]]}))

(reg-event-fx :handle-initial-data!
  (fn [_ [_ records]]
    {:transact (convert/records->txs records)}))
