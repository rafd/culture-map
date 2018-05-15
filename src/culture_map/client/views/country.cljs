(ns culture-map.client.views.country
  (:require
    [re-frame.core :refer [dispatch subscribe]]
    [culture-map.client.state.routes :as routes]))

(defn selected-variants [custom country-id]
  (->>
    (custom :custom/variants)
    (filter
      (fn [variant]
        (let [country-ids (->> (variant :variant/country-ids)
                            (map :country/id)
                            set)]
          (contains? country-ids country-id))))))

(defn variant-selected? [variant custom country-id]
  (contains? (set (selected-variants custom country-id)) variant))

(defn view-country-view [country-id]
  (when-let [country @(subscribe [:country country-id])]
    [:div
     [:h1 (country :country/name)]
     [:a
      {:href (routes/edit-country-path {:id country-id})}
      "Edit"]
     (for [custom @(subscribe [:country-customs country-id])]
       [:div {:key (custom :custom/id)}
        [:h2 (custom :custom/name)]
        (for [variant (selected-variants custom country-id)]
          [:h3 {:key (variant :variant/id)}
           (variant :variant/name)])])]))


(defn edit-country-view [country-id]
  (when-let [country @(subscribe [:country country-id])]
    [:div
     [:h1 (country :country/name)]
     [:button
      {:on-click (fn [_] (dispatch [:save-country! country-id]))}
      "Done"]
     [:button {:on-click
               (fn [_]
                 (dispatch [:country-new-custom! country-id]))}
      "New custom"]
     (for [custom @(subscribe [:customs+])]
       [:div {:key (custom :custom/id)}
        [:h2 (custom :custom/name)]
        (for [variant (custom :custom/variants)]
          [:span {:key (str (variant :variant/id) country-id)}
           [:input {:type "checkbox"
                    :checked (variant-selected? variant custom country-id)
                    :on-change
                    (fn [_]
                      (if (variant-selected? variant custom country-id)
                        (dispatch [:remove-custom-variant-country! (variant :variant/id) country-id])
                        (dispatch [:add-custom-variant-country! (variant :variant/id) country-id])))}]
           (variant :variant/name)])])]))

(defn country-view [country-id editing?]
  (if editing?
    (edit-country-view country-id)
    (view-country-view country-id)))
