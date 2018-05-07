(ns culture-map.client.state.subs
  (:require
    [re-frame.core :refer [reg-sub reg-sub-raw]]
    [posh.reagent :as p]
    [reagent.ratom :as r]
    [re-posh.db :refer [store]]
    [re-posh.core :refer [reg-query-sub reg-pull-sub]]))

(defn reg-sub-pull
  [sub-name query-pattern pull-pattern]
  (reg-sub-raw sub-name
    (fn [_ [_ & args]]
      ; result should be: id, nil, [id ...] or []
      (let [result (apply p/q query-pattern @store args)]
        (cond
          (vector? @result) ; [id ...] or []
          (->> @result
               (map (fn [id]
                      @(p/pull @store pull-pattern id)))
               doall
               r/reaction)

          (some? @result) ; id
          (p/pull @store pull-pattern @result)

          :else ; nil
          result)))))

(reg-sub-pull :country
  '[:find ?e .
    :in $ ?id
    :where [?e :country/id ?id]]
  '[:country/id
    :country/name])

(reg-sub-pull :country-customs
  '[:find [?custom ...]
    :in $ ?country-id
    :where [?country :country/id ?country-id]
           [?variant :variant/country-ids ?country]
           [?custom :custom/variants ?variant]]
  '[:custom/id
    :custom/name
    {:custom/variants [:variant/id
                       :variant/name
                       {:variant/country-ids [:country/id]}]}])

(reg-sub-pull :countries
  '[:find [?e ...]
    :where [?e :country/id _]]
  '[:country/id
    :country/name])

(reg-sub-pull :custom
  '[:find ?custom .
    :in $ ?custom-id
    :where [?custom :custom/id ?custom-id]]
  '[:custom/id
    :custom/name
    {:custom/variants [:variant/id
                       :variant/name
                       {:variant/country-ids [:country/id
                                              :country/name]}]}])

(reg-sub-pull :customs
  '[:find [?e ...]
    :where [?e :custom/id _]]
  '[*])

(reg-query-sub :page
  '[:find [?id ?data]
    :where [?e :db/global :page]
           [?e :page/id ?id]
           [?e :page/data ?data]])

(reg-sub-raw :sidebar-countries
  (fn [_ _]
    (->> (p/q '[:find ?country-id ?country-name (count ?custom)
                :where
                [?country :country/id ?country-id]
                [?country :country/name ?country-name]
                [?variant :variant/country-ids ?country]
                [?custom :custom/variants ?variant]]
           @store)
         deref
         (filter (fn [[_ _ count]]
                   (< 3 count)))
         (map (fn [[id name _]]
                {:country/id id
                 :country/name name}))
         (sort-by :country/name)
         r/reaction)))

