(ns culture-map.client.views.app
  (:require
    [culture-map.client.state.core :refer [subscribe dispatch]]
    [culture-map.client.views.styles :refer [styles-view]]
    [culture-map.client.views.custom :refer [custom-view]]
    [culture-map.client.views.country :refer [country-view]]
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
