(ns culture-map.client.views.app
  (:require
    [culture-map.client.state.core :refer [subscribe dispatch]]
    [culture-map.client.views.styles :refer [styles-view]]
    [culture-map.client.views.custom :refer [custom-view]]
    [culture-map.client.views.country :refer [country-view]]))

(defn customs-list-view []
  (let [[_ data] @(subscribe [:page])]
    [:div.customs.list
     (doall
       (for [custom @(subscribe [:customs])]
         [:div.custom.item
          {:class (when (= (custom :custom/id) (data :custom-id))
                    "active")
           :key (custom :custom/id)
           :on-click
           (fn [_]
             (dispatch [:view-custom! (custom :custom/id)]))}
          (custom :custom/name)]))]))


(defn countries-list-view []
  (let [[_ data] @(subscribe [:page])]
    [:div.countries.list
     (doall
       (for [country @(subscribe [:sidebar-countries])]
        [:div.country.item
         {:class (when (= (country :country/id) (data :country-id))
                   "active")
          :key (country :country/id)
          :on-click
          (fn [_]
            (dispatch [:view-country! (country :country/id)]))}
         (country :country/name)]))]))

(defn sidebar-view []
  [:div.sidebar
   [:h1 "Customs"]
   [customs-list-view]
   [:h1 "Countries"]
   [countries-list-view]
   [:button {:on-click
             (fn [_]
               (dispatch [:new-custom!]))}
    "New custom"]])

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
