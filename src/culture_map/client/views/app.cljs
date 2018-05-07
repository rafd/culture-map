(ns culture-map.client.views.app
  (:require
    [culture-map.client.state.core :refer [subscribe dispatch]]
    [culture-map.client.views.styles :refer [styles-view]]
    [culture-map.client.views.custom :refer [custom-view]]
    [culture-map.client.views.country :refer [country-view]]))

(defn customs-list-view []
  (let [[_ data] @(subscribe [:page])]
    [:div.customs-list
     [:div.customs
      (doall
        (for [custom @(subscribe [:customs])]
          [:div.custom
           {:class (when (= (custom :custom/id) (data :custom-id))
                     "active")
            :key (custom :custom/id)
            :on-click
            (fn [_]
              (dispatch [:view-custom! (custom :custom/id)]))}
           (custom :custom/name)]))]
     [:button {:on-click
               (fn [_]
                 (dispatch [:new-custom!]))}
      "New custom"]]))

(defn countries-list-view []
  (let [[_ data] @(subscribe [:page])]
    [:div.customs-list
     [:div.customs
      (doall
        (for [country @(subscribe [:sidebar-countries])]
         [:div.custom
          {:class (when (= (country :country/id) (data :country-id))
                    "active")
           :key (country :country/id)
           :on-click
           (fn [_]
             (dispatch [:view-country! (country :country/id)]))}
          (country :country/name)]))]]))

(defn sidebar-view []
  [:div.sidebar
   [:h1 "Cultures"]
   [customs-list-view]
   [:h1 "Countries"]
   [countries-list-view]])

(defn app-view []
  [:div.app
   [styles-view]
   [sidebar-view]
   [:div.content
    (let [[id data] @(subscribe [:page])]
      (case id
        :home [:div]
        :custom [custom-view (data :custom-id) (data :editing?)]
        :country [country-view (data :country-id)]
        nil))]])
