(ns bbg-reframe.db)

(def default-db
  {:result nil
   :fields ["name"]
   :form {:sort-id "playability"
          :take "10"
          :higher-than "7.0"
          :players "4"
          :threshold "0.8"
          :time-limit "180"
          :show "boardgame" ;; "any" "boardgame"
          }
   :games []
   :queue #{}
   :fetching #{}
   :fetches 0
   :bg-loading false
   :error nil
   :cors-running true
   :user nil
   :ui {:open-tab ""}})



