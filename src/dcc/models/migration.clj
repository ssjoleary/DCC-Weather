(ns dcc.models.migration
    (:require [clojure.java.jdbc :as sql]
              [dcc.models.weather :as weather]))
              
(defn migrated? []
    (-> (sql/query weather/spec
                    [(str "select count(*) from information_schema.tables "
                            "where table_name='highlow'")])
        first :count pos?))
        
(defn migrate []
    (when (not (migrated?))
        (print "Creating database structure...") (flush)
        (sql/db-do-commands weather/spec
                            (sql/create-table-ddl
                            :highlow
                            [:id :serial "PRIMARY KEY"]
                            [:high :int]
                            [:low :int]))
    (println " done")))