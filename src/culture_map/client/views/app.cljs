(ns culture-map.client.views.app
  (:require
    [re-frame.core :refer [subscribe dispatch]]))

(defn customs-list-view []
  [:div.customs-list
   (for [custom @(subscribe [:customs])]
     [:div.custom
      {:key (custom :id)
       :on-click
       (fn [_]
         (dispatch [:set-active-custom-id (custom :id)]))}

      (custom :name)])])

(defn active-custom-view []
  (let [custom-id @(subscribe [:active-custom-id])
        custom (->> @(subscribe [:customs])
                    (filter (fn [custom]
                              (= (custom :id) custom-id)))
                    first)]
    (when custom
      [:div.active-custom
       [:h1 (custom :name)]
       (doall
         (for [variant (custom :variants)]
           [:div.variant
            {:key (variant :id)}
            [:h2 (variant :name)]
            (doall
              (for [country-id (variant :country-ids)]
                (let [country (->> @(subscribe [:countries])
                                   (filter (fn [country]
                                             (= (country :id) country-id)))
                                   first)]
                  [:div.country
                   {:key country-id}
                   (country :name)])))]))])))

(defn app-view []
  [:div.app
   [:h1 "Culture Map"]
   [customs-list-view]
   [active-custom-view]])
