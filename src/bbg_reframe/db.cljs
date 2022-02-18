(ns bbg-reframe.db
  (:require [bbg-reframe.model.db :refer [read-db]]
            [bbg-reframe.model.sort-filter :refer [game-better?]]))

(def default-db
  {:collection (read-db)
   :result (take 5 (sort game-better? (vals (read-db))))
   :fields ["name" "id" "rating"]})



