(ns culture-map.client.styles.colors)

(def blue "#4CAFEF")
(def red "#F55D3E")
(def grey "#878E88")
(def yellow "#F7CB15")
(def dark "#233D4D")

(def map-background "#CCC")
(def background "#f3f3f3")
(def text dark)
(def sidebar-background "#fcfcfc")
(def accent blue)

(defn map-variant-color [index count]
  (case index
    0 red
    1 yellow))
