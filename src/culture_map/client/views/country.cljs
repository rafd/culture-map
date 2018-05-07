(ns culture-map.client.views.country
  (:require
    [culture-map.client.state.core :refer [subscribe]]))

(defn country-view [country-id]
  (let [country @(subscribe [:country country-id])
        customs @(subscribe [:country-customs country-id])]
    [:div
     [:h1 (country :country/name)]
     (for [custom customs]
       [:div {:key (custom :custom/id)}
        [:h2 (custom :custom/name)]
        (for [variant (->> (custom :custom/variants)
                           (filter
                             (fn [variant]
                               (let [country-ids (->> (variant :variant/country-ids)
                                                      (map :country/id)
                                                      set)]
                                 (contains? country-ids country-id)))))]
          [:h3 {:key (variant :variant/id)}
           (variant :variant/name)])])]))

