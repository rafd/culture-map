(ns culture-map.client.views.styles
  (:require
    [culture-map.client.styles.app :refer [styles]]
    [garden.core :as css]))

(defn styles-view []
  [:style {:dangerouslySetInnerHTML {:__html (css/css (styles))}}])
