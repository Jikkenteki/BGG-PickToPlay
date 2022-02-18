(ns bbg-reframe.db
  (:require [bbg-reframe.model.db :refer [read-db]]))

(def default-db
  {:collection (read-db)
   :result (take 5 (vals (read-db)))
   :fields ["name" "id" "rating"]})



