(ns culture-map.client.views.app
  (:require
    [culture-map.client.state.core :refer [subscribe dispatch]]
    [culture-map.client.views.styles :refer [styles-view]]
    [culture-map.client.views.custom :refer [custom-view]]))

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

(defn sidebar-view []
  [:div.sidebar
   [:h1 "Culture Map"]
   [customs-list-view]])

(defn app-view []
  [:div.app
   [styles-view]
   [sidebar-view]
   [:div.content
    (let [[id data] @(subscribe [:page])]
      (case id
        :home [:div]
        :custom [custom-view (data :custom-id) (data :editing?)]
        nil))]])
