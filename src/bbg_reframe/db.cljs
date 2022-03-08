(ns bbg-reframe.db)

(def default-db
  {:result []
   :fields ["name"]
   :form {:sort-id :playability-time
          :take "20"
          :higher-than "7.0"
          :players "4"
          :threshold "0.5"
          :time-available "120"
          :show :boardgame}
   :games {}
   :queue #{}
   :fetching #{}
   :fetches 0
   :bg-loading false
   :error nil
   :cors-running true
   :user nil
   :ui {:open-tab nil}})
