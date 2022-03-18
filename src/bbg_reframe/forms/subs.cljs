(ns bbg-reframe.forms.subs
  (:require [re-frame.core :as re-frame]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [bbg-reframe.forms.utils :refer [is-substring?]]))


(re-frame/reg-sub
 ::get-value
 (fn [db [_ path]]
   (get-in db path)))

(re-frame/reg-event-db
 ::set-value!
 (fn-traced
  [db [_ path value]]
  (assoc-in db path value)))

(re-frame/reg-event-db
 ::update-value!
 (fn-traced
  [db [_ path upd-fn]]
  (update-in db path upd-fn)))

(re-frame/reg-sub
 ::dropdown-select-options
 (fn [db [_ path all-options]]
   (->> all-options
        (filter
         (fn [{:keys [_ name]}]
           (or (nil? (get-in db path)) (is-substring? (get-in db path) name)))))))

(re-frame/reg-sub
 ::dropdown-select-size
 (fn [[_ path all-options]]
   (re-frame/subscribe [::dropdown-select-options path all-options]))
 (fn [options]
   ;; an extra option is (Nothing)
   (min 10 (inc (count options)))))
