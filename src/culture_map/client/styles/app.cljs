(ns culture-map.client.styles.app
  (:require
    [garden.stylesheet :refer [at-import]]
    [culture-map.client.styles.colors :as colors]))

(def font-size-title "1rem")
(def font-size-text "0.8rem")

(defn >map []
  [:>.map
   {:width "100%"
    :max-width "600px"
    :margin "1em auto"}

   ["[data-cc]"
    {:fill colors/map-background}]])

(defn styles []
  [(at-import "https://fonts.googleapis.com/css?family=Open+Sans")
   [:body
    {:margin 0
     :font-family "'Open Sans'"
     :color colors/text}

    [:#app

     [:>.app
      {:display "flex"
       :font-size font-size-text
       :background colors/background
       :width "100%"
       :height "100%"}

      (let [pad "1rem"]
        [:>.sidebar
         {:background colors/sidebar-background
          :margin-left "2em"
          :padding pad
          :border-left [[colors/accent "0.5em" "solid"]]}

         [:>h1
          {:font-size font-size-title
           :margin 0}

          [:>a
           {:text-decoration "none"}]]

         [:>h2
          {:font-size font-size-title
           :margin [["1em" 0 "0.5em"]]}]

         [:>.list
          {:margin [[0 (str "-" pad)]]}

          [:>.item
           {:padding [[0 pad]]
            :display "block"
            :text-decoration "none"
            :height "1.5em"
            :white-space "nowrap"
            :line-height "1.5em"}

           [:&.active
            {:background [[colors/blue "!important"]]
             :color colors/white}]

           [:&:hover
            {:background colors/background
             :cursor "pointer"}]]]])

      [:>.content
       {:height "100%"
        :flex-grow 1
        :flex-shrink 1
        :overflow-x "scroll"
        :position "relative"
        :padding "2em"
        :box-sizing "border-box"}

       [:>.active-custom
        {:text-align "center"}

        [:>h1
         {:font-size font-size-title
          :margin 0
          :text-align "center"}]

        (>map)

        [:>.edit
         {:position "absolute"
          :top 0
          :right 0}]

        [:>.variants
         {:display "flex"
          :text-align "left"}

         [:&:before
          :&:after
          {:content "\"\""
           :flex-grow 1}]

         [:>.variant
          {:flex-grow 1}

          [:>.color-square
           {:height "1em"
            :width "1em"
            :margin-left "-1.5em"
            :margin-right "0.5em"
            :display "inline-block"}]

          [:>h2
           {:font-size font-size-title
            :margin 0
            :display "inline-block"}]

          [:>.countries
           {:column-count 2
            :column-gap "1em"}

           [:>.country
            {:margin 0}]]]]]]]]]])




