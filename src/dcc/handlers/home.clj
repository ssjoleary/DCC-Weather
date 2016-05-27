(ns dcc.handlers.home
  (:require [compojure.core :refer [defroutes GET context]]
            [dcc.handlers.xmlweather :as xmlweather]
            [dcc.handlers.jsonweather :as jsonweather]
            [ring.util.response :refer [resource-response response content-type]]
            ))

(defroutes routes
  (GET "/" [] (resource-response "index.html" {:root "public"}))
  (context "/weather" []
    (GET "/average" [] (xmlweather/get-averages))
    (GET "/forecastJSON" [] (jsonweather/json-data-request))
    (GET "/forecastXML" [] (-> (response (xmlweather/xml-data-request))
                               (content-type "application/json")))))