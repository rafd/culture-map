(ns culture-map.server.handler
  (:require
    [compojure.core :refer [routes]]
    [compojure.handler]
    [ring.middleware.format :refer [wrap-restful-format]]
    [culture-map.server.routes.api :as api]
    [culture-map.server.routes.spa :as spa]))

(def app
  (routes
    (-> api/routes
        (wrap-restful-format :formats [:json :transit-json])
        compojure.handler/api)
    (-> spa/routes)))
