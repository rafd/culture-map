(ns culture-map.client.views.custom
  (:require
    [reagent.core :as r]
    [culture-map.client.views.map :refer [map-view]]
    [culture-map.client.styles.colors :as colors]
    [culture-map.client.state.core :refer [subscribe dispatch]]))

(defn color [custom variant]
  (let [variants (custom :custom/variants)
        index (.indexOf variants variant)]
    (colors/map-variant-color index (count variants))))

(defn custom-name-view [custom editing?]
  (if editing?
    [:input {:value (custom :custom/name)
             :on-change
             (fn [e]
               (dispatch [:update-custom-name! (custom :custom/id) (.. e -target -value)]))}]
    [:h1 (custom :custom/name)]))

(defn edit-toggle-button-view [custom editing?]
  (if editing?
    [:button.edit {:on-click (fn [_] (dispatch [:view-custom! (custom :custom/id)]))}
     "Done"]
    [:button.edit
     {:on-click (fn [_] (dispatch [:edit-custom! (custom :custom/id)]))}
     "Edit"]))

(defn variant-name-view [variant editing?]
  (if editing?
    [:input {:value (variant :variant/name)
             :on-change
             (fn [e]
               (dispatch [:update-custom-variant-name! (variant :variant/id) (.. e -target -value)]))}]
    [:h2 (variant :variant/name)]))

(defn add-country-view [variant-id]
  (let [pick? (r/atom false)]
    (fn []
      (if @pick?
        [:div {:key 0}
         [:select {:on-change (fn [e]
                                (dispatch [:add-custom-variant-country! variant-id (.. e -target -value)])
                                (reset! pick? false))}
          [:option {:value nil} ""]
          (doall
            (for [country @(subscribe [:countries])]
              [:option {:key (country :country/id)
                        :value (country :country/id)}
               (country :country/name)]))]]
        [:button {:on-click
                  (fn [_]
                    (reset! pick? true))}
         "Add country"]))))

(defn custom-view [custom-id editing?]
  (when-let [custom @(subscribe [:custom custom-id])]
    [:div.active-custom
     [custom-name-view custom editing?]
     [edit-toggle-button-view custom editing?]
     (when editing?
       [:button {:on-click (fn [_]
                             (dispatch [:remove-custom! custom-id]))}
        "Delete"])
     [map-view (->> (custom :custom/variants)
                 (map (fn [variant]
                        (->> (variant :variant/country-ids)
                          (map (fn [country]
                                 [(country :country/id)
                                  (color custom variant)])))))
                 (apply concat))]
     [:div.variants
      (doall
        (for [variant (custom :custom/variants)]
          [:div.variant
           {:key (variant :variant/id)}
           [:div.color-square {:style {:background (color custom variant)}}]
           [variant-name-view variant editing?]
           (when editing?
             [:button
              {:on-click (fn [_] (dispatch [:remove-custom-variant! (variant :variant/id)]))}
              "×"])
           [:div.countries
            (doall
              (for [country (variant :variant/country-ids)]
                [:div.country
                 {:key (country :country/id)}
                 (country :country/name)
                 (when editing?
                   [:button
                    {:on-click (fn [_] (dispatch [:remove-custom-variant-country! (variant :variant/id) (country :country/id)]))}
                    "×"])]))
            (when editing?
              [add-country-view (variant :variant/id)])]]))
      (when editing?
        [:button {:on-click
                  (fn [_]
                    (dispatch [:new-custom-variant! custom-id]))}
         "Add variant"])]]))
