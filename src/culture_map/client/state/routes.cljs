(ns culture-map.client.state.routes
  (:require
    [bloom.omni.router :as router]
    [re-frame.core :refer [dispatch]]))

(router/defroute index-path "/" []
  (dispatch [:set-page! :home {}]))

(router/defroute view-country-path "/country/:id" [id]
  (dispatch [:set-page! :country {:country-id id}]))

(router/defroute edit-country-path "/country/:id/edit" [id]
  (dispatch [:set-page! :country {:country-id id
                                  :editing? true}]))

(router/defroute view-custom-path "/custom/:id" [id]
  (dispatch [:set-page! :custom {:custom-id (UUID. id false)}]))

(router/defroute edit-custom-path "/custom/:id/edit" [id]
  (dispatch [:set-page! :custom {:custom-id (UUID. id false)
                                 :editing? true}]))
