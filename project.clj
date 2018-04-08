(defproject culture-map "0.0.1"
  :dependencies [; server
                 [org.clojure/clojure "1.9.0-alpha19"]
                 [http-kit "2.2.0"]
                 [javax.servlet/servlet-api "2.5"]
                 [compojure "1.6.0"]
                 [ring-middleware-format "0.7.2"]
                 [hiccup "1.0.5"]
                 [human-db/core "0.5.0"]
                 [human-db/persistors.github "0.5.0"]
                 [human-db/processors.yaml "0.5.0"]

                 ; client
                 [org.clojure/clojurescript "1.9.854"]
                 [garden "1.3.2"]
                 [reagent "0.8.0-alpha2"]
                 [re-posh "0.1.5"]
                 [datascript "0.16.4"]
                 [re-frame "0.10.1"]
                 [cljs-ajax "0.7.2"]]

  :plugins [[lein-figwheel "0.5.13"]
            [lein-cljsbuild "1.1.6" :exclude [org.clojure/clojure]]]

  :main culture-map.server.core

  :clean-targets ^{:protect false}
  ["resources/public/js"]

  :figwheel {:server-port 6592}

  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src/culture_map/client"]
                        :figwheel {:on-jsload "culture-map.client.core/reload"}
                        :compiler {:main "culture-map.client.core"
                                   :asset-path "/js/out/dev"
                                   :output-to "resources/public/js/culture-map.js"
                                   :output-dir "resources/public/js/out/dev"}}

                       {:id "release"
                        :source-paths ["src/culture_map/client"]
                        :compiler {:main "culture-map.client.core"
                                   :asset-path "/js/out/prod"
                                   :output-to "resources/public/js/culture-map.js"
                                   :output-dir "resources/public/js/out/prod"
                                   :optimizations :advanced}}]}
                                   ; to debug advanced compilation issues, enable these options:
                                   ;:source-map "resources/public/js/culture-map.js.map"
                                   ;:pseudo-names true
                                   ;:pretty-print true}}]}

  :profiles {:uberjar {:aot :all
                       :prep-tasks ["compile" ["cljsbuild" "once" "release"]]}})
