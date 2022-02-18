(ns bbg-reframe.db
  (:require [bbg-reframe.model.db :refer [read-db]]
            [bbg-reframe.model.sort-filter :refer [game-better?]]))

(def default-db
  {:collection (read-db)
   :result (take 25 (sort game-better? (vals (read-db))))
   :fields ["name"]
   :form {:sort-id "rating"
          :take "10"
          :higher-than "7"
          :players "4"
          :threshold "0.8"
          :time-limit "120"}})



