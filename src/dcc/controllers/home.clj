(ns dcc.controllers.home
    (:require [compojure.core :refer [defroutes GET]]              
              [dcc.models.weather :as model]
              [ring.util.response :refer [resource-response]]))
              
(defn index []
    (GET "/" [] (resource-response "index.html" {:root "public"})))
      
(defroutes routes
    (GET "/" [] (index)))