(ns dcc.handlers.jsonweather
  (:require [clj-http.client :as client]
            [cheshire.core :refer :all]))

(defn get-four-day-forecast-json
  []
  (let [url "https://api.wunderground.com/api/8559dda6fb73dc2c/forecast/q/UK/London.json"
        response (client/get url {:as :json})
        body (:body response)
        forecast (get-in body [:forecast :simpleforecast :forecastday] "nada")]
    forecast)
  )

(defn json-data-request
  []
  {:body (generate-string {:forecast (get-four-day-forecast-json)})}
  )