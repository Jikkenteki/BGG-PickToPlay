(ns bbg-reframe.events
  (:require
   [re-frame.core :as re-frame]
   [bbg-reframe.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   [bbg-reframe.model.sort-filter :refer [sorting-fun rating-higher-than? with-number-of-players? and-filters is-playable-with-num-of-players playingtime-between?]]
   [clojure.tools.reader.edn :refer [read-string]]))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
            db/default-db))


(re-frame/reg-event-db
 ::field
 (fn [db [_ field]]
   (let [new-fields
         (if (some #(= field %) (:fields db))
           (filter #(not= field %) (:fields db))
           (conj (:fields db) field))]
     (assoc db :fields new-fields))))

(re-frame/reg-event-db
 ::update-form
 (fn [db [_ id val]]
   (let [new-db (assoc-in db [:form id] val)
        ;;  _ (println (read-string (get-in new-db [:form :higher-than])))
         new-db2
         (assoc new-db
                :result
                ((fn [sorter]
                   (take (read-string (get-in new-db [:form :take]))
                         (sort sorter
                               (filter
                                (and-filters
                                 (with-number-of-players? (read-string (get-in new-db [:form :players])))
                                 (rating-higher-than? (read-string (get-in new-db [:form :higher-than])))
                                 (playingtime-between? 0 (read-string (get-in new-db [:form :time-limit])))
                                 (is-playable-with-num-of-players
                                  (get-in new-db [:form :players])
                                  (get-in new-db [:form :threshold])))
                                (vals (db :collection))))))
                 (get sorting-fun (keyword (get-in new-db [:form :sort-id])))))]
     new-db2)))



