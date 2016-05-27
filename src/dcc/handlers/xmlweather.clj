(ns dcc.handlers.xmlweather
  (:require [ring.util.response :refer [resource-response]]
            [clj-http.client :as client]
            [clojure.xml :as xml]
            [clojure.zip :as zip]
            [dcc.dal.weather :as model]
            [cheshire.core :refer :all])
  (:use [clojure.data.zip.xml])
  (:import (org.xml.sax InputSource)
           (java.io StringReader)))

(defn get-high-average
  []
  (get (first (model/getAvg)) :avg))

(defn get-low-average
  []
  (get (first (model/getAvg)) :avg_2))

(defn get-averages
  []
  {:status 200
   :headers {"Content-Type" "json"}
   :body (generate-string {:high (get-high-average)
                           :low (get-low-average)})})

(defn parse-str [s]
  (zip/xml-zip (xml/parse (new InputSource
                               (new StringReader s)))))

(defn process-xml
  [daily-forecast]
  (let [z (zip/xml-zip daily-forecast)]
    {:day       (xml1-> z :day (attr :t))
     :high      (Integer. (xml1-> z :hi text))
     :low       (Integer. (xml1-> z :low text))
     :cond      (xml1-> z :part :t text)
     :high-diff (- (Integer. (xml1-> z :hi text)) (Math/round (float (get-high-average))))
     :low-diff  (- (Integer. (xml1-> z :low text)) (Math/round (float (get-low-average))))
     }))

(defn get-four-day-forecastXML
  [xml-data]
  (->> xml-data
       :content
       (into {} (filter #(= :dayf (:tag %))))
       :content
       rest
       (map process-xml)))

(defn get-xml-data
  []
  (let [url "http://wxdata.weather.com/wxdata/weather/local/UKXX0085?cc=*&unit=m&dayf=4"
        response (client/get url {:as :xml})
        body (:body response)
        raw-xml (first (parse-str body))
        four-day-forecast (get-four-day-forecastXML raw-xml)]
    four-day-forecast))

(defn xml-data-request
  []
  (generate-string {:forecast (get-xml-data)}))