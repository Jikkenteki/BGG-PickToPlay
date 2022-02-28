(ns bbg-reframe.db)

(def default-db
  {:result nil
   :fields ["name"]
   :form {:sort-id "playable"
          :take "10"
          :higher-than "7.0"
          :players "4"
          :threshold "0.8"
          :time-limit "180"}
   :games []
   :queue #{}
   :fetching #{}
   :fetches 0
   :error nil
   :cors-running false
   :user nil
   :ui {:sort-by-button-state false}})



