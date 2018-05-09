(ns culture-map.client.views.country
  (:require
    [re-frame.core :refer [dispatch subscribe]]
    [culture-map.client.state.routes :as routes]))

(defn country-view [country-id editing?]
  (when-let [country @(subscribe [:country country-id])]
    [:div
     [:h1 (country :country/name)]

     (if editing?
       [:button
        {:on-click (fn [_] (dispatch [:save-country! country-id]))}
        "Done"]
       [:a
        {:href (routes/edit-country-path {:id country-id})}
        "Edit"])
     (when editing?
       [:button {:on-click
                 (fn [_]
                   (dispatch [:country-new-custom! country-id]))}
        "New custom"])
     (for [custom (if editing?
                    @(subscribe [:customs+])
                    @(subscribe [:country-customs country-id]))]
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


