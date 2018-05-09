(ns culture-map.client.views.app
  (:require
    [re-frame.core :refer [subscribe]]
    [culture-map.client.views.styles :refer [styles-view]]
    [culture-map.client.views.custom :refer [custom-view]]
    [culture-map.client.views.country :refer [country-view]]
    [culture-map.client.views.sidebar :refer [sidebar-view]]))

(defn app-view []
  [:div.app
   [styles-view]
   [sidebar-view]
   [:div.content
    (let [[id data] @(subscribe [:page])]
      (case id
        :home [:div]
        :custom [custom-view (data :custom-id) (data :editing?)]
        :country [country-view (data :country-id) (data :editing?)]
        nil))]])
