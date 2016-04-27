(ns dcc.models.weather
    (:require [clojure.java.jdbc :as sql]))
    
(def spec (or (System/getenv "DATABASE_URL")
                "postgresql://localhost:5432/randomweather"))
                
(defn all []
    (into [] (sql/query spec ["select * from randomweather"])))
    
(defn create []
    (loop [x 28]
         (when (> x 1)
         (sql/insert! "postgresql://localhost:5432/randomweather" :highlow {:high (+ (rand-int 11) 15) :low (+ (rand-int 11) 5)})
         (recur (- x 1)))))

