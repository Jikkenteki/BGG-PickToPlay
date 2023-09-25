(ns bbg-reframe.db)

(def default-db
  {:result []
   :form {:sort-id :playability-time
          :take "200"
          :players "4"
          :threshold "0.5"
          :time-available "120"
          :show-expansions? false
          :only-available? false}
   :games {}
   :collections []
   :error nil
   :network {:cors-running false
             :fetches 0
             :queue #{}
             :fetching #{}
             :loading false}
   :user nil
   :ui {:open-tab nil}})
