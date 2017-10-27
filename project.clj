(defproject culture-map "0.0.1"
  :dependencies [[org.clojure/clojure "1.9.0-beta1"]
                 [org.clojure/clojurescript "1.9.908"]
                 [garden "1.3.2"]
                 [reagent "0.8.0-alpha1"]
                 [re-frame "0.10.1"]]

  :plugins [[lein-figwheel "0.5.13"]]

  :clean-targets ^{:protect false}
  ["resources/public/js"]

  :figwheel {:server-port 6592}

  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src"]
                        :figwheel {:on-jsload "culture-map.client.core/reload"}
                        :compiler {:main "culture-map.client.core"
                                   :asset-path "/js/out"
                                   :output-to "resources/public/js/culture-map.js"
                                   :output-dir "resources/public/js/out"}}]})
