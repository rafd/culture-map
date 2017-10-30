(ns culture-map.client.state.fx.ajax
  (:require
    [ajax.core :as ajax]
    [cognitect.transit :as transit]))

(defn ajax-fx
  [{:keys [uri method params body format on-success on-error headers]
    :or {format (ajax/transit-request-format)
         on-success identity
         on-error (fn [r]
                    (.error js/console "Ajax request error" (pr-str r)))}}]
  (ajax/ajax-request
    {:uri uri
     :method method
     :body body
     :params params
     :headers headers
     :handler
     (fn [[ok response]]
       (if ok
         (on-success response)
         (on-error response)))
     :format format
     :response-format (ajax/transit-response-format
                        {:type :json
                         :reader (transit/reader :json {:handlers {"u" uuid}})})}))
