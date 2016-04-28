(ns dcc.models.weather
    (:require [clojure.java.jdbc :as sql]))
    
(def spec (or (System/getenv "DATABASE_URL")
                "postgresql://localhost:5432/randomweather"))
                
(defn all []
    (into [] (sql/query spec ["select * from highlow"])))
    
(defn getAvg []    
    (into [] (sql/query spec ["select avg(high), avg(low) from highlow"])))

