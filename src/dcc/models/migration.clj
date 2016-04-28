(ns dcc.models.migration
    (:require [clojure.java.jdbc :as sql]
              [dcc.models.weather :as weather]))
              
(defn migrated? []
    (-> (sql/query weather/spec
                    [(str "select count(*) from information_schema.tables "
                            "where table_name='highlow'")])
        first :count pos?))
        
(defn migrate []
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
    (loop [x 28]
        (when (> x 1)
            (sql/insert! weather/spec :highlow {:high (+ (rand-int 11) 15) :low (+ (rand-int 11) 5)})
            (recur (- x 1))))
    (println " done"))