(ns culture-map.client.views.app
  (:require
    [reagent.core :as r]
    [culture-map.client.state.core :refer [subscribe dispatch]]
    [culture-map.client.views.styles :refer [styles-view]]))

(defn customs-list-view []
  [:div.customs-list
   (doall
     (for [custom @(subscribe [:customs])]
       [:div.custom
        {:key (custom :custom/id)
         :on-click
         (fn [_]
           (dispatch [:view-custom! (custom :custom/id)]))}
        (custom :custom/name)]))
   [:button {:on-click
             (fn [_]
               (dispatch [:new-custom!]))}
    "New custom"]])

(defn custom-view [custom-id]
  (when-let [custom @(subscribe [:custom custom-id])]
    [:div.active-custom
     [:h1 (custom :custom/name)]
     [:button 
      {:on-click (fn [_] (dispatch [:edit-custom! custom-id]))}
      "Edit"]
     (doall
       (for [variant (custom :custom/variants)]
         [:div.variant
          {:key (variant :variant/id)}
          [:h2 (variant :variant/name)]
          (doall
            (for [country (variant :variant/country-ids)]
              [:div.country
               {:key (country :country/id)}
               (country :country/name)]))]))]))

(defn add-country-view [custom-id variant-id]
  (let [pick? (r/atom false)]
    (fn []
      (if @pick?
        [:div {:key 0}
         [:select {:on-change (fn [e]
                                (dispatch [:add-custom-variant-country! custom-id variant-id (.. e -target -value)])
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

(defn custom-editor-view [custom-id]
  (when-let [custom @(subscribe [:custom custom-id])]
    [:div.active-custom
     [:input {:value (custom :custom/name)
              :on-change
              (fn [e]
                (dispatch [:update-custom-name! custom-id (.. e -target -value)]))}]
     (doall
       (for [variant (custom :custom/variants)]
         [:div.variant
          {:key (variant :variant/id)}
          [:input {:value (variant :variant/name)
                   :on-change
                   (fn [e]
                     (dispatch [:update-custom-variant-name! custom-id (variant :variant/id) (.. e -target -value)]))}]
          [:button 
            {:on-click (fn [_] (dispatch [:remove-custom-variant! custom-id (variant :variant/id)]))}
            "×"]
          (doall
            (for [country (variant :variant/country-ids)]
              [:div.country
               {:key (country :country/id)}
               (country :country/name) 
               [:button 
                {:on-click (fn [_] (dispatch [:remove-custom-variant-country! custom-id (variant :variant/id) (country :country/id)]))}
                "×"]]))
          [add-country-view custom-id (variant :variant/id)]]))
     [:button {:on-click
               (fn [_]
                 (dispatch [:new-custom-variant! custom-id]))}
      "Add variant"]
     [:button {:on-click (fn [_]
                           (dispatch [:remove-custom! custom-id]))}
      "Delete"]
     [:button {:on-click (fn [_]
                           (dispatch [:view-custom! custom-id]))}
      "Done"]]))

(defn app-view []
  [:div.app
   [styles-view]
   [:h1 "Culture Map"]
   [customs-list-view]
   (let [[id data] @(subscribe [:page])]
     (case id
       :home [:div]
       :custom (if (data :editing?)
                 [custom-editor-view (data :custom-id)]
                 [custom-view (data :custom-id)])
       nil))])
