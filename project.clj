(defproject dcc "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/java.jdbc "0.4.1"]
                 [org.postgresql/postgresql "9.4-1201-jdbc41"]
                 [ring/ring-jetty-adapter "1.4.0"]
                 [compojure "1.4.0"]
                 [ring/ring-defaults "0.1.2"]
                 [ring/ring-core "1.3.2"]
                 [cheshire "5.6.1"]
                 [clj-http "2.2.0"]
                 [org.clojure/data.xml "0.0.8"]
                 [org.clojure/data.zip "0.1.2"]]
  :plugins [[lein-ring "0.9.7"]]
  :main ^:skip-aot dcc.core
  :target-path "target/%s"
  :ring {:handler dcc.core/application
         :init    dcc.models.migration/migrate}
  :profiles {:dev     {:dependencies [[javax.servlet/servlet-api "2.5"]
                                      [ring-mock "0.1.5"]]}
             :uberjar {:aot :all}})
