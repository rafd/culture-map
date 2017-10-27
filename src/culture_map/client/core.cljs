(ns culture-map.client.core
  (:require
    [reagent.core :as r]))

(enable-console-print!)

(defonce state
  (r/atom
    {:customs [{:id 1
                :name "driving side"
                :variants [{:id 0
                            :name "left"
                            :country-ids [:england :japan]}
                           {:id 1
                            :name "right"
                            :country-ids [:canada :poland]}]}
               {:id 2
                :name "bidet use"
                :variants [{:id 0
                            :name "common"
                            :country-ids [:japan]}
                           {:id 1
                            :name "uncommon"
                            :country-ids [:canada :poland]}]}
               {:id 3
                :name "same-sex marriage"
                :variants [{:id 0
                            :name "legal"
                            :country-ids [:canada]}
                           {:id 1
                            :name "illegal"
                            :country-ids [:poland]}]}]

     :countries [{:id :canada
                  :name "Canada"}
                 {:id :japan
                  :name "Japan"}
                 {:id :poland
                  :name "Poland"}
                 {:id :england
                  :name "England"}]

     :active-custom-id nil}))

(defn customs-list-view []
  [:div.customs-list
   (for [custom (@state :customs)]
     [:div.custom
      {:key (custom :id)
       :on-click
       (fn [_]
         (swap! state
           (fn [prev-state]
             (assoc prev-state :active-custom-id (custom :id)))))}
      (custom :name)])])

(defn active-custom-view []
  (let [custom-id (@state :active-custom-id)
        custom (->> (@state :customs)
                    (filter (fn [custom]
                              (= (custom :id) custom-id)))
                    first)]
    (when custom
      [:div.active-custom
       [:h1 (custom :name)]
       (doall
         (for [variant (custom :variants)]
           [:div.variant
            {:key (variant :id)}
            [:h2 (variant :name)]
            (doall
              (for [country-id (variant :country-ids)]
                (let [country (->> (@state :countries)
                                   (filter (fn [country]
                                             (= (country :id) country-id)))
                                   first)]
                  [:div.country
                   {:key country-id}
                   (country :name)])))]))])))

(defn app-view []
  [:div.app
   [:h1 "Culture Map"]
   [customs-list-view]
   [active-custom-view]])

(defn render []
  (r/render-component [app-view]
    (.. js/document (getElementById "app"))))

(defn ^:export init []
  (render))

(defn ^:export reload []
  (render))
