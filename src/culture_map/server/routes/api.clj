(ns culture-map.server.routes.api
  (:require
    [compojure.core :refer [GET PATCH PUT DELETE defroutes context]]
    [culture-map.server.db :as db]))

(defroutes routes

  (context "/api" _

    (GET "/initial-data" _
      {:status 200
       :body db/data})))

