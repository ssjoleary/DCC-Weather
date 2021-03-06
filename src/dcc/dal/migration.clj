(ns dcc.dal.migration
    (:require [clojure.java.jdbc :as sql]
              [dcc.dal.weather :as weather]))
              
(defn migrated? []
    (-> (sql/query weather/spec
                    [(str "select count(*) from information_schema.tables "
                            "where table_name='highlow'")])
        first :count pos?))
        
(defn migrate []
    ; If the old table exists then drop it because we generate a new set of high/low values below
    (when (migrated?)
        (print "Dropping old table...") (flush)
        (sql/db-do-commands weather/spec "drop table highlow")
        (println " done")
    )
    (print "Creating database structure...") (flush)
    (sql/db-do-commands weather/spec
                        (sql/create-table-ddl
                        :highlow
                        [:id :serial "PRIMARY KEY"]
                        [:high :int]
                        [:low :int]))
    ; Loop 28 times and insert a row into the DB where the High value is a random number between 15 and 25, Low between 5 and 15
    (loop [x 28]
        (when (> x 0)
            (sql/insert! weather/spec :highlow {:high (+ (rand-int 11) 15) :low (+ (rand-int 11) 5)})
            (recur (- x 1))))
            
    (println " done"))