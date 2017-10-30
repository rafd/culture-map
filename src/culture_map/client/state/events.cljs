(ns culture-map.client.state.events
  (:require
    [re-frame.core :refer [dispatch reg-fx reg-event-fx]]))

(reg-event-fx :init
  (fn [{db :db} _]
    {:db {:customs []
          :countries []
          :active-custom-id nil}
     :dispatch [:get-remote-data]}))

(reg-event-fx :set-active-custom-id
  (fn [{db :db} [_ id]]
    {:db (assoc db :active-custom-id id)}))

(reg-event-fx :get-remote-data
  (fn [{db :db} _]
    {:db (assoc db
           :customs
           [{:id 1
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

           :countries
           [{:id :canada
             :name "Canada"}
            {:id :japan
             :name "Japan"}
            {:id :poland
             :name "Poland"}
            {:id :england
             :name "England"}])}))

