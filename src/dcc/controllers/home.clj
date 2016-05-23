(ns dcc.controllers.home
  (:use [clojure.data.zip.xml])
  (:require [compojure.core :refer [defroutes GET]]
            [dcc.models.weather :as model]
            [ring.util.response :refer [resource-response]]
            [cheshire.core :refer :all]
            [clj-http.client :as client]
            ;;[clojure.data.xml :as data-xml]
            ;;[clojure.data.zip.xml]
            [clojure.xml :as xml]
            [clojure.zip :as zip]))

(defn getHighAvg
  []
  (get (first (model/getAvg)) :avg)
  )

(defn getLowAvg
  []
  (get (first (model/getAvg)) :avg_2)
  )

(defn get4DayForecastJSON
  []
  (let [url "https://api.wunderground.com/api/8559dda6fb73dc2c/forecast/q/UK/London.json"
        response (client/get url {:as :json})
        body (:body response)
        forecast (get-in body [:forecast :simpleforecast :forecastday] "nada")]
    forecast)
  )

(defn parse-str [s]
  (zip/xml-zip (xml/parse (new org.xml.sax.InputSource
                               (new java.io.StringReader s)))))

(defn process-xml
  []
  )

(defn get4DayForecastXML
  []
  (let [url "http://wxdata.weather.com/wxdata/weather/local/UKXX0085?cc=*&unit=m&dayf=1"
        response (client/get url {:as :xml})
        body (:body response)
        raw-xml (parse-str body)
        forecast (xml-> raw-xml :dayf :day)]
    raw-xml)
  )

(defroutes routes
  (GET "/" [] (resource-response "index.html" {:root "public"}))
  (GET "/weather/average" [] {:body (generate-string {:high (getHighAvg), :low (getLowAvg)})})
  (GET "/weather/forecastJSON" [] {:body (generate-string {:info (get4DayForecastJSON)})})
  (GET "/weather/forecastXML" [] {:body (generate-string {:info (get4DayForecastXML)})}))