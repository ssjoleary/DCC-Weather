(ns dcc.core
  (:require [compojure.core :refer [defroutes]]
            [ring.adapter.jetty :as ring]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [dcc.controllers.home :as home]
            [dcc.models.migration :as schema])
  (:gen-class :main true))

(defroutes app-routes
  home/routes
  (route/resources "/")
  (route/not-found "<h1>Page not found</h1>"))

(def application
  (-> (handler/site app-routes)
      (ring.middleware.content-type/wrap-content-type {:mime-types {nil "text/html"}})))

(defn start [port]
  (ring/run-jetty application {:port  port
                               :join? false}))

(defn -main []
  (schema/migrate)
  (let [port (Integer. (or (System/getenv "PORT") "8080"))]
    (start port)))
