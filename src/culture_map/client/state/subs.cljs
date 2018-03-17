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
               r/reaction)

          (some? @result) ; id
          (p/pull @store pull-pattern @result)

          :else ; nil
          result)))))

(reg-sub-pull :country
  '[:find ?e .
    :in $ ?id
    :where [?e :country/id ?id]]
  '[*])

(reg-sub-pull :countries
  '[:find [?e ...]
    :where [?e :country/id _]]
  '[*])

(reg-sub-pull :custom
  '[:find ?e .
    :in $ ?id
    :where [?e :custom/id ?id]]
  '[:custom/id
    :custom/name
    {:custom/variants [:variant/id
                       :variant/name
                       :variant/country-ids]}])

(reg-sub-pull :customs
  '[:find [?e ...]
    :where [?e :custom/id _]]
  '[*])

(reg-sub-pull :variant
  '[:find ?e .
    :in $ ?id
    :where [?e :variant/id ?id]]
  '[*])

(reg-query-sub :page
  '[:find [?id ?data]
    :where [?e :db/global :page]
           [?e :page/id ?id]
           [?e :page/data ?data]])
