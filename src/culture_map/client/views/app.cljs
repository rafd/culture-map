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
      (custom :name)])
   [:button {:on-click
             (fn [_]
               (dispatch [:new-custom]))}
    "New custom"]])

(defn custom-view [custom-id]
  (when-let [custom @(subscribe [:custom custom-id])]
    [:div.active-custom
     [:h1 (custom :name)]
     (doall
       (for [variant (custom :variants)]
         [:div.variant
          {:key (variant :id)}
          [:h2 (variant :name)]
          (doall
            (for [country-id (variant :country-ids)]
              (let [country @(subscribe [:country country-id])]
                [:div.country
                 {:key country-id}
                 (country :name)])))]))]))

(defn custom-editor-view [custom-id]
  (when-let [custom @(subscribe [:custom custom-id])]
    (println custom)
    [:div.active-custom
     [:input {:value (custom :name)
              :on-change
              (fn [e]
                (dispatch [:update-custom-name custom-id (.. e -target -value)]))}]
     (doall
       (for [variant (custom :variants)]
         [:div.variant
          {:key (variant :id)}
          [:input {:value (variant :name)
                   :on-change
                   (fn [e]
                     (dispatch [:update-custom-variant-name custom-id (variant :id) (.. e -target -value)]))}]
          (doall
            (for [country-id (variant :country-ids)]
              (if (nil? country-id)
                [:div {:key 0}
                 [:select {:on-change (fn [e]
                                        (dispatch [:add-custom-variant-country custom-id (variant :id) (.. e -target -value)]))}
                  [:option {:value nil} ""]
                  (for [country @(subscribe [:countries])]
                    [:option {:key (country :id)
                              :value (country :id)}
                     (country :name)])]]
                (let [country @(subscribe [:country country-id])]
                  [:div.country
                   {:key country-id}
                   (country :name)]))))
          [:button {:on-click
                    (fn [_]
                      (dispatch [:new-custom-variant-country custom-id (variant :id)]))}
           "Add country"]]))
     [:button {:on-click
               (fn [_]
                 (dispatch [:new-custom-variant custom-id]))}
      "Add variant"]]))

(defn app-view []
  [:div.app
   [:h1 "Culture Map"]
   [customs-list-view]
   (let [page @(subscribe [:page])]
     (case (page :type)
       :home [:div]
       :custom (if (page :editing?)
                 [custom-editor-view (page :custom-id)]
                 [custom-view (page :custom-id)])))])
