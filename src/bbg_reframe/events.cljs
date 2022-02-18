(ns bbg-reframe.events
  (:require
   [re-frame.core :as re-frame]
   [bbg-reframe.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced]]))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
            db/default-db))


(re-frame/reg-event-db
 ::field
 (fn-traced 
  [db [_ field]]
  (let [
        new-fields 
        (if (some #(= field %) (:fields db))
          (filter #(not= field %) (:fields db))
          (conj (:fields db) field))
        ]
        (assoc db :fields new-fields))
  ))    
              
        

