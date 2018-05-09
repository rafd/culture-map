(ns culture-map.client.views.country
  (:require
    [re-frame.core :refer [subscribe]]))

(defn country-view [country-id]
  (when-let [country @(subscribe [:country country-id])]
    [:div
     [:h1 (country :country/name)]
     (for [custom @(subscribe [:country-customs country-id])]
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


