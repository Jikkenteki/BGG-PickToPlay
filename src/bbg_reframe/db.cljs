(ns bbg-reframe.db)

(def default-db
  {:result []
   :fields ["name"]
   :form {:sort-id :playability-time
          :take "200"
          :players "4"
          :threshold "0.5"
          :time-available "120"
          :show-expansions? false
          :only-available? false}
   :games {}
   :collections []
   :queue #{}
   :fetching #{}
   :fetches 0
   :bg-loading false
   :error nil
   :network {:cors-running false}
   :user nil
   :ui {:open-tab nil}})
