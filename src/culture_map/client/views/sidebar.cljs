(ns culture-map.client.views.sidebar
  (:require
    [re-frame.core :refer [subscribe dispatch]]
    [culture-map.client.state.routes :as routes]))

(defn customs-list-view []
  (let [[_ data] @(subscribe [:page])]
    [:div.customs.list
     (doall
       (for [custom @(subscribe [:customs])]
         [:a.custom.item
          {:class (when (= (custom :custom/id) (data :custom-id))
                    "active")
           :key (custom :custom/id)
           :href (routes/view-custom-path {:id (custom :custom/id)})}
          (custom :custom/name)]))]))

(defn countries-list-view []
  (let [[_ data] @(subscribe [:page])]
    [:div.countries.list
     (doall
       (for [country @(subscribe [:sidebar-countries])]
         [:a.country.item
          {:class (when (= (country :country/id) (data :country-id))
                    "active")
           :key (country :country/id)
           :href (routes/view-country-path {:id (country :country/id)})}
          (country :country/name)]))]))

(defn sidebar-view []
  [:div.sidebar
   [:h1
    [:a {:href (routes/index-path)} "Culture Map"]]
   [:h2 "Customs"]
   [customs-list-view]
   [:h2 "Countries"]
   [countries-list-view]
   [:button {:on-click
             (fn [_]
               (dispatch [:new-custom!]))}
    "New custom"]])
